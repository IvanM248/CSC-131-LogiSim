package com.Im.logisim;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 * A class which represents an output light. An instance of this class will contain two bitmap
 * images, one that represents the light bulbs off state, and the other which represents the light
 * bulbs on state. Because an object of this class will be contained within a grid of a specific
 * size, an instance of this class also contains its grid position in terms of a row and column.
 * This class will not implement the Node interface as its state depends on the evaluation of a
 * logic circuit that it is connected to. However, this class will contain an object of type Node.
 * This object will be the final element connected to the light bulb to complete a circuit. This
 * Node object can be a logic gate object or a switch object.
 */
public class Light {
    private Bitmap lightBulbOn, lightBulbOff;
    private int row, column;
    private boolean state;
    private Node n;

    public Light(Context context, int row, int column) {
        /*
        * The current image sized used that works well with multiple smart phone display sizes is
        * 90x72 pixels. If a different image is used, then the optimal image size value will vary
        * on the original image size.
        */
        lightBulbOff = BitmapFactory.decodeResource(context.getResources(), R.drawable.lightbulboff);
        lightBulbOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.lightbulbon);
        this.column = column;
        this.row = row;
        state = false;
    }

    public void draw(Canvas canvas, int gridWidth, int gridHeight) {
        /*
        * Variables below are used to ensure the switch image is centered within the grid square it
        * will be drawn in. Since the offSwitch bitmap and onSwitch bitmap are the same size,we only
        * need to get the height of one of the bitmaps.
        */
        int bm_Width = lightBulbOn.getWidth();
        int bm_Height = lightBulbOn.getHeight();
        int horizontalCenter = (column*gridWidth) + (gridWidth/2 - bm_Width/2);
        int verticalCenter = (row*gridHeight) + (gridHeight/2 - bm_Height/2);

        if(state) {
            canvas.drawBitmap(lightBulbOn, horizontalCenter, verticalCenter, null);
        }
        else {
            canvas.drawBitmap(lightBulbOff, horizontalCenter, verticalCenter, null);
        }
    }

    public void setSource(Node n){ this.n = n; }

    //A light bulbs state is set by calling the eval method on its only input connection.
    public void setState() {
        if(n != null){
            this.state = n.eval();
        }
        else{
            this.state = false;
        }
    }

    public Node getSource() { return n; }

    public int getColumn() { return column; }

    public int getRow() { return row; }

    public void setRow(int row) { this.row = row; }

    public void setColumn(int column) { this.column = column; }
}
