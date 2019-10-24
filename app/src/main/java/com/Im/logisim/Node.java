package com.Im.logisim;

import android.graphics.Canvas;
/*
* An interface which will be implemented by the switch class and all logic gate classes. This
* interface is necessary for evaluating the result of logic circuits created by the user. The
* interface also makes it much easier to group all logic gates together. The idea here for
* using an interface to evaluate logic circuits is taken from Dr.Posnett's LogiSimEvaluationExample
* code.
*/
public interface Node {
    void draw(Canvas canvas, int gridWidth, int gridHeight);
    void setColumn(int column);
    void setRow(int row);
    int getRow();
    int getColumn();
    boolean eval();
}
