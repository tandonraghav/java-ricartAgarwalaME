package com.mutualexclusion.controller;

import com.mutualexclusion.rickartagarwala.Message;
import com.mutualexclusion.rickartagarwala.RiccartAgarwalaME;
import com.mutualexclusion.rickartagarwala.SystemSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PocRestController {

    @Autowired
    RiccartAgarwalaME riccartAgarwalaME;
    @Autowired
    SystemSettings systemSettings;

    @GetMapping(path = "/test")
    public String test(){



        riccartAgarwalaME.request(createMessage(),systemSettings.getNodeId());
        riccartAgarwalaME.executeCS(null);

        return "";
    }

    private Message createMessage(){
        Message m=new Message();
        m.setResourceName("test");
        m.setNodeId(systemSettings.getNodeId());
        m.setTimestamp(System.currentTimeMillis());
        m.setPId(systemSettings.getPId());
        return m;
    }
}
