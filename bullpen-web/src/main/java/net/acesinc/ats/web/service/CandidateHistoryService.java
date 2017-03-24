/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.candidate.CandidateHistory;
import net.acesinc.ats.model.common.ContactType;
import net.acesinc.ats.model.common.Email;
import net.acesinc.ats.model.common.Phone;
import net.acesinc.ats.model.common.PreferenceType;
import net.acesinc.ats.model.common.ValueHistory;
import net.acesinc.ats.model.common.ValueSource;
import net.acesinc.ats.model.converter.ModelConverter;
import net.acesinc.ats.model.converter.candidate.CandidateModelConverterFactory;
import net.acesinc.ats.web.repository.CandidateHistoryRepository;
import net.acesinc.ats.web.repository.CandidateRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author andrewserff
 */
@Service
public class CandidateHistoryService {

    private static final Logger log = LoggerFactory.getLogger(CandidateHistoryService.class);

    @Autowired
    private CandidateRepository candidateRepo;
    @Autowired
    private CandidateHistoryRepository candidateHistRepo;
    
    public CandidateHistory diffCandidates(Candidate origCandidate, Candidate newCandidate, ValueSource source) {
        return diffCandidates(origCandidate, newCandidate, source, null);
    }

    public CandidateHistory diffCandidates(Candidate origCandidate, Candidate newCandidate, ValueSource source, String sourceId) {
        CandidateHistory hist = new CandidateHistory();
        if (origCandidate != null) {
            hist.setCandidateId(origCandidate.getId());
        } else {
            hist.setCandidateId(newCandidate.getId());
        }

        ReflectionUtils.doWithFields(Candidate.class, new CandidateReflectionHelper(origCandidate, newCandidate, hist, source, sourceId));

        return hist;
    }

    public void updateCandidateHistory(Candidate newCandidateInfo, ValueSource source, String sourceId) {
        Candidate dbCandidate = null;
        if (newCandidateInfo.getId() != null) {
            dbCandidate = candidateRepo.findOne(newCandidateInfo.getId());
        } else {
            log.debug("Candidate has no id, Candidate History will be invalid");
            return;
        }
        updateCandidateHistory(dbCandidate, newCandidateInfo, source, sourceId);
    }
    public void updateCandidateHistory(Candidate oldCandidateInfo, Candidate newCandidateInfo, ValueSource source, String sourceId) {
        CandidateHistory dbHistory = null;
        if (newCandidateInfo.getId() != null) {
            dbHistory = candidateHistRepo.findByCandidateId(newCandidateInfo.getId());
        } else {
            log.debug("Candidate has no id, Candidate History will be invalid");
            return;
        }
        
        CandidateHistory history = diffCandidates(oldCandidateInfo, newCandidateInfo, source, sourceId);
        
        if (dbHistory != null) {
            log.debug("Updating existing CandidateHistory with id [ " + dbHistory.getId() + " ]");
            //we already have history for this candidate, so we need to add new history
            for (String key : history.getPropertyHistoryMap().keySet()) {
                List<ValueHistory> hists = history.getPropertyHistoryMap().get(key);
                if (hists != null && !hists.isEmpty()) {
                    dbHistory.addHistories(key, hists);
                }
            }
        } else { 
            log.debug("No existing CandidateHistory could be found.  Makeing a new one");
            dbHistory = history;
        }
        
        candidateHistRepo.save(dbHistory);
    }
    
    public static void main(String[] args) {
//        ConsoleAppender console = new ConsoleAppender();
//        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
//        console.setLayout(new PatternLayout(PATTERN));
//        console.setThreshold(Level.DEBUG);
//        console.activateOptions();
//        BasicConfigurator.configure(console);
//
////                String resumeFile = "/Users/andrewserff/Downloads/RandomResume.pdf";
//        String resumeFile = "/Users/andrewserff/Dropbox/Documents/Work/Resume/AndrewSerffResume-ACES.pdf";
////        String resumeFile = "/Users/andrewserff/Desktop/Test Resumes/Benjamin R Fancher_CSA_GA.docx";
////        String resumeFile = "/Users/andrewserff/Desktop/Test Resumes/AndrewSerffResume-ACES.pdf";
//        File resume = new File(resumeFile);
//        if (resume.exists()) {
//            Map<String, String> config = new HashMap<>();
//            
//
//            try {
//                String hrxml = parser.getHRXMLForResume(resume);
////                log.info("HRXML response: " + hrxml);
//
//                ModelConverter<Candidate> converter = CandidateModelConverterFactory.getModelConverterForData(hrxml);
//                Candidate c = converter.getModelFromData(hrxml);
//
//                if (c == null) {
//                    log.error("Candidate is null...didn't convert");
//                } else {
//                    log.info("We converted the Candidate " + c.getFormattedName() + "!");
//
//                    //now tell us what is different. 
//                    CandidateHistoryService differ = new CandidateHistoryService();
//                    CandidateHistory hist = differ.diffCandidates(null, c, ValueSource.AUTOMATIC);
//
//                    for (String propName : hist.getPropertyHistoryMap().keySet()) {
//                        log.debug("History for prop : " + propName + ": " + hist.getPropertyHistoryMap().get(propName));
//                    }
//
//                    log.info("---------------Creating Copy and Changing Values---------------");
//                    Candidate c2 = c.createCopy();
//
//                    //change something
//                    c2.setGivenName("Bob");
//                    c2.setFamilyName("Ross");
//
//                    //add a few things
//                    Phone phone = new Phone();
//                    phone.setNumber("555-555-5555");
//                    c2.getPhone().add(phone);
//                    Email email = new Email();
//                    email.setAddress("me@example.com");
//                    email.setLabel(ContactType.personal);
//                    email.setPreferred(PreferenceType.primary);
//                    c2.getEmail().add(email);
//
//                    //remove something
//                    c2.getPersonCompetencies().remove(0);
//
//                    CandidateHistory hist2 = differ.diffCandidates(c, c2, ValueSource.MANUAL, "abserff");
//
//                    for (String propName : hist2.getPropertyHistoryMap().keySet()) {
//                        log.debug("History for prop : " + propName + ": " + hist2.getPropertyHistoryMap().get(propName));
//                    }
//
//                }
//            } catch (IOException ex) {
//                log.error("Error parsing resume [ " + resume.getAbsolutePath() + " ]", ex);
//            }
//        }
 }

}
