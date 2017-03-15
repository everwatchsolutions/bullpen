/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.tld;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import net.acesinc.ats.model.candidate.CandidateHistory;
import net.acesinc.ats.model.common.ValueHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class HistoryDetailsPopover extends SimpleTagSupport {

    private static final Logger log = LoggerFactory.getLogger(HistoryDetailsPopover.class);
    private CandidateHistory history;
    private String fieldPrefix;
    private String uniqueId;
    private String popupLocation;
    private String title;
    private String popoverType;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        StringBuilder sb = new StringBuilder();
        String type = popoverType;
        if (type == null || popoverType.isEmpty()) {
            type = "popover";
        }
        sb.append("data-toggle=\"").append(type).append("\" ");
        sb.append("data-container=\"body\" ");
        String pos = popupLocation;
        if (pos == null || pos.isEmpty()) {
            pos = "top";
        }
        sb.append("data-placement=\"").append(pos).append("\" ");

        String fixedId = "";
        if (uniqueId != null) {
            fixedId = uniqueId.replaceAll("\\.", "_").replaceAll("/", "_").replaceAll(" ", "-");
        }
        String propId = fieldPrefix + fixedId;

        String sourceDesc = "Source: ";
        if (history != null) {
            ValueHistory hist = history.getMostRecentHistory(propId);
            if (hist != null) {
                sourceDesc += hist.getSource() + " ( " + hist.getSourceId() + " )";
                sourceDesc += " - " + hist.getDateModified();
//                switch (hist.getSource()) {
//                    case AUTOMATIC:
//                        
//                        break;
//                    case MANUAL:
//                        sb.append("class=\"fa fa-fw fa-info-circle source-automatic\"");
//                        break;
//                }
                sb.append("class=\"fa fa-fw fa-info-circle source-").append(hist.getSource().toString().toLowerCase()).append("\"");
            } else {
                log.debug("No history found for property [ " + propId + " ]");
                sourceDesc += "Unknown";
            }
        } else {
            sourceDesc += "No History Available";
        }

        sb.append("data-content=\"").append(sourceDesc).append("\" ");
        if ("tooltip".equals(type)) {
            sb.append("title=\"").append(sourceDesc).append("\" ");
        } else {
            String t = title;
            if (t == null || t.isEmpty()) {
                t = "Where this data came from";
            }
            sb.append("title=\"").append(t).append("\" ");
        }

        out.println(sb.toString());
    }

    /**
     * @return the history
     */
    public CandidateHistory getHistory() {
        return history;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(CandidateHistory history) {
        this.history = history;
    }

    /**
     * @return the fieldPrefix
     */
    public String getFieldPrefix() {
        return fieldPrefix;
    }

    /**
     * @param fieldPrefix the fieldPrefix to set
     */
    public void setFieldPrefix(String fieldPrefix) {
        this.fieldPrefix = fieldPrefix;
    }

    /**
     * @return the uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return the popupLocation
     */
    public String getPopupLocation() {
        return popupLocation;
    }

    /**
     * @param popupLocation the popupLocation to set
     */
    public void setPopupLocation(String popupLocation) {
        this.popupLocation = popupLocation;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the popoverType
     */
    public String getPopoverType() {
        return popoverType;
    }

    /**
     * @param popoverType the popoverType to set
     */
    public void setPopoverType(String popoverType) {
        this.popoverType = popoverType;
    }

}
