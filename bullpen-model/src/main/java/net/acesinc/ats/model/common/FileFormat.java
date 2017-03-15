/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import org.springframework.http.MediaType;

/**
 *
 * @author andrewserff
 */
public enum FileFormat {
    DOC,
    DOCX,
    PDF,
    TXT,
    RTF,
    HRXML,
    JPEG,
    PNG,
    GIF,
    UNKNOWN;
    
    public static FileFormat fromMimeType(String mimeType) throws IllegalArgumentException {
        MediaType t = MediaType.parseMediaType(mimeType);
        if (MediaType.IMAGE_GIF.equals(t)) {
            return GIF;
        } else if (MediaType.IMAGE_JPEG.equals(t)) {
            return JPEG;
        } else if (MediaType.IMAGE_PNG.equals(t)) {
            return PNG;
        } else {
            throw new IllegalArgumentException("Unknown mimetype [ " + mimeType + " ]");
        }
    }
}
