package com.mutualexclusion.rickartagarwala;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClusterOps {

    @Autowired private MessagePriorityQueue messagePriorityQueue;
    @Autowired private TCPConnection tcpConnection;
    @Autowired private SystemSettings systemSettings;

    private Map<String,Processes> processes=new HashMap<>();
    private Map<String,Integer> recievedSignal=new HashMap<>();

    @PostConstruct
    public void init(){
        //Processes process=new Processes(1234,systemSettings.getTcpPort(),systemSettings.getNodeId(),"localhost");
        if(systemSettings.getNodeId().equalsIgnoreCase("node1")){
            Processes p2=new Processes(1235,6667,"node2","localhost");
            //Processes p3=new Processes(1236,6668,"node3","localhost");
            this.processes.put("node2",p2);
            //this.processes.put("node3",p3);
        }

        if(systemSettings.getNodeId().equalsIgnoreCase("node2")){
            Processes p2=new Processes(1234,6666,"node1","localhost");
            //Processes p3=new Processes(1236,6668,"node3","localhost");
            this.processes.put("node1",p2);
            //this.processes.put("node3",p3);
        }

    }

    public void sendReqToAll(Message message){
        message.setOperation(Operation.REQUEST);
        String msg=message.createMessage();

        for(Map.Entry<String,Processes> p:processes.entrySet()){
            try{
                System.out.println("Sending msg = "+msg+" to node = "+p.getValue().getTcpPort());
                tcpConnection.send(msg,p.getValue().getTcpPort());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendReleaseMsg(){
        for(Map.Entry<String,Message> p:messagePriorityQueue.getPriorityMessageQueue().entrySet()){
            try{
                sendOK(p.getKey());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean canExecuteCS(){
        for(Map.Entry<String,Processes> key:processes.entrySet()){
            String nodeVal=key.getKey();
            if(recievedSignal.get(nodeVal)!=null && recievedSignal.get(nodeVal).equals(1)){
                return true;
            }
        }
        return false;
    }

    public void reset(){
        recievedSignal.clear();
    }

    public void addToDeferredRequests(Message message){
        Map<String,Message> messageMap=messagePriorityQueue.getPriorityMessageQueue();
        if(!messageMap.containsKey(message.getNodeId())){
            messageMap.put(message.getNodeId(),message);
        }
    }

    public void sendOK(String nodeId){
        try{
            String msg=Operation.OK.name()+"&"+systemSettings.getNodeId();
            tcpConnection.send(msg,processes.get(nodeId).getTcpPort());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleOK(String nodeId){
        System.out.println("Handling ok for node "+nodeId);
        recievedSignal.put(nodeId,1);
    }

    public void addProcess(Processes p){
        this.processes.put(p.getNodeId(),p);
    }
}
