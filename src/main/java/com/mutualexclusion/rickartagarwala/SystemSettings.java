package com.mutualexclusion.rickartagarwala;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
public class SystemSettings {

    @Value("${nodeId}")
    private String nodeId;

    @Value("${tcpPort}")
    private Integer tcpPort;

    @Value("${pId}")
    private Integer pId;
}
