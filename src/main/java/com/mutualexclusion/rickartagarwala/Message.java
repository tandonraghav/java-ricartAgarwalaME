package com.mutualexclusion.rickartagarwala;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Message{

    private Integer pId;
    private long timestamp;
    private String resourceName;
    private String nodeId;
    private Operation operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return getTimestamp() == message.getTimestamp() &&
                Objects.equals(pId, message.pId) &&
                getResourceName().equalsIgnoreCase(message.getResourceName()) &&
                Objects.equals(getNodeId(), message.getNodeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pId, getTimestamp(), getResourceName(), getNodeId());
    }

    public String createMessage(){
        return this.operation.name()+"&"+this.getPId()+"&"+this.getTimestamp()+"&"+this.getNodeId()+"&"+this.getResourceName();
    }

}
