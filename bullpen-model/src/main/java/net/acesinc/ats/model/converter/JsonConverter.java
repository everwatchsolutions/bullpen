/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import net.acesinc.ats.model.candidate.Candidate;

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
public class JsonConverter<T> extends ModelConverter {
  
    public JsonConverter(Class type) {
        super(type);
    }
    public T getModelFromData(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(data, getMyType());
    }
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/3.3/Recruiting-JSON/Instances/UC003B_ShowCandidate.json";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/ResumeExample-Standard-HRXML-25.xml";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/HireAbilitySample.xml";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/AndrewSerffResume-ACES.pdf-ORP.xml";
//        String filename = "/Users/andrewserff/Documents/Project Documents/Applicant Tracking/HR-XML Standards/ParserSamples/AndrewSerffResume-Daxtra-hrxml.xml";

        
        try {
            
            String data = FileUtils.readFileToString(new File(filename));
            ModelConverter<Candidate> modelConverter = new JsonConverter<Candidate>(Candidate.class);
            
//            CandidateModelConverter converter = CandidateModelConverterFactory.getModelConverterForData(data);
//            if (converter instanceof BaseXMLConverter) {
////                log.info("Data read from file was: " + data);
//                data = preconditionXMLData(data);
////                log.info("Escaped data is now: " + data);
//            }
            Candidate c = modelConverter.getModelFromData(data);
//            Candidate c = converter.getCandidateFromData(data);
            if (c == null) {
               // log.error("Candidate is null...didn't convert");
            } else {
               // log.info("We converted the Candidate " + c.getFormattedName() + "!");
//                log.info("Candidate: " + ToStringBuilder.reflectionToString(c, ToStringStyle.MULTI_LINE_STYLE));
                //log.info("Candidate: " + ToStringBuilder.reflectionToString(c, new RecursiveToStringStyle()));
            }
        } catch (IOException ex) {
            
        }
        
    }
}
