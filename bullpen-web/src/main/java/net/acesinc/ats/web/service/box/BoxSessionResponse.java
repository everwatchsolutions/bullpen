/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.service.box;

/**
 *
 * {
    'type': 'session',
    'id': '4fba9eda0dd745d491ad0b98e224aa25',
    'expires_at': '3915-10-29T01:31:48.677Z',
    'urls': {
        'view': 'https://view-api.box.com/1/sessions/4fba9eda0dd745d491ad0b98e224aa25/view',
        'assets': 'https://view-api.box.com/1/sessions/4fba9eda0dd745d491ad0b98e224aa25/assets/',
        'realtime': 'https://view-api.box.com/sse/4fba9eda0dd745d491ad0b98e224aa25'
    }
}
 * @author andrewserff
 */
public class BoxSessionResponse {
    private String type;
    private String id;
    private String expires_at;
    private BoxUrls urls;
    private Object document;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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

    /**
     * @return the expires_at
     */
    public String getExpires_at() {
        return expires_at;
    }

    /**
     * @param expires_at the expires_at to set
     */
    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    /**
     * @return the urls
     */
    public BoxUrls getUrls() {
        return urls;
    }

    /**
     * @param urls the urls to set
     */
    public void setUrls(BoxUrls urls) {
        this.urls = urls;
    }

    /**
     * @return the document
     */
    public Object getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Object document) {
        this.document = document;
    }

}
