/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.acesinc.ats.model.common.ValueHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class CandidateHistory {
    private static final Logger log = LoggerFactory.getLogger(CandidateHistory.class);
    
    private String id;
    private String candidateId;
    private Map<String, List<ValueHistory>> propertyHistoryMap;

    public CandidateHistory() {
        this.propertyHistoryMap = new HashMap<>();
    }

    
    private List<ValueHistory> getHistoryList(String propertyName) {
        if (propertyHistoryMap == null) {
            propertyHistoryMap = new HashMap<>();
        }
        List<ValueHistory> list = propertyHistoryMap.get(propertyName);
        if (list == null) {
            list = new ArrayList<>();
            propertyHistoryMap.put(propertyName, list);
        }
        return list;
    }
    public void addHistories(String propertyName, List<ValueHistory> histories) {
        List<ValueHistory> list = getHistoryList(propertyName);
        list.addAll(histories);
    }
    
    public void addHistory(String propertyName, ValueHistory history) {
        List<ValueHistory> list = getHistoryList(propertyName);
        list.add(history);
    }
    
    public ValueHistory getMostRecentHistory(String propertyName) {
        ValueHistory mostRecent = null;
        List<ValueHistory> list = propertyHistoryMap.get(propertyName);
        if (list != null) {
            for (ValueHistory h : list) {
                if (mostRecent == null || (h.getDateModified().after(mostRecent.getDateModified()))) {
                    mostRecent = h;
                } 
            }
        } else {
            log.debug("No history for property [ " + propertyName + " ]");
        }
        return mostRecent;
    }
    
    /**
     * @return the candidateId
     */
    public String getCandidateId() {
        return candidateId;
    }

    /**
     * @param candidateId the candidateId to set
     */
    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    /**
     * @return the propertyHistoryMap
     */
    public Map<String, List<ValueHistory>> getPropertyHistoryMap() {
        return propertyHistoryMap;
    }

    /**
     * @param propertyHistoryMap the propertyHistoryMap to set
     */
    public void setPropertyHistoryMap(Map<String, List<ValueHistory>> propertyHistoryMap) {
        this.propertyHistoryMap = propertyHistoryMap;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
