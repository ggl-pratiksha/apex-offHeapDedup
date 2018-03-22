/**
 * Put your copyright and license info here.
 */
package com.example.offHeap;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.lib.io.ConsoleOutputOperator;

@ApplicationAnnotation(name="OffheapDedup")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    /*RandomNumberGenerator randomGenerator = dag.addOperator("randomGenerator", RandomNumberGenerator.class);
    randomGenerator.setNumTuples(500);*/

    SerialNumberGenerator serialNumberGenerator = dag.addOperator("SerialNumberGenerator", SerialNumberGenerator.class);
    OffHeapDedupOperator offHeapDedupOperator = dag.addOperator("OffHeapDedupOperator", new OffHeapDedupOperator());
    ConsoleOutputOperator uniqueconsole = dag.addOperator("uniqueconsole", new ConsoleOutputOperator());
    ConsoleOutputOperator duplicateConsole = dag.addOperator("duplicateConsole", new ConsoleOutputOperator());

    dag.addStream("SerialNumberGenerator", serialNumberGenerator.out, offHeapDedupOperator.input);
    dag.addStream("uniqueData", offHeapDedupOperator.unique, uniqueconsole.input);
    dag.addStream("duplicateConsole", offHeapDedupOperator.duplicate, duplicateConsole.input);
  }
}
