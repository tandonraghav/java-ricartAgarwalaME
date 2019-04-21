package com.mutualexclusion.rickartagarwala;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Processes {

    private Integer pId;
    private Integer tcpPort;
    private String nodeId;
    private String tcpHost;

    public Processes(){}

    public Processes(Integer pId, Integer tcpPort, String nodeId, String tcpHost) {
        this.pId = pId;
        this.tcpPort = tcpPort;
        this.nodeId = nodeId;
        this.tcpHost = tcpHost;
    }
}
