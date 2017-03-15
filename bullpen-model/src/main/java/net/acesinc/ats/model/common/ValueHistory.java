/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.common;

import java.util.Date;

/**
 *
 * @author andrewserff
 */
public class ValueHistory {
    private ValueState state;
    private ValueSource source;
    private String sourceId;
    private Date dateModified;
    private Object oldValue;
    private Object newValue;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("State: ").append(state).append("\n");
        sb.append("Old Value: ").append(oldValue).append("\n");
        sb.append("New Value: ").append(newValue).append("\n");
        sb.append("Date Mod: ").append(dateModified).append("\n");
        sb.append("Source: ").append(source).append("\n");
        sb.append("SourceId: ").append(sourceId);
        
        return sb.toString();
    }

    
    /**
     * @return the source
     */
    public ValueSource getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(ValueSource source) {
        this.source = source;
    }

    /**
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return the dateModified
     */
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * @param dateModified the dateModified to set
     */
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * @return the oldValue
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue the oldValue to set
     */
    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * @return the newValue
     */
    public Object getNewValue() {
        return newValue;
    }

    /**
     * @param newValue the newValue to set
     */
    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    /**
     * @return the state
     */
    public ValueState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(ValueState state) {
        this.state = state;
    }
}
