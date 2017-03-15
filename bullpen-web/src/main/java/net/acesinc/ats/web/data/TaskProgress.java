/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrewserff
 */
public class TaskProgress {
    private String taskId;
    private String taskName;
    private int percentComplete;
    private String status;
    private boolean complete;
    private boolean error;
    private Map<String, String> taskDetail;
    
    @Override
    public String toString() {
        return "TaskProgress{ percentComplete: " + percentComplete + ", status: " + status + ", complete: " + complete + "}";
    }

    public void addTaskDetail(String key, String value) {
        if (taskDetail == null) {
            taskDetail = new HashMap<>();
        }
        taskDetail.put(key, value);
    }
    
    /**
     * @return the percentComplete
     */
    public int getPercentComplete() {
        return percentComplete;
    }

    /**
     * @param percentComplete the percentComplete to set
     */
    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @param complete the complete to set
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the taskDetail
     */
    public Map<String, String> getTaskDetail() {
        return taskDetail;
    }

    /**
     * @param taskDetail the taskDetail to set
     */
    public void setTaskDetail(Map<String, String> taskDetail) {
        this.taskDetail = taskDetail;
    }
    
}
