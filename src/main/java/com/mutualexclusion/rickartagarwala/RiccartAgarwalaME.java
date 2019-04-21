package com.mutualexclusion.rickartagarwala;

import java.util.function.Function;

public interface RiccartAgarwalaME {

    void request(Message message,String nodeId);

    void reply(Message message,String nodeId);

    void release();

    void executeCS(Function<String,String> fn);

    public void handleOKMsg(Message message);
}
