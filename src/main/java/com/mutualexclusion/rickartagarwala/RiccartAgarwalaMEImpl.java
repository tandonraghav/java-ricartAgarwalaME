package com.mutualexclusion.rickartagarwala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RiccartAgarwalaMEImpl implements RiccartAgarwalaME{
    Logger logger = LoggerFactory.getLogger(RiccartAgarwalaMEImpl.class);

    @Autowired private ClusterOps clusterOps;
    @Autowired private MessagePriorityQueue messagePriorityQueue;
    @Autowired private SystemSettings systemSettings;

    boolean csReqd=false;
    boolean isCSExecuting=false;

    /*
    1. Send request to All processess
    2. If everyone says OK, access the resource, else wait
    */
    @Override
    public synchronized void request(Message message, String nodeId) {
        logger.info("Going to request for CS ....");
        csReqd=true;
        if(!messagePriorityQueue.getPriorityMessageQueue().containsKey(systemSettings.getNodeId())){
            messagePriorityQueue.getPriorityMessageQueue().put(systemSettings.getNodeId(),message);
        }
        clusterOps.sendReqToAll(message);
    }

    @Override
    public void reply(Message message, String nodeId) {
        //logger.info("Handling REQUEST "+csReqd+" "+isCSExecuting);
        if(isCSExecuting) {
            logger.info("Currently executing CS. Adding to deferred Queue!");
            clusterOps.addToDeferredRequests(message);
            return;
        }
        if(csReqd){
           boolean isGreater = messagePriorityQueue.isCurrentMessageGreaterThan(message);
           if(isGreater){
               logger.info("Timestamp is Lesser. So sending OK!");
               //Reply back OK.
               clusterOps.sendOK(message.getNodeId());
           }else{
               logger.info("Timestamp is Greater. So adding to deffered requests!");
               //Add msg to deferred req.
               clusterOps.addToDeferredRequests(message);
           }
        }else{
            logger.info("CS Not reqd. sending OK!");
            //Send OK to nodeID
            clusterOps.sendOK(message.getNodeId());
        }
    }

    @Override
    public void release() {
        logger.info("Message released");
    }

    @Override
    public synchronized void executeCS(Function<String,String> fn) {
        try{
            while(true){
                //logger.info("In execute CS...");
                if(!csReqd) return;
                boolean canExecute=clusterOps.canExecuteCS();
                if(canExecute){
                    //call fn() in real case
                    isCSExecuting=true;
                    try {
                        logger.info("Executing CS....");
                        Thread.sleep(2000);
                        logger.info("Executing CS Done!!! ....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

        }finally{
            //send release CS.
            clusterOps.sendReleaseMsg();
            clusterOps.reset();
            isCSExecuting=false;
            csReqd=false;
        }
    }

    public void handleOKMsg(Message message){
        clusterOps.handleOK(message.getNodeId());
    }
}
