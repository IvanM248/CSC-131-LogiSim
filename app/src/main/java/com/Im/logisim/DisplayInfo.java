package com.Im.logisim;

/*
   This class holds information related to the display on which the app is running on. I found it
   easier encapsulate this information as it is needed among some of the different classes that
   need to display to the device screen such as the Grid class and the Bitmap object. Because the
   entirety of the information contained in this class is not used by all objects that display to
   the device screen, I chose to keep it within it's own class.
 */
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;
public class DisplayInfo {

    private int numberHorizontalPixels;
    private int numberVerticalPixels;

    public DisplayInfo(WindowManager window) {
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        numberHorizontalPixels = size.x;
        numberVerticalPixels = size.y;
    }

    /*
    * Setter methods are not implemented in this class as the private fields in this class only
    * need to be set once on creation.
    */

    public int getScreenWidth() { return numberHorizontalPixels; }

    public int getScreenHeight() { return numberVerticalPixels; }
}