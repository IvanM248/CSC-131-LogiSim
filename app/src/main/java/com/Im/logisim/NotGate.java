package com.Im.logisim;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 * A class which represents a not gate. An instance of this class will contain one bitmap
 * image representing the not gate. Because an object of this class will be contained within a grid
 * of a specific size, an instance of this class also contains its grid position in terms of a row
 * and column. As this class implements the Node interface, the state of an object of this class
 * will be returned by the eval() method it will implements.
 */
public class NotGate implements Node {
    //The variable n represents the Not gates single input connector.
    private Node n;
    private int row, column;
    private Bitmap imageBitmap;

    public NotGate(Context context, int row, int column) {
        /*
        * Current image size used that works well with multiple screen sizes is 90x49 pixels.If a
        * different image is used, then the image size value will vary on the original image size.
        */
        imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.notgate);
        this.column = column;
        this.row = row;
        this.n = null;
    }

    //Returns the state of a Not gate object based on its input connector.
    public boolean eval(){
        if(n == null){
            return true;
        }
        else{
            return !n.eval();
        }
    }

    public void draw(Canvas canvas , int gridWidth, int gridHeight) {
        /*
        * Variables below are used to ensure that the gate image is centered within the grid square
        * it will be drawn in.
        */
        int bm_Width = imageBitmap.getWidth();
        int bm_Height = imageBitmap.getHeight();
        int horizontalCenter = (column*gridWidth) + (gridWidth/2 - bm_Width/2);
        int verticalCenter = (row*gridHeight) + (gridHeight/2 - bm_Height/2);

        //Draw the and gate in the specified grid square.
        canvas.drawBitmap(imageBitmap, horizontalCenter, verticalCenter, null);
    }

    public Node getSource() { return n; }

    public void setSource(Node n){ this.n = n; }

    public void setRow(int row) { this.row = row; }

    public void setColumn(int column) { this.column = column; }

    public int getRow() { return row; }

    public int getColumn() { return column; }
}
