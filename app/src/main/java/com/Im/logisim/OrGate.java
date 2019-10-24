package com.Im.logisim;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 * A class which represents an or gate. An instance of this class will contain one bitmap image
 * representing an or gate. Because an object of this class will be contained within a grid of a
 * specific size, an instance of this class also contains its grid position in terms of a row and
 * column. As this class implements the Node interface, the state of an object of this class will
 * be returned by the eval() method it implements.
 */
public class OrGate implements Node {
    //The a and b variables represent the Or gates two input connectors.
    private Node a,b;
    private int row, column;
    private Bitmap imageBitmap;

    public OrGate(Context context, int row, int column) {
        /*
        * Current image sized used that works well with multiple smart phone display sizes is 90x46
        * pixels. If a different image is used, then the image size value will vary on the original
        * image size.
        */
        imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.orgate);
        this.column = column;
        this.row = row;
        this.a = null;
        this.b = null;
    }

    public void draw(Canvas canvas , int gridWidth, int gridHeight) {
        /*
        * The variables below are used to ensure that the gate image is centered within the grid
        * square it will be drawn in.
        */
        int bm_Width = imageBitmap.getWidth();
        int bm_Height = imageBitmap.getHeight();
        int horizontalCenter = (column*gridWidth) + (gridWidth/2 - bm_Width/2);
        int verticalCenter = (row*gridHeight) + (gridHeight/2 - bm_Height/2);

        canvas.drawBitmap(imageBitmap, horizontalCenter, verticalCenter, null);
    }

    public void setSourceA(Node a){ this.a = a; }

    public void setSourceB(Node b){ this.b = b; }

    //Return the Or Gates state based on its two input connectors.
    public boolean eval(){
        if(a == null && b != null){
            return b.eval();
        }
        else if(a != null && b == null) {
            return a.eval();

        }
        else{
            return (a.eval() || b.eval());
        }
    }

    public Node getSourceA() { return a;}

    public Node getSourceB() {return b;}

    public void setRow(int row) { this.row = row; }

    public void setColumn(int column) { this.column = column; }

    public int getRow() { return row; }

    public int getColumn() { return column; }
}
