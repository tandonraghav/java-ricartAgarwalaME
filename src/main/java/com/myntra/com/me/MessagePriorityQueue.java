package com.myntra.com.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Component
public class MessagePriorityQueue {

    @Autowired SystemSettings systemSettings;

    private Map<String,Message> messageMap=new HashMap<>();

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

        if(currentNodeMsg.getTimestamp()>m.getTimestamp()) return true;
        return false;
    }

}
