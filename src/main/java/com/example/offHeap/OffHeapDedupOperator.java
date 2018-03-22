package com.example.offHeap;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bits.heap.OffHeapSet;

public class OffHeapDedupOperator extends BaseOperator {

    private static final int MAX = 50000000;

    private static final Logger LOG = LoggerFactory.getLogger(OffHeapDedupOperator.class);

    public final transient DefaultOutputPort<Integer> unique = new DefaultOutputPort<Integer>();

    public final transient DefaultOutputPort<Integer> duplicate = new DefaultOutputPort<Integer>();

    public final transient DefaultInputPort<Integer> input = new DefaultInputPort<Integer>() {
        @Override
        public void process(Integer tuple) {
            dedup(tuple);
        }
    };


    private OffHeapSet offHeapSet;

    public OffHeapDedupOperator() {}

    public void dedup(Integer key){
        if(key == 1)
            LOG.info("START 1 : " + System.currentTimeMillis());

        if(offHeapSet.add(key)){
            unique.emit(key);
        }
        else {
            duplicate.emit(key);
        }
        if(key == MAX) {
            LOG.info("DONE " + MAX + " : " + System.currentTimeMillis());
            throw new ShutdownException();
        }
    }


    @Override
    public void setup(Context.OperatorContext context)
    {
        offHeapSet = new OffHeapSet();
    }

    @Override
    public void endWindow() { LOG.info("data size : " + offHeapSet.size());}


}
