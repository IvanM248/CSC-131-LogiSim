package com.Im.logisim;

import  android.graphics.Canvas;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 * A class which represents an and gate. An instance of this class will contain one bitmap image
 * representing an and gate. Because an object of this class will be contained within a grid of
 * a specific size, this class also contains its grid position in terms of a row and column.
 * Because this class implements the Node interface, the state of an object of this class will be
 * returned by the eval() method it implements.
 */
public class AndGate implements Node {
    //The a and b variables represent the And gates two input connectors.
    private Node a, b;
    private int row, column;
    private Bitmap imageBitmap;

    public AndGate(Context context, int row, int column) {
        /*
        * The Current image sized used that works well with multiple smart phone display sizes is
        * 90x54 pixels. If a different image is used, then the image size value will vary on the
        * original image size.
        */
        imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.andgate);
        setRow(row);
        setColumn(column);
        this.a = null;
        this.b = null;
    }

    public void draw(Canvas canvas, int blockWidth, int blockHeight) {
        /*
        * The Variables below are used to ensure that when the and gate image is drawn
        * to the screen in a specified grid square, it will be centered within that grid
        * square.
        */
        int bmWidth = imageBitmap.getWidth();
        int bmHeight = imageBitmap.getHeight();
        int horizontalCenter = (column * blockWidth) + (blockWidth / 2 - bmWidth / 2);
        int verticalCenter = (row * blockHeight) + (blockHeight / 2 - bmHeight / 2);

        canvas.drawBitmap(imageBitmap, horizontalCenter, verticalCenter, null);
    }

    //Return an And Gates state based on its two input connectors.
    public boolean eval(){
        if(this.a == null || this.b == null){
            return false;
        }
        else {
            return (this.a.eval() && this.b.eval());
        }
    }

    public void setSourceA(Node a){ this.a = a; }

    public void setSourceB(Node b){ this.b = b; }

    public Node getSourceA() { return a;}

    public Node getSourceB() {return b;}

    public int getRow() { return row; }

    public int getColumn() { return column; }

    public void setRow(int row) { this.row = row; }

    public void setColumn(int column) { this.column = column; }
}
