package com.mutualexclusion.rickartagarwala;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RiccartAgarwalaMEImpl implements RiccartAgarwalaME{

    @Autowired private ClusterOps clusterOps;
    @Autowired private MessagePriorityQueue messagePriorityQueue;

    boolean csReqd=false;
    boolean isCSExecuting=false;

    /*
    1. Send request to All processess
    2. If everyone says OK, access the resource, else wait
    */
    @Override
    public synchronized void request(Message message, String nodeId) {
        System.out.println("Going to request for CS ....");
        //if(!csReqd) return;
        csReqd=true;
        clusterOps.sendReqToAll(message);
    }

    @Override
    public synchronized void reply(Message message, String nodeId) {
        if(isCSExecuting) {
            System.out.println("Currently executing CS. Adding to deferred Queue!");
            clusterOps.addToDeferredRequests(message);
            return;
        }
        if(csReqd){
           boolean isGreater = messagePriorityQueue.isCurrentMessageGreaterThan(message);
           if(isGreater){
               System.out.println("Timestamp is Lesser. So sending OK!");
               //Reply back OK.
               clusterOps.sendOK(message.getNodeId());
           }else{
               System.out.println("Timestamp is Greater. So sending OK!");
               //Add msg to deferred req.
               clusterOps.addToDeferredRequests(message);
           }
        }else{
            System.out.println("CS Not reqd. sending OK!");
            //Send OK to nodeID
            clusterOps.sendOK(message.getNodeId());
        }
    }

    @Override
    public void release() {
        System.out.println("Message released");
    }

    @Override
    public synchronized void executeCS(Function<String,String> fn) {
        try{
            while(true){
                if(!csReqd) return;
                boolean canExecute=clusterOps.canExecuteCS();
                if(canExecute){
                    //call fn() in real case
                    isCSExecuting=true;
                    try {
                        System.out.println("Executing CS....");
                        Thread.sleep(2000);
                        System.out.println("Executing CS Done!!! ....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

        }finally{
            //send release CS.
            clusterOps.sendReleaseMsg();
            System.out.println("Send RELEASE Msg ...");
            clusterOps.reset();
            isCSExecuting=false;
            csReqd=false;
        }
    }

    public void handleOKMsg(Message message){
        clusterOps.handleOK(message.getNodeId());
    }
}
