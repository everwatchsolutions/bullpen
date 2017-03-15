/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.request;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author andrewserff
 */
public class ExternalRequestContextEnumConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(ExternalRequestContext.valueOf(text.trim().toUpperCase()));
    }

}
