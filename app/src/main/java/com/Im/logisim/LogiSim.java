package com.Im.logisim;
/*
 * The LogiSim class contains all of the logic necessary to display
 * and run the user interface. The user interface, as described in the LogiSim
 * design document, consists of all the buttons necessary for creating logic
 * gates, switches, and output light bulbs as well as the buttons necessary for
 * moving on screen elements and deleting on screen elements, and saving/loading
 * schematics.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.List;

public class LogiSim extends Activity {
    ImageView myImageView;
    Bitmap myBitmap;
    Canvas myCanvas;
    DisplayInfo display;
    GridUI myGridUI;
    List<Node> gates;
    List<Switch> switches;
    List<Light> lights;
    List<Wire> wires;
    Save saveA, saveB, saveC;
    int numRows, numColumns;
    GridLocation selectedObj1, selectedObj2, uiButtonPress;
    //The boolean values below hold appropriate states based on any UI button
    //pressed on the screen.
    boolean addGate, addSwitch, addLightBulb;
    boolean moveElement, deleteElement, objectSelected, runSim;
    boolean establishConnection, connectionObjectsSelected, savePressed, selectingSavePreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = new DisplayInfo(getWindowManager());
        myBitmap = Bitmap.createBitmap(display.getScreenWidth(), display.getScreenHeight(), Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(myBitmap);
        myImageView = new ImageView(this);
        numRows = 5;
        numColumns = 6;
        myGridUI = new GridUI(numColumns, numRows, display.getScreenWidth(), display.getScreenHeight());
        gates = new ArrayList<>();
        switches = new ArrayList<>();
        lights = new ArrayList<>();
        wires = new ArrayList<>();
        selectedObj1 = new GridLocation();
        selectedObj2 = new GridLocation();
        uiButtonPress = new GridLocation();
        saveA = new Save(this);
        saveB = new Save(this);
        saveC = new Save(this);

        setContentView(myImageView);
        draw();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int) (event.getX() / myGridUI.getGridWidth());
            int row = (int) (event.getY() / myGridUI.getGridHeight());
            checkUIButtonPress(row, column);
        }
        return true;
    }

    /*
    * Handle setting appropriate states based on what the user touches on the
    * screen.
    */
    public void checkUIButtonPress(int row, int column) {
        if (runSim) {
            runSimPressed(row, column);
            switchPressed(row, column);
        }
        else {
            addGateButtonPressed(row, column);
            addSwitchButtonPressed(row, column);
            addLightBulbButtonPressed(row, column);
            addWireButtonPressed(row, column);
            moveButtonPressed(row, column);
            deleteButtonPressed(row, column);
            loadSave(row,column);
            saveButtonPressed(row, column);
            runSimPressed(row, column);
        }
        draw();
    }

    public void loadSave(int row, int column) {
        if (row == 4 && column == 3 && !savePressed) {
            saveA.load(switches, gates, lights, wires);
        }
       else if (row == 4 && column == 4 && !savePressed) {
            saveB.load(switches, gates, lights, wires);
        }
        else if (row == 4 && column == 5 && !savePressed) {
            saveC.load(switches, gates, lights, wires);
        }
    }

    public void saveButtonPressed(int row, int column) {
        if (row == 4 && column == 2) {
            savePressed = true;
            selectingSavePreset = true;
        } else if (savePressed) {
            saveCurrentSchematic(row, column);
            savePressed = false;
            selectingSavePreset = false;
        }
    }

    public void saveCurrentSchematic(int row, int column) {
        if (row == 4 && column == 3) {
            saveA.save(switches, gates, lights, wires);
        }
        else if (row == 4 && column == 4) {
            saveB.save(switches, gates, lights, wires);
        }
        else if (row == 4 && column == 5) {
            saveC.save(switches, gates, lights, wires);
        }
    }

    public void switchPressed(int row, int column) {
        for (Switch s : switches) {
            if (s.getRow() == row && s.getColumn() == column) {
                s.toggleSwitch();
            }
        }
        updateLightBulbsState();
    }

    public void updateLightBulbsState() {
        for (Light light : lights) {
            light.setState();
        }
    }

    public void runSimPressed(int row, int column) {
        if (row == 4 && column == 1) {
            runSim = !runSim;
            myGridUI.toggleStatusIndicator();
        }
        if (runSim) {
            updateLightBulbsState();
        }
    }

    public void addWireButtonPressed(int row, int column) {
        if (row == 2 && column == 1) {
            establishConnection = true;
        }
        else if (establishConnection) {
            selectedObj1.x = row;
            selectedObj1.y = column;
            establishConnection = false;
            connectionObjectsSelected = true;
        }
        else if (connectionObjectsSelected) {
            selectedObj2.x = row;
            selectedObj2.y = column;
            connectionObjectsSelected = false;
            setUpConnection();
        }
    }

    public void setUpConnection() {
        //First selected object can be either a switch or logic gate.
        Node obj1 = getSelectedObj1();
        /*
         * We do not know the type of the second selected object so we cannot assign it to
         * a variable.
         */
        if (obj1 != null && getSelectedObj2() != null) {
            connectObjects(obj1, getSelectedObj2());
        } else if (obj1 != null && getLightObj2() != null) {
            connectObjects(obj1, getLightObj2());
        }
    }

    /*
    * Overloaded method that will connect two Node objects. The first node object (obj1) can be
    * either a switch or a logic gate. The second node object (obj2) can only be a logic gate
    * object. Note, obj1 is connected to obj2. That is, if the connection is established, then obj2
    * will contain a reference to obj1. If the connection is established, then a visual wire object
    * is created between the two objects.
    */
    public void connectObjects(Node obj1, Node obj2) {
        if(ConnectionHandler.nodeToNode(obj1, obj2)){
            wires.add(new Wire(selectedObj1, selectedObj2));
        }
    }

    /*
    * Overloaded method that will connect any node object to a light object. The node object may be
    * a switch or and logic gate object. If the connection is established, then a visual wire object
    * is created between the two objects.
    */
    public void connectObjects(Node obj1, Light light) {
        if (ConnectionHandler.nodeToLightBulb(obj1, light, lights)) {
            wires.add(new Wire(selectedObj1, selectedObj2));
        }
    }

    public Node getSelectedObj1() {
        for (Node gate : gates) {
            if (gate.getRow() == selectedObj1.x && gate.getColumn() == selectedObj1.y) {
                return gate;
            }
        }

        for (Switch s : switches) {
            if (s.getRow() == selectedObj1.x && s.getColumn() == selectedObj1.y) {
                return s;
            }
        }
        return null;
    }

    public Node getSelectedObj2() {
        for (Node gate : gates) {
            if (gate.getRow() == selectedObj2.x && gate.getColumn() == selectedObj2.y) {
                return gate;
            }
        }
        return null;
    }

    public Light getLightObj2() {
        for (Light light : lights) {
            if (light.getRow() == selectedObj2.x && light.getColumn() == selectedObj2.y) {
                return light;
            }
        }
        return null;
    }

    public void deleteButtonPressed(int row, int column) {
        if (row == 3 && column == 1) {
            deleteElement = true;
        } else if (deleteElement) {
            deleteElement(row, column);
            deleteElement = false;
        }
    }

    /*
    * This method will search through all of the lists containing logic gates, switches, and lights
    * searching for the item to be deleted. Once the selected item to be deleted is found, the
    * ConnectionHandler class will search every object that may contain a reference to the object
    * that is being deleted and reset that objects connection to null.
    */
    public void deleteElement(int row, int column) {
        for (int i = 0; i < gates.size(); i++) {
            Node gate = gates.get(i);
            if (gate.getRow() == row && gate.getColumn() == column) {
                ConnectionHandler.resetNodeConnections(gate, gates);
                ConnectionHandler.resetLightConnections(gate, lights);
                deleteWires(row, column);
                gates.remove(i);
                return;
            }
        }

        for (int i = 0; i < switches.size(); i++) {
            Node s = switches.get(i);
            if (s.getRow() == row && s.getColumn() == column) {
                ConnectionHandler.resetNodeConnections(s, gates);
                ConnectionHandler.resetLightConnections(s, lights);
                deleteWires(row, column);
                switches.remove(i);
                return;
            }
        }

        for (int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);
            if (light.getRow() == row && light.getColumn() == column) {
                deleteWires(row, column);
                lights.remove(i);
                return;
            }
        }
    }

    public void deleteWires(int row, int column) {
        /*
        * Start removing from the back of the array list and move towards the front. If we remove
        * from the front, then we can potentially skip over objects that need to be deleted.
        */
        for (int i = wires.size() - 1; i >= 0; i--) {
            Wire wire = wires.get(i);
            int startRow = wire.getStartRow();
            int startColumn = wire.getStartColumn();
            int endRow = wire.getEndRow();
            int endColumn = wire.getEndColumn();
            if((row==startRow && column==startColumn)||(row==endRow && column==endColumn)){
                wires.remove(i);
            }
        }
    }

    public void moveButtonPressed(int row, int column) {
        if (row == 3 && column == 0) {
            moveElement = true;
        } else if (moveElement) {
            objectSelected = true;
            moveElement = false;
            selectedObj1.x = row;
            selectedObj1.y = column;
        } else if (objectSelected) {
            moveObject(row, column);
            objectSelected = false;
        }
    }

    public void moveObject(int row, int column) {
        if (column != 0 && column != 1 && row != 4 && isEmptyGridSquare(row, column)) {
            for (Node node : gates) {
                if (node.getRow() == selectedObj1.x && node.getColumn() == selectedObj1.y) {
                    node.setRow(row);
                    node.setColumn(column);
                    moveConnections(row, column);
                    return;
                }
            }
            for (Switch s : switches) {
                if (s.getRow() == selectedObj1.x && s.getColumn() == selectedObj1.y) {
                    s.setRow(row);
                    s.setColumn(column);
                    moveConnections(row, column);
                    return;
                }
            }
            for (Light light : lights) {
                if (light.getRow() == selectedObj1.x && light.getColumn() == selectedObj1.y) {
                    light.setRow(row);
                    light.setColumn(column);
                    moveConnections(row, column);
                    return;
                }
            }
        }
    }

    public void moveConnections(int row, int column) {
        for (Wire w : wires) {
            if (w.getStartRow() == selectedObj1.x && w.getStartColumn() == selectedObj1.y) {
                GridLocation newLoc = new GridLocation(row, column);
                w.relocateStart(newLoc);
            } else if (w.getEndRow() == selectedObj1.x && w.getEndColumn() == selectedObj1.y) {
                GridLocation newLoc = new GridLocation(row, column);
                w.relocateEnd(newLoc);
            }
        }
    }

    public void addLightBulbButtonPressed(int row, int column) {
        if (row == 2 && column == 0) {
            addLightBulb = true;
        } else if (addLightBulb) {
            createLightBulb(row, column);
            addLightBulb = false;
        }
    }

    public void createLightBulb(int row, int column) {
        if (column != 0 && column != 1 && row != 4 && isEmptyGridSquare(row, column)) {
            lights.add(new Light(this, row, column));
        }
    }

    public void addSwitchButtonPressed(int row, int column) {
        if (row == 1 && column == 1) {
            addSwitch = true;
        } else if (addSwitch) {
            addSwitch = false;
            createSwitch(row, column);
        }
    }

    public void createSwitch(int row, int column) {
        if (column != 0 && column != 1 && row != 4 && isEmptyGridSquare(row, column)) {
            switches.add(new Switch(this, row, column));
        }
    }

    public void addGateButtonPressed(int row, int column) {
        if (column == 0 || column == 1) {
            addGate = true;
            uiButtonPress.x = row;
            uiButtonPress.y = column;
        } else if (addGate && isEmptyGridSquare(row, column)) {
            addGateNode(row, column);
            addGate = false;
        }
    }

    public void addGateNode(int gateRow, int gateColumn) {
        if (uiButtonPress.x == 0 && uiButtonPress.y == 0 && gateRow != 4) {
            gates.add(new AndGate(this, gateRow, gateColumn));
        }
        else if (uiButtonPress.x == 0 && uiButtonPress.y == 1 && gateRow != 4) {
            gates.add(new OrGate(this, gateRow, gateColumn));
        }
        else if (uiButtonPress.x == 1 && uiButtonPress.y == 0 && gateRow != 4) {
            gates.add(new NotGate(this, gateRow, gateColumn));
        }
    }

    public boolean isEmptyGridSquare(int row, int column) {
        for (Node node : gates) {
            if (node.getRow() == row && node.getColumn() == column) {
                return false;
            }
        }

        for (Switch s : switches) {
            if (s.getRow() == row && s.getColumn() == column) {
                return false;
            }
        }

        for (Light light : lights) {
            if (light.getRow() == row && light.getColumn() == column) {
                return false;
            }
        }
        return true;
    }

    public void draw() {
        myImageView.setImageBitmap(myBitmap);
        myGridUI.draw(myCanvas);
        drawConnections();
        drawGates();
        drawSwitches();
        drawLights();
    }

    public void drawConnections() {
        if (wires != null) {
            for (Wire wire : wires) {
                wire.draw(myCanvas, myGridUI.getGridWidth(), myGridUI.getGridHeight());
            }
        }
    }

    public void drawGates() {
        for (Node node : gates) {
            node.draw(myCanvas, myGridUI.getGridWidth(), myGridUI.getGridHeight());
        }
    }

    public void drawSwitches() {
        for (Switch s : switches) {
            s.draw(myCanvas, myGridUI.getGridWidth(), myGridUI.getGridHeight());
        }
    }

    public void drawLights() {
        for (Light light : lights) {
            light.draw(myCanvas, myGridUI.getGridWidth(), myGridUI.getGridHeight());
        }
    }
}