/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.converter.candidate;

import java.io.File;
import java.io.IOException;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.converter.JsonConverter;
import net.acesinc.ats.model.converter.ModelConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class CandidateModelConverterFactory {
    private static final Logger log = LoggerFactory.getLogger(CandidateModelConverterFactory.class);
    
    public static ModelConverter getModelConverterForData(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        if (data.startsWith("{")) {
            //this is json data
            return new JsonConverter<Candidate>(Candidate.class);
        } else if (data.startsWith("<")) {
            
//            //starting to look like XML
            if (data.contains("ITCONS e-Solutions")) {
                //This is HR-XML from the OnlineResumeParser
                return new OnlineResumeParserCandidateConverter();
            } else if (data.contains("DAXTRA")) {
                //This is HR-XML from the Daxtra Parser. 
                return new DaxtraCandidateConverter();
            } else if (data.contains("StructuredXMLResume")) {
                //Assume this is standard HR-XML 2.5
                return new HRXML25CandidateConverter();
            } else {
                return null; //i give up...
            }
            
        } else {
            return null; //cause we don't know how to parse this
        }
    }
    
    public static String preconditionXMLData(String data) {
        return data.replaceAll("&(?![#a-zA-Z0-9]+;)", "&amp;");
    }
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/3.3/Recruiting-JSON/Instances/UC003B_ShowCandidate.json";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/ResumeExample-Standard-HRXML-25.xml";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/HireAbilitySample.xml";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/AndrewSerffResume-ACES.pdf-ORP.xml";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/AndrewSerffResume-Daxtra-hrxml.xml";
        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/AndrewSerffResume-ACES.pdf-rchilli.xml";

        
        try {
            
            String data = FileUtils.readFileToString(new File(filename));
            ModelConverter<Candidate> converter = CandidateModelConverterFactory.getModelConverterForData(data);
//            if (converter instanceof BaseXMLConverter) {
////                log.info("Data read from file was: " + data);
//                data = preconditionXMLData(data);
////                log.info("Escaped data is now: " + data);
//            }
            Candidate c = converter.getModelFromData(data);
            if (c == null) {
                log.error("Candidate is null...didn't convert");
            } else {
                log.info("We converted the Candidate " + c.getFormattedName() + "!");
//                log.info("Candidate: " + ToStringBuilder.reflectionToString(c, ToStringStyle.MULTI_LINE_STYLE));
                log.info("Candidate: " + ToStringBuilder.reflectionToString(c, new RecursiveToStringStyle()));
            }
        } catch (IOException ex) {
            log.error("Error converting file [ " + filename + " ] to Candidate", ex);
        }
        
    }
}
