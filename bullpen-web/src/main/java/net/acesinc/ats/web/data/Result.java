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
public class Result {

    private boolean error;
    private String message;
    private Object data;

    public static Result ok(Object data) {
        return ok(data, "Success");
    }
    public static Result ok(Object data, String message) {
        Result r = new Result();
        r.setData(data);
        r.setError(false);
        r.setMessage(message);
        return r;
    }
    public static Result error(Object data) {
        return error(data, "Error");
    }
    public static Result error(Object data, String message) {
        Result r = new Result();
        r.setData(data);
        r.setError(true);
        r.setMessage(message);
        return r;
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
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

}
