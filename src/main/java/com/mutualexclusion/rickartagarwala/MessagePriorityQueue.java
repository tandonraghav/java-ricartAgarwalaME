package com.mutualexclusion.rickartagarwala;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessagePriorityQueue {

    @Autowired SystemSettings systemSettings;

    private Map<String,Message> messageMap=new ConcurrentHashMap<>();

//    private PriorityQueue<Message> messages=new PriorityQueue<>(new Comparator<Message>() {
//        @Override
//        public int compare(Message o1, Message o2) {
//            if(o1.getTimestamp()<o2.getTimestamp()) return -1;
//            if(o1.getTimestamp()==o2.getTimestamp()){
//                return o1.getPId().compareTo(o2.getPId());
//            }
//            return 0;
//        }
//    });

    public Map<String,Message> getPriorityMessageQueue(){
        return messageMap;
    }

    public boolean isCurrentMessageGreaterThan(Message m){
        Message currentNodeMsg=messageMap.get(systemSettings.getNodeId());

        if(currentNodeMsg!=null && currentNodeMsg.getTimestamp()>m.getTimestamp()) {
            System.out.println(currentNodeMsg.getPId()+" "+m.getPId());
            return true;
        }
        if(currentNodeMsg!=null && currentNodeMsg.getTimestamp()==m.getTimestamp()){
            System.out.println(currentNodeMsg.getPId()+" "+m.getPId());
            return currentNodeMsg.getPId().compareTo(m.getPId())>0;
        }
        return false;
    }

}
