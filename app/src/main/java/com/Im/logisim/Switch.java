package com.Im.logisim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 * A class which represents an input switch. This class contains two bitmap images, one that
 * represents the switches off state, and the other which represents the switches on state. Because
 * an object of this class will be contained within a grid of a specific size, an instance of this
 * class also contains its grid position in terms of a row and column. As this class implements
 * the Node interface, the state of an object of this class will be returned by the eval() method
 * it implements.
 */
public class Switch implements Node {
    private int column;
    private int row;
    private boolean toggleState;
    private Bitmap onSwitchBitmap;
    private Bitmap offSwitchBitmap;

    public Switch(Context context, int row, int column) {
        /*
        * The current image size that works well with different smart phone display sizes is 60x60
        * pixels. If a different image is used, then this image size value will vary on the original
        * image size.
        */
        onSwitchBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.onswitch);
        offSwitchBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.offswitch);
        toggleState = false;
        this.column = column;
        this.row = row;
    }

    public void draw(Canvas canvas, int gridWidth, int gridHeight) {
        /*
        * Variables below are used to ensure the switch image is centered within the grid square it
        * will be drawn in. As the offSwitch bitmap and onSwitch bitmap are the same size, we only
        * need to get the height of one of the bitmaps.
        */
        int bm_Width = offSwitchBitmap.getWidth();
        int bm_Height = offSwitchBitmap.getHeight();
        int horizontalCenter = (column*gridWidth) + (gridWidth/2 - bm_Width/2);
        int verticalCenter = (row*gridHeight) + (gridHeight/2 - bm_Height/2);

        if(toggleState) {
            canvas.drawBitmap(onSwitchBitmap, horizontalCenter, verticalCenter, null);
        }
        else {
            canvas.drawBitmap(offSwitchBitmap, horizontalCenter, verticalCenter, null);
        }
    }

    public void toggleSwitch() { toggleState = !toggleState; }

    //return the switches state.
    public boolean eval() { return this.toggleState; }

    public void setRow(int row) { this.row = row; }

    public void setColumn(int column) { this.column = column; }

    public int getRow() { return row; }

    public int getColumn() { return column; }
}