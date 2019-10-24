package com.Im.logisim;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/*
 * This class will save a logic circuit schematic. Because the logic gate and light objects
 * can contain references to other objects, in order to create a deep copy of each object in the
 * schematic, you must recreate each object in the schematic with the same primitive information
 * that each logic gate and light bulb object contain. After they are recreated, the new objects
 * must be re-connected to each other in the same manner that the schematic outlines.
 */
public class Save{
    private List<Node> gates;
    private List<Switch> switches;
    private List<Light> lights;
    private List<Wire> wires;
    private Context context;

    public Save(Context context){
        this.context = context;
        /*
        * All lists are initially empty. If the user loads a save that is empty, then the current
        * schematic on the screen will be wiped.
        */
        this.switches = new ArrayList<>();
        this.gates = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.wires = new ArrayList<>();
    }

    public void save(List<Switch> switches, List<Node>gates, List<Light>lights, List<Wire>wires){
        this.switches = copySwitches(switches);
        this.gates = copyGates(gates);
        this.lights = copyLights(lights);
        this.wires = copyWires(wires);
        connectItems(this.switches, this.gates, this.wires, this.lights);
    }

    /*
    * Inorder to load a saved schematic, we must create a copy of the current save. If we do not
    * create a copy of the current save and instead load the current save directly, then the user
    * will have the ability to unintentionally alter the currently saved schematic. Because the
    * lists that are passed in as a parameter are references passed by value, we cannot directly
    * assign a copy of each list to the lists that are passed in as parameters.
    */
    public void load(List<Switch> switches, List<Node>gates, List<Light>lights, List<Wire>wires){
        List<Switch> s = copySwitches(this.switches);
        List<Node> g = copyGates(this.gates);
        List<Light> l = copyLights(this.lights);
        List<Wire> w = copyWires(this.wires);
        connectItems(s, g, w, l);

        //Clear out current schematic within the lists passed as a parameter.
        switches.removeAll(switches);
        gates.removeAll(gates);
        lights.removeAll(lights);
        wires.removeAll(wires);

        //load a copy of the current save schematic into the the lists passed as a parameter.
        switches.addAll(s);
        gates.addAll(g);
        lights.addAll(l);
        wires.addAll(w);
    }

    private List<Switch> copySwitches(List<Switch> switches){
        List<Switch> tempSwitches = new ArrayList<>();
        for(Switch s: switches){
            Switch switchCopy = new Switch(context, s.getRow(),s.getColumn());
            if(s.eval()){
                switchCopy.toggleSwitch();
            }
            tempSwitches.add(new Switch(context,s.getRow(),s.getColumn()));
        }
        return tempSwitches;
    }

    private List<Node> copyGates(List<Node> gates){
        List<Node> tempGates = new ArrayList<>();
        for(Node gate : gates){
            if(gate instanceof AndGate) {
                tempGates.add(new AndGate(context, gate.getRow(), gate.getColumn()));
            }
            else if(gate instanceof OrGate){
                tempGates.add(new OrGate(context, gate.getRow(),gate.getColumn()));
            }
            else{
                tempGates.add(new NotGate(context, gate.getRow(), gate.getColumn()));
            }
        }
        return tempGates;
    }

    private List<Light> copyLights(List<Light> lights){
        List<Light> tempLights = new ArrayList<>();
        for(Light light : lights){
            tempLights.add(new Light(context, light.getRow(), light.getColumn()));
        }
        return tempLights;
    }

    private List<Wire> copyWires(List<Wire> wires){
        List<Wire> tempWires = new ArrayList<>();
        GridLocation tempStart = new GridLocation();
        GridLocation tempEnd = new GridLocation();
        for(Wire wire: wires){
            tempStart.x = wire.getStartRow();
            tempStart.y = wire.getStartColumn();
            tempEnd.x = wire.getEndRow();
            tempEnd.y = wire.getEndColumn();
            tempWires.add(new Wire(tempStart,tempEnd));
        }
        return tempWires;
    }

    private void connectItems(List<Switch>switches,List<Node>gates,List<Wire>wires,List<Light>lights){
        //Check switch connections. Establish a connection if a wire connection is found.
        for(Switch s : switches){
            for(Wire w : wires){
                if(s.getRow()==w.getStartRow() && s.getColumn()==w.getStartColumn()) {
                    connectSwitches(s, w.getEndRow(), w.getEndColumn(), lights, gates);
                }
            }
        }

        //Check gate connections. Establish a connection if a wire connection is found.
        for(Node gate: gates){
            for(Wire w : wires){
                if(gate.getRow() == w.getStartRow() && gate.getColumn() == w.getStartColumn()){
                    connectGates(gate, w.getEndRow(), w.getEndColumn(),lights, gates);
                }
            }
        }
    }

    private void connectGates(Node gate, int endRow, int endColumn,List<Light>lights,List<Node>gates){
        for(Node g : gates){
            if(g.getRow() == endRow && g.getColumn() == endColumn){
                ConnectionHandler.nodeToNode(gate,g);
            }
        }

        for(Light light : lights){
            if(light.getRow() == endRow && light.getColumn() == endColumn){
                ConnectionHandler.nodeToLightBulb(gate, light, lights);
            }
        }
    }

    private void connectSwitches(Switch s,int endRow,int endColumn,List<Light>lights, List<Node>gates){
        for(Node gate : gates) {
            if (gate.getRow() == endRow && gate.getColumn() == endColumn) {
                ConnectionHandler.nodeToNode(s, gate);
            }
        }

        for(Light light : lights){
            if(light.getRow() == endRow && light.getColumn() == endColumn) {
                ConnectionHandler.nodeToLightBulb(s, light, lights);
            }
        }
    }
}
