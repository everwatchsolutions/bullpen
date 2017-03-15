/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.data;

/**
 *
 * @author andrewserff
 */
public class Notification<T> {
    /**
     * name is the Notification queue name context.  Be mindful of what you set as the name!
     */
    private String name;
    private long timestamp;
    private T payload;

    public Notification(String name) {
        this.name = name;
        timestamp = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", payload=" + payload +
                '}';
    }
    
}
