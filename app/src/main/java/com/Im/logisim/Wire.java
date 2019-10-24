package com.Im.logisim;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

/*
 * This class represents a visual wire connection established between two objects on the grid.
 */
public class Wire {

    private int startRow;
    private int startColumn;
    private int endRow;
    private int endColumn;
    private Paint blackPaint;

    public Wire(GridLocation start, GridLocation end) { this.establishConnection(start, end); }

    public void establishConnection(GridLocation start, GridLocation end){
        blackPaint = new Paint(Color.BLACK);
        this.startRow = start.x;
        this.startColumn = start.y;
        this.endRow = end.x;
        this.endColumn = end.y;
    }

    public void relocateStart(GridLocation start){
        this.startRow = start.x;
        this.startColumn = start.y;
    }

    public void relocateEnd(GridLocation end){
        this.endRow = end.x;
        this.endColumn = end.y;
    }

    public void draw(Canvas canvas, int gridWidth, int gridHeight) {

        blackPaint.setStrokeWidth(5);
        //Variables below are be used to draw a line from the center of a specified
        //grid square to the center of a different specified grid square.
        int startX = startColumn * gridWidth +(gridWidth/2);
        int startY = startRow * gridHeight + (gridHeight/2);
        int endX = endColumn * gridWidth + (gridWidth/2);
        int endY = endRow * gridHeight + (gridHeight/2);
        canvas.drawLine(startX, startY, endX, endY, blackPaint);

    }

    public int getStartRow(){ return this.startRow; }

    public int getStartColumn() { return this.startColumn; }

    public int getEndRow() { return this.endRow; }

    public int getEndColumn() { return this.endColumn; }
}
