/**
 * Put your copyright and license info here.
 */
package com.example.offHeap;

import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;

import java.util.Random;

/**
 * This is a simple operator that emits random number.
 */
public class RandomNumberGenerator extends BaseOperator implements InputOperator
{
  private int numTuples = 100;
  private transient int count = 0;
  private static final int MIN = 0;
  private static final int MAX = 1000;
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
    if (count++ < numTuples) {
      out.emit(positive(getRandomInt()));
    }
  }

  private Integer positive(Integer key) {
      return (key < 0 ? (-key) : key);
  }

  private int getRandomInt() {
    return (int)Math.floor(Math.random() * (MAX - MIN)) + MIN; //The maximum is exclusive and the minimum is inclusive
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
}
