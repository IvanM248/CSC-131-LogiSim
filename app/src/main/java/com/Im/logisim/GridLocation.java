package com.Im.logisim;
/*
 * A class that represents a grid location. This class is mainly used by the LogiSim class inorder
 * to store information about when a user wants to move an object to a new location, or information
 * about which UI button the user pressed.
 */
public class GridLocation {
    /*
     * These fields are made public as the information it holds is constantly changing throughout
     * its use. As these fields are not dependencies for other information of this class, I allowed
     * them to be public.
     */
    public int x;
    public int y;

    /*
     * I added constructors here in order to be able to initialize a GridLocation object upon its
     * creation.
     */
    public GridLocation(){ this(0,0); }

    public GridLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
}
