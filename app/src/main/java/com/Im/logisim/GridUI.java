package com.Im.logisim;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
/*
 * A class that represents a grid of a user specified size. The grid contains all UI buttons needed
 * to create and manipulate logic circuits. The logic behind the working UI buttons is not handled
 * in this class. This class merely displays the entire UI to the screen.
 */
public class GridUI {
    private int numColumns, numRows;
    private int screenWidth, screenHeight;
    private int gridWidth, gridHeight;
    private boolean statusIndicator;
    private Paint paint;

    public GridUI(int numColumns, int numRows, int screenWidth, int screenHeight) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.statusIndicator = false;
        /*
         * By dividing the screenHeight and screenWidth by the numRows and numColumns respectively,
         * we evenly partition the screen into our defined grid size.
         */
        gridHeight = screenHeight/ numRows;
        gridWidth = screenWidth / numColumns;
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLACK);

        /*
        * Draw the grids vertical lines. The second vertical grid line that is drawn will be drawn
        * slightly thicker than the rest in order to indicate that the buttons to the left of this
        * line are UI Buttons.
        */
        for(int i = 1; i < numColumns; i++) {
            if(i == 2) {
                paint.setStrokeWidth(5);
                canvas.drawLine(gridWidth*i, 0, gridWidth*i, screenHeight, paint);
                paint.setStrokeWidth(0);
            }
            else {
                canvas.drawLine(gridWidth*i, 0, gridWidth*i, screenHeight, paint);
            }
        }

        /*
        * Draw the grids horizontal lines. The last horizontal grid line drawn will be drawn
        * slightly thicker than the rest to indicate that the buttons below this line are UI
        * buttons.
        */
        for(int i = 1; i < numRows; i++) {
            if(i == numRows -1){
                canvas.drawLine(0,gridHeight*i, gridWidth * 2, gridHeight*i, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(gridWidth * 2,gridHeight*i, screenWidth, gridHeight*i, paint);
                paint.setStrokeWidth(0);
            }
            else{
                canvas.drawLine(0,gridHeight*i, screenWidth, gridHeight*i, paint);
            }
        }
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(gridHeight/4);
        drawUIButtons(canvas);
        drawSaveButtons(canvas);
        drawStatusIndicator(canvas);
    }

    /*
     * The bottom very bottom left grid square will be used as a status indicator for the running
     * state of a circuit. If the current circuit is running then the grid square will be colored
     * green. Otherwise, the grid square will remain red.
     */
    private void drawStatusIndicator(Canvas canvas){
        if(statusIndicator){
            paint.setColor(Color.GREEN);
        }
        else{
            paint.setColor(Color.RED);
        }
        canvas.drawRect(0,screenHeight-gridHeight, gridWidth,screenHeight,paint);
    }

    private void drawSaveButtons(Canvas canvas){
        int xLocation = (gridWidth - gridWidth/2) + (2*gridWidth);
        int yLocation = (4*gridHeight) + (gridHeight/2);

        canvas.drawText("Save",xLocation,yLocation,paint);
        xLocation += gridWidth;
        canvas.drawText("A", xLocation,yLocation,paint);
        xLocation += gridWidth;
        canvas.drawText("B", xLocation,yLocation,paint);
        xLocation += gridWidth;
        canvas.drawText("C", xLocation,yLocation,paint);
    }

    private void drawUIButtons(Canvas canvas) {
        //xLocation ensures that the text will be horizontally centered
        //within a specified grid square.
        int xLocation = gridWidth - gridWidth/2;
        int yLocation = gridHeight/2;

        /*
        * Create a rectangle around the Text Add in order to get the vertical height of the text.
        * The height will be used to vertically align all text that goes below the text "Add".
        */
        Rect bounds = new Rect();
        paint.getTextBounds("Add", 0, 3, bounds);
        int height = bounds.height();

        canvas.drawText("Add",xLocation,yLocation,paint);
        canvas.drawText("And-Gate",xLocation, yLocation + height, paint);
        yLocation += gridHeight;

        canvas.drawText("Add", xLocation, yLocation, paint);
        canvas.drawText("Not-Gate", xLocation, yLocation + height, paint);
        yLocation += gridHeight;

        canvas.drawText("Add",xLocation, yLocation, paint);
        canvas.drawText("Light Bulb", xLocation, yLocation + height, paint);
        yLocation += gridHeight;

        canvas.drawText("Move", xLocation, yLocation, paint);

        //Reset yLocation height to draw second column of button text.
        //Set up xLocation to be horizontally centered within a specific
        //grid square in the second column.
        yLocation = gridHeight / 2;
        xLocation += gridWidth;

        canvas.drawText("Add", xLocation, yLocation,paint);
        canvas.drawText("Or-Gate", xLocation, yLocation + height, paint);
        yLocation += gridHeight;

        canvas.drawText("Add", xLocation, yLocation, paint);
        canvas.drawText("Switch", xLocation, yLocation + height, paint);
        yLocation += gridHeight;

        canvas.drawText("Add", xLocation, yLocation, paint);
        canvas.drawText("Wire", xLocation, yLocation + height, paint);
        yLocation += gridHeight;

        canvas.drawText("Delete", xLocation, yLocation, paint);
        yLocation += gridHeight;

        paint.getTextBounds("Run/", 0, 4, bounds);
        height = bounds.height();
        canvas.drawText("Run/", xLocation, yLocation, paint);
        canvas.drawText("Pause", xLocation, yLocation + height, paint);
    }

    public void toggleStatusIndicator(){ statusIndicator = !statusIndicator; }

    public int getGridWidth() { return this.gridWidth; }

    public int getGridHeight() { return this.gridHeight; }
}
