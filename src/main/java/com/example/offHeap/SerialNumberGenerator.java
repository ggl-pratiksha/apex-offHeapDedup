package com.example.offHeap;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;

import java.util.Random;

public class SerialNumberGenerator extends BaseOperator implements InputOperator
{
    private int numTuples = 5000;
    private transient int count = 0;
    private static final int MAX = 50000000;
    private int no;
    Random random = new Random();

    public final transient DefaultOutputPort<Integer> out = new DefaultOutputPort<Integer>();

    @Override
    public void beginWindow(long windowId)
    {
        count = 0;
    }

    @Override
    public void emitTuples()
    {
        if (count++ < numTuples && no <= MAX) {
            out.emit(no);
            no++;
        }
    }

    public int getNumTuples()
    {
        return numTuples;
    }

    /**
     * Sets the number of tuples to be emitted every window.
     * @param numTuples number of tuples
     */
    public void setNumTuples(int numTuples)
    {
        this.numTuples = numTuples;
    }

    @Override
    public void setup(Context.OperatorContext context) { no = 1; }
}
