/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.converter;

import java.io.IOException;

/**
 *
 * @author andrewserff
 */
public abstract class ModelConverter<T> {
    private Class<T> myType;
    
    public ModelConverter(Class<T> type) {
        this.myType = type;
    }
    public abstract T getModelFromData(String data) throws IOException;

    /**
     * @return the myType
     */
    public Class<T> getMyType() {
        return myType;
    }

    /**
     * @param myType the myType to set
     */
    public void setMyType(Class<T> myType) {
        this.myType = myType;
    }
}
