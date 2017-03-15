/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.candidate.CandidateHistory;
import net.acesinc.ats.model.common.IdentifibleObject;
import net.acesinc.ats.model.common.ValueHistory;
import net.acesinc.ats.model.common.ValueSource;
import net.acesinc.ats.model.common.ValueState;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 *
 * @author andrewserff
 */
public class CandidateReflectionHelper implements FieldCallback {

    private static final Logger log = LoggerFactory.getLogger(CandidateReflectionHelper.class);

    private Candidate oldCandidate;
    private Candidate newCandidate;
    private CandidateHistory hist;
    private ValueSource source;
    private String sourceId;

    public CandidateReflectionHelper(Candidate oldCandidate, Candidate newCandidate, CandidateHistory hist, ValueSource source, String sourceId) {
        this.oldCandidate = oldCandidate;
        this.newCandidate = newCandidate;
        this.hist = hist;
        this.source = source;
        this.sourceId = sourceId;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        String fieldName = field.getName();

        if ("id".equals(fieldName) || "log".equals(fieldName)) {
            return;
        }
        
        field.setAccessible(true);
        Object newVal = null;
        if (newCandidate != null) {
            newVal = field.get(newCandidate);
        }

        Object oldVal = null;
        if (oldCandidate != null) {
            oldVal = field.get(oldCandidate);
        }

        if (field.getType().isAssignableFrom(List.class)) {
            List newList = (List) newVal;
            List oldList = (List) oldVal;
            if (newList != null) {
                for (Object obj : newList) {
                    IdentifibleObject identObj = null;
                    if (obj instanceof IdentifibleObject) {
                        identObj = (IdentifibleObject) obj;
                    }
                    int index = newList.indexOf(obj);
                    ValueHistory vh = getDefaultValueHistory(oldCandidate, newCandidate, source, sourceId);
                    if (oldCandidate == null || oldList == null || !oldList.contains(obj)) {
                        vh.setState(ValueState.ADDED);
                        vh.setOldValue(null);
                        vh.setNewValue(obj);
                    } 
                    /*else if (oldList != null && !EqualsBuilder.reflectionEquals(obj, oldList.get(index), true)) {
                     vh.setState(ValueState.CHANGED);
                     vh.setOldValue(oldList.get(index));
                     vh.setNewValue(obj);
                     }*/

                    if (vh.getState() != null) {
                        if (identObj != null) {
                            String id = identObj.getUniqueId();
                            id = id.replaceAll(" ", "-");
                            id = id.replaceAll("/", "_");
                            id = id.replaceAll("\\.", "_");
                            log.debug("Using key " + id);
                            hist.addHistory("/" + fieldName + "/" + id, vh);
                        } else {
                            hist.addHistory("/" + fieldName + "[" + index + "]", vh);
                        }
                    }
                }
                if (oldList != null) {
                    for (Object oldObj : oldList) {
                        int index = oldList.indexOf(oldObj);
                        if (!newList.contains(oldObj)) {
                            ValueHistory vh = getDefaultValueHistory(oldCandidate, newCandidate, source, sourceId);
                            vh.setState(ValueState.DELETED);
                            vh.setOldValue(oldObj);
                            vh.setNewValue(null);

                            IdentifibleObject identObj = null;
                            if (oldObj instanceof IdentifibleObject) {
                                identObj = (IdentifibleObject) oldObj;
                            }
                            
                            if (identObj != null) {
                                String id = identObj.getUniqueId();
                                id = id.replaceAll(" ", "-");
                                id = id.replaceAll("/", "_");
                                id = id.replaceAll("\\.", "_");
                                log.debug("Using key " + id);
                                hist.addHistory("/" + fieldName + "/" + id, vh);
                            } else {
                                hist.addHistory("/" + fieldName + "[" + index + "]", vh);
                            }
                        }
                    }
                }
            } else if (oldCandidate != null && oldVal != null) {
                //they all got deleted
                for (Object obj : oldList) {
                    int index = oldList.indexOf(obj);
                    ValueHistory vh = getDefaultValueHistory(oldCandidate, newCandidate, source, sourceId);
                    vh.setState(ValueState.DELETED);
                    vh.setOldValue(obj);
                    vh.setNewValue(null);
                    hist.addHistory("/" + fieldName + "[" + index + "]", vh);
                }
            }
        } else {
            if (newVal != null) {
                ValueHistory vh = getDefaultValueHistory(oldCandidate, newCandidate, source, sourceId);
                if (oldCandidate == null) {
                    vh.setState(ValueState.ADDED);
                    vh.setOldValue(null);
                    vh.setNewValue(newVal);
                } else if (!EqualsBuilder.reflectionEquals(newVal, oldVal, true)) {
                    vh.setState(ValueState.CHANGED);
                    vh.setOldValue(oldVal);
                    vh.setNewValue(newVal);
                }
                if (vh.getState() != null) {
                    hist.addHistory("/" + fieldName, vh);
                }
            } else if (oldCandidate != null && oldVal != null) {
                //they all got deleted
                ValueHistory vh = getDefaultValueHistory(oldCandidate, newCandidate, source, sourceId);
                vh.setState(ValueState.DELETED);
                vh.setOldValue(oldVal);
                vh.setNewValue(null);
                hist.addHistory("/" + fieldName, vh);
            }
        }
    }

    private ValueHistory getDefaultValueHistory(Candidate origCandidate, Candidate newCandidate, ValueSource source, String sourceId) {
        ValueHistory vh = new ValueHistory();
        vh.setDateModified(new Date());
        vh.setSource(source);
        vh.setSourceId(sourceId);
        return vh;
    }

}
