/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import net.acesinc.ats.web.service.CandidateHistoryService;
import com.mongodb.gridfs.GridFSDBFile;
import java.lang.reflect.Field;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.candidate.CandidateHistory;
import net.acesinc.ats.model.candidate.EducationalOrganization;
import net.acesinc.ats.model.candidate.Position;
import net.acesinc.ats.model.candidate.notes.CandidateFileNote;
import net.acesinc.ats.model.candidate.notes.CandidateMeetingNote;
import net.acesinc.ats.model.candidate.notes.CandidateNote;
import net.acesinc.ats.model.candidate.notes.CandidateTextNote;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.common.Certification;
import net.acesinc.ats.model.common.Competency;
import net.acesinc.ats.model.common.EducationLevel;
import net.acesinc.ats.model.common.Email;
import net.acesinc.ats.model.common.FileFormat;
import net.acesinc.ats.model.common.IdentifibleObject;
import net.acesinc.ats.model.common.Phone;
import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.common.ValueSource;
import net.acesinc.ats.model.common.Website;
import net.acesinc.ats.model.opening.Opening;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.model.workflow.Workflow;
import net.acesinc.ats.web.data.Result;
import net.acesinc.ats.web.data.UploadFile;
import net.acesinc.ats.web.data.UploadResponse;
import net.acesinc.ats.web.repository.CandidateHistoryRepository;
import net.acesinc.ats.web.repository.CandidateRepository;
import net.acesinc.ats.web.repository.UserRepository;
import net.acesinc.ats.web.service.BoxViewerService;
import net.acesinc.ats.web.service.NotificationService;
import net.acesinc.ats.web.service.box.BoxSessionResponse;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author andrewserff
 */
@Controller
public class CandidateController {

    private static final Logger log = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    private CandidateRepository candidateRepo;
    @Autowired
    private CandidateHistoryRepository candidateHistRepo;
    @Autowired
    private CandidateHistoryService candidateHistService;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BoxViewerService boxService;
    @Autowired
    private GridFsOperations mongoFs;
    @Autowired
    private ConstantsController constants;
    @Autowired
    private SearchController search;
    
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UploadController uploadController;

    private SimpleDateFormat inboundDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private SimpleDateFormat inboundDateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
    private List<String> monthList = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    private void setupCandidatePageModel(Principal user, String id, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            if (c != null) {

               
               

                CandidateHistory hist = candidateHistRepo.findByCandidateId(c.getId());
                model.addAttribute("candidate", c);
                model.addAttribute("history", hist);
                model.addAttribute("timeline", getNoteTimeline(c));
                model.addAttribute("presetNotes", c.getOwnerCompany().getPresetNotes());

                constants.populateModel(u.getCompany(), model);

                StoredFile resume = c.getMostRecentResume();
                if (resume != null) {
                    model.addAttribute("currentResume", resume);
                    String boxId = getBoxIdForStoredFile(resume.getStorageId());
                    if (boxId != null) {
                        BoxSessionResponse boxSession = boxService.createBoxViewerSession(boxId);
                        if (boxSession != null) {
                            model.addAttribute("resumeViewUrl", boxSession.getUrls().getView());
                        }
                    } else {
//                    model.addAttribute("error", true);
//                    model.addAttribute("errMessage", "Unable to load resume. Try reloading the page");
                        //the view can handle there being no URL
                    }
                }

             

                String name = c.getFormattedName();
                if (name == null || name.isEmpty()) {
                    name = c.getGivenName() + " " + c.getFamilyName();
                }
                model.addAttribute("pageName", "Candidate - " + name);

                
                model.addAttribute("availableNotes", getAllCandidateNoteTypes());
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errMessage", "Unable to find Candidate");
            }
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Unable to find Candidate");
        }
    }

    public List<CandidateNote> getAllCandidateNoteTypes() {
        List<CandidateNote> notes = new ArrayList<>();

        notes.add(new CandidateTextNote());
        notes.add(new CandidateMeetingNote());
        notes.add(new CandidateFileNote());

        return notes;
    }

    public Map<String, Map<String, List<CandidateNote>>> getNoteTimeline(Candidate c) {
        Map<String, Map<String, List<CandidateNote>>> noteTimeline = new TreeMap<>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                Integer one = Integer.parseInt(o1);
                Integer two = Integer.parseInt(o2);
                return two.compareTo(one);
            }
        });

        if (c.getNotes() != null && !c.getNotes().isEmpty()) {
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMMM");

            for (CandidateNote note : c.getNotes()) {
                Date noteTime = note.getDateCreated();
                if (note instanceof CandidateMeetingNote) {
                    noteTime = ((CandidateMeetingNote) note).getMeetingDate();
                }
                String year = yearFormat.format(noteTime);
                String month = monthFormat.format(noteTime);

                Map<String, List<CandidateNote>> yearNotes = noteTimeline.get(year);
                if (yearNotes == null) {
                    yearNotes = new TreeMap<>(new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            Integer one = monthList.indexOf(o1);
                            Integer two = monthList.indexOf(o2);
                            return two.compareTo(one);
                        }
                    });

                    noteTimeline.put(year, yearNotes);
                }

                List<CandidateNote> monthNotes = yearNotes.get(month);
                if (monthNotes == null) {
                    monthNotes = new ArrayList<>();
                    yearNotes.put(month, monthNotes);
                }

                if (note instanceof CandidateFileNote) {
                    StoredFile file = ((CandidateFileNote) note).getStoredFile();
                    String boxId = getBoxIdForStoredFile(file.getStorageId());
                    if (boxId != null) {
                        BoxSessionResponse boxSession = boxService.createBoxViewerSession(boxId);
                        if (boxSession != null) {
                            ((CandidateFileNote) note).setViewUrl(boxSession.getUrls().getView());
                        }
                    } else if (file.getFormat() != null) {
                        switch (file.getFormat()) {
                            case GIF:
                            case PNG:
                            case JPEG:
                                ((CandidateFileNote) note).setViewUrl("/view/file/" + file.getStorageId());
                                break;
                            default:
                            //Do nothing so there is no View URL on this note
                        }
                    }
                }
                monthNotes.add(note);
            }

            for (String year : noteTimeline.keySet()) {
                for (String month : noteTimeline.get(year).keySet()) {
                    List<CandidateNote> monthNotes = noteTimeline.get(year).get(month);
                    Collections.sort(monthNotes, new Comparator<CandidateNote>() {
                        public int compare(CandidateNote n1, CandidateNote n2) {
                            Date n1Date = n1.getDateCreated();
                            if (n1 instanceof CandidateMeetingNote) {
                                n1Date = ((CandidateMeetingNote) n1).getMeetingDate();
                            }
                            Date n2Date = n2.getDateCreated();
                            if (n2 instanceof CandidateMeetingNote) {
                                n2Date = ((CandidateMeetingNote) n2).getMeetingDate();
                            }

                            return n2Date.compareTo(n1Date);
                        }
                    });
                }
            }
        }

        return noteTimeline;
    }

    @RequestMapping(value = "/candidate/{id}", method = RequestMethod.GET)
    public String getCandidateById(Principal user, @PathVariable("id") String id, ModelMap model) {
        setupCandidatePageModel(user, id, model);
        return "candidate";
    }

    @RequestMapping(value = "/candidate/archive/{id}", method = RequestMethod.GET)
    public String archive(Principal user, @PathVariable("id") String id, HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {

        User u = userRepo.findByEmail(user.getName());
        Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
        if (c.isArchive()) {
            c.setArchive(false);
            log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] unarchived Candidate[ " + id + " ].");
            candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Successfully unarchived candidate");
        } else {
            c.setArchive(true);

            //remove candidate from workflow
            if (c.getWorkflow() != null) {
                Workflow w = c.getWorkflow();
                c.setWorkflow(null);
                c.setCurrentState(null);
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Successfully archived candidate and removed from workflow " + w.getName());
            } else {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Successfully archived candidate");
            }
            log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] archived Candidate[ " + id + " ].");
            candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
        }

        candidateRepo.save(c);
        return "redirect:/candidate/" + id;

    }

    @RequestMapping(value = "/candidate/startDate", method = RequestMethod.POST)
    public String startDate(Principal user, @RequestParam("id2") String id, @RequestParam("start") String start, HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {

        User u = userRepo.findByEmail(user.getName());
        Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        try {
            startDate = df.parse(start);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        if (startDate != null) {
            c.setStartDate(startDate);
            candidateRepo.save(c);

            redirectAttributes.addFlashAttribute("message", "Successfully added start date.");
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errMessage", "Please enter a vaild start date.");
        }

        return "redirect:/candidate/" + id;

    }

    @RequestMapping(value = "/candidate/secure/{id}", method = RequestMethod.GET)
    public String secure(Principal user, @PathVariable("id") String id, @RequestParam(value = "levels", required = false) Integer level, @RequestParam(value = "securityStart", required = false) String start, @RequestParam(value = "securityEnd", required = false) String end, @RequestParam(value = "verify", required = false) Integer verify, final RedirectAttributes redirectAttributes) {

        User u = userRepo.findByEmail(user.getName());
        Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startSec = null;
        Date endSec = null;

        if (level != null) {
            c.setLevel(level);
        }

        if (verify != null) {
            c.setSecurityVerify(verify);
        }

        try {
            startSec = df.parse(start);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        try {
            endSec = df.parse(end);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        if (startSec != null) {
            c.setSecurityStart(startSec);
        }

        if (endSec != null) {
            c.setSecurityEnd(endSec);
        }

        candidateRepo.save(c);
        redirectAttributes.addFlashAttribute("message", "Updated Security Clearance Information");

        return "redirect:/candidate/" + id;
    }

    @RequestMapping(value = "/candidate/{id}/edit", method = RequestMethod.POST)
    public String editCandidateProperty(Principal user, @PathVariable("id") String id, @RequestParam("fieldName") String fieldName, HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
        boolean edited = false;
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            log.debug("User [ " + user.getName() + " ] wants to edit property [ " + fieldName + " ] from Candidate [ " + id + " ]");
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            if (c != null) {
                String field = fieldName;
                String uniqueId = null;
                if (fieldName.startsWith("/")) {
                    String[] parts = fieldName.split("/");
                    field = parts[1];
                    if (parts.length > 2) {
                        uniqueId = parts[2];
                        if (parts.length > 3) {
                            for (int i = 3; i < parts.length; i++) {
                                uniqueId += "/" + parts[i];
                            }
                        }
                    }
                    log.debug("field = " + field + " uniqueId = " + uniqueId);

                }

                try {
                    Field f = Candidate.class
                            .getDeclaredField(field);
                    f.setAccessible(
                            true);
                    Object obj = f.get(c);

                    if (String.class
                            .isAssignableFrom(f.getType())) {
                        String newVal = request.getParameter("newValue");

                        f.set(c, newVal);
                        edited = true;

                        //handle a special case.  If they edited the formattedName, we should fix the first/lastname. 
                        if ("formattedName".equals(field)) {
                            String[] split = newVal.split(" ");
                            if (split.length == 1) {

                            } else if (split.length == 2) {
                                c.setGivenName(split[0]);
                                c.setFamilyName(split[1]);
                            } else {
                                //first middle last?  Two last names?  Title? What do we do?
                                c.setGivenName(split[0]);
                                String last = split[1];
                                for (int i = 2; i < split.length; i++) {
                                    last += " " + split[i];
                                }
                                c.setFamilyName(last);
                            }
                        }
                    } else if (Double.class
                            .isAssignableFrom(f.getType())) {
                        String newVal = request.getParameter("newValue");
                        Double d = Double.parseDouble(newVal);

                        f.set(c, d);
                        edited = true;
                    } else if (EducationLevel.class
                            .isAssignableFrom(f.getType())) {
                        String newVal = request.getParameter("educationLevel");
                        EducationLevel l = EducationLevel.valueOf(newVal.toLowerCase());

                        f.set(c, l);
                        edited = true;
                    } else {
                        switch (field) {
                            case "phone": {
                                String newVal = request.getParameter("newValue");
                                if (newVal != null && !newVal.equals("") && validatePhoneNumber(newVal)) {
                                    List<Phone> l = (List<Phone>) obj;
                                    for (Phone p : l) {
                                        if (p.getUniqueId().equals(uniqueId)) {
                                            p.setNumber(newVal);
                                            edited = true;
                                            break;
                                        }
                                    }
                                } else {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid phone number");
                                    edited = false;
                                }
                                break;
                            }
                            case "email": {
                                String newVal = request.getParameter("newValue");
                                if (newVal != null && !newVal.equals("")) {

                                    List<Email> l = (List<Email>) obj;
                                    for (Email e : l) {
                                        if (e.getUniqueId().equals(uniqueId)) {
                                            e.setAddress(newVal);
                                            edited = true;
                                            break;
                                        }
                                    }
                                } else {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid email address");
                                    edited = false;
                                }
                                break;

                            }
                            case "web": {
                                String newVal = request.getParameter("newValue");
                                if (newVal != null && !newVal.equals("")) {
                                    if (newVal.contains("facebook")) {
                                        redirectAttributes.addFlashAttribute("error", true);
                                        redirectAttributes.addFlashAttribute("errMessage", "Please select Facebook from the dropdown instead");
                                        edited = false;
                                        break;
                                    }

                                    if (newVal.contains("twitter")) {
                                        redirectAttributes.addFlashAttribute("error", true);
                                        redirectAttributes.addFlashAttribute("errMessage", "Please select Twitter from the dropdown instead");
                                        edited = false;
                                        break;
                                    }

                                    if (newVal.contains("linkedin")) {
                                        redirectAttributes.addFlashAttribute("error", true);
                                        redirectAttributes.addFlashAttribute("errMessage", "Please select Linkedin from the dropdown instead");
                                        edited = false;
                                        break;
                                    }

                                    if (newVal.contains("github.com")) {
                                        redirectAttributes.addFlashAttribute("error", true);
                                        redirectAttributes.addFlashAttribute("errMessage", "Please select GitHub from the dropdown instead");
                                        edited = false;
                                        break;
                                    }
                                    List<Website> l = (List<Website>) obj;
                                    for (Website w : l) {
                                        if (w.getUniqueId().equals(uniqueId)) {
                                            w.setAddress(newVal);
                                            edited = true;
                                            break;
                                        }
                                    }

                                } else {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid website");
                                    edited = false;
                                }
                                break;
                            }
                            case "address": {
                                //get all the things
                                String address = request.getParameter("loc-street-address");
                                String city = request.getParameter("loc-city");
                                String state = request.getParameter("loc-state");
                                String zip = request.getParameter("loc-zipcode");

                                if (address.equals("") || city.equals("") || zip.equals("")) {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Please enter a valid address");
                                    edited = false;
                                } else {
                                    List<Address> l = (List<Address>) obj;
                                    for (Address a : l) {
                                        if (a.getUniqueId().equals(uniqueId)) {
                                            a.setAddressLine(address);
                                            a.setCityName(city);
                                            a.setState(state);
                                            a.setPostalCode(zip);
                                            edited = true;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                            case "positionHistory": {
                                String employer = request.getParameter("emp-name");
                                String position = request.getParameter("emp-pos-title");
                                String startDate = request.getParameter("emp-start-date");
                                String endDate = request.getParameter("emp-end-date");

                                if (employer.equals("") || position.equals("") || startDate.equals("") || endDate.equals("")) {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Please enter an employer, position, and start and end dates");
                                    edited = false;
                                } else {
                                    List<Position> l = (List<Position>) obj;
                                    for (Position p : l) {
                                        if (p.getUniqueId().equals(uniqueId)) {
                                            p.setEmployer(employer);
                                            p.setPositionTitle(position);
                                            p.setStartDate(startDate);
                                            p.setEndDate(endDate);
                                            edited = true;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                            case "educationalOrganizations": {
                                String school = request.getParameter("school-name");
                                String startDate = request.getParameter("school-start-date");
                                String endDate = request.getParameter("school-end-date");

                                if (school.equals("") || startDate.equals("") || endDate.equals("")) {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Please enter a school, start date, and end date");
                                    edited = false;
                                } else {
                                    List<EducationalOrganization> l = (List<EducationalOrganization>) obj;
                                    for (EducationalOrganization edu : l) {
                                        if (edu.getUniqueId().equals(uniqueId)) {
                                            edu.setSchool(school);
                                            edu.setAttendanceStartDate(startDate);
                                            edu.setAttendanceEndDate(endDate);
                                            edited = true;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                            case "notes": {
                                String title = request.getParameter("note-title");
                                String text = request.getParameter("note-text");
                                text = text.replace("\r\n", " ").replace("\n", "");
                                boolean alreadyUsed = false;

                                List<CandidateNote> list = (List<CandidateNote>) obj;
                                if (list != null && !list.isEmpty()) {
                                    for (CandidateNote note : list) {
                                        if ((note.getRequiredInfoFieldValue("note-title")).equals(title)) {
                                            alreadyUsed = true;
                                        }
                                    }
                                }

                                if (title.equals("") || text.trim().equals("")) {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "Cannot edit a note with blank Title or Text");
                                    edited = false;
                                } else if (alreadyUsed && !uniqueId.equals(title)) {
                                    redirectAttributes.addFlashAttribute("error", true);
                                    redirectAttributes.addFlashAttribute("errMessage", "A note with that title already exists");
                                    edited = false;
                                } else {
                                    for (CandidateNote note : list) {
                                        if ((note.getRequiredInfoFieldValue("note-title")).equals(uniqueId)) {
                                            CandidateTextNote n = (CandidateTextNote) note;
                                            n.setTitle(title);
                                            n.setText(text);
                                            edited = true;
                                        }
                                    }
                                }
                                break;
                            }
                            case "personCompetencies": {
                                break;  //you can't edit skills...
                            }
                        }
                    }
                    if (edited) {
                        log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] edited old value [ " + obj + " ] from field [ " + f.getName() + " ] from Candidate[ " + id + " ]");
                        candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
                        candidateRepo.save(c);
                    } else {
                        log.debug("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] tried updating field [ " + f.getName() + " ] from Candidate[ " + id + " ] but nothing was changed");
                    }
                } catch (NoSuchFieldException | IllegalAccessException | SecurityException ex) {
                    log.debug("Error trying to access Candidate field [ " + field + " ]");
                }
            } else {
                log.debug("Unable to find Candidate [ " + id + " ] in Company [ " + u.getCompany().getName() + " ]");
            }
        }

        if (edited) {
            redirectAttributes.addFlashAttribute("message", "Candidate has been edited successfully");
        }
        /*else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errMessage", "Sorry. An error occured editing the Candidate");
        }*/
        // Else statement was overriding error messages above. Now allows for specific error messages

        return "redirect:/candidate/" + id;
    }

    //FIXME should be a DELETE request and also naming is inconsistent (Remove vs Delete)
    @RequestMapping(value = "/candidate/{id}/removeProperty", method = RequestMethod.POST)
    public String deleteCandidateProperty(Principal user, @PathVariable("id") String id, @RequestParam("fieldName") String fieldName, ModelMap model, final RedirectAttributes redirectAttributes) {
        boolean edited = false;
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            log.debug("User [ " + user.getName() + " ] wants to delete property [ " + fieldName + " ] from Candidate [ " + id + " ]");
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            if (c != null) {
                String field = fieldName;
                String uniqueId = null;
                if (fieldName.startsWith("/")) {
                    String[] parts = fieldName.split("/");
                    field = parts[1];
                    if (parts.length > 2) {
                        uniqueId = parts[2];
                        if (parts.length > 3) {
                            for (int i = 3; i < parts.length; i++) {
                                uniqueId += "/" + parts[i];
                            }
                        }
                    }
                    log.debug("field = " + field + " uniqueId = " + uniqueId);

                }
                try {
                    Field f = Candidate.class
                            .getDeclaredField(field);
                    f.setAccessible(
                            true);
                    Object obj = f.get(c);

                    if (String.class
                            .isAssignableFrom(f.getType())) {
                        log.info(
                                "User [ " + u.getEmail() + " ( " + u.getId() + " ) ] removed old value [ " + f.get(c) + " ] from field [ " + f.getName() + " ] from Candidate[ " + id + " ]");
                        f.set(c,
                                null);
                        edited = true;
                    } else if (List.class
                            .isAssignableFrom(obj.getClass())) {
                        List l = (List) obj;
                        Iterator it = l.iterator();

                        while (it.hasNext()) {
                            Object o = it.next();
                            if (o instanceof IdentifibleObject) {
                                IdentifibleObject i = (IdentifibleObject) o;
                                if (i.getUniqueId().equals(uniqueId)) {
                                    log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] removed old value [ " + i.getUniqueId() + " ] from field [ " + f.getName() + " ] from Candidate[ " + id + " ]");
                                    it.remove();
                                    edited = true;
                                    break;
                                }
                            } else if (o instanceof CandidateNote) {
                                CandidateNote note = (CandidateNote) o;
                                if (note.getRequiredInfoFieldValue("note-title").equals(uniqueId)) {
                                    log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] removed old value [ " + note.getRequiredInfoFieldValue("note-title") + " ] from field [ " + f.getName() + " ] from Candidate[ " + id + " ]");
                                    it.remove();
                                    edited = true;
                                    break;
                                }
                            } else {
                                log.debug("List does not contain IdentifibleObjects.  Can't remove Objects of type [ " + o.getClass() + " ]");
                            }
                        }
                        if (edited) {
                            candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
                            candidateRepo.save(c);
                        }
                    } else {
                        log.debug("Unknown type of property to remove [ " + obj.getClass() + " ]");
                    }
                } catch (NoSuchFieldException | IllegalAccessException | SecurityException ex) {
                    log.debug("Error trying to access Candidate field [ " + field + " ]");
                }
            } else {
                log.debug("Unable to find Candidate [ " + id + " ] in Company [ " + u.getCompany().getName() + " ]");
            }
        }

        if (edited) {
            redirectAttributes.addFlashAttribute("message", "Candidate has been edited successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errMessage", "Sorry. An error occured editing the Candidate");
        }
        return "redirect:/candidate/" + id;
    }

    @RequestMapping(value = "/candidate/{id}/add", method = RequestMethod.POST)
    public String addCandidateProperty(Principal user, @PathVariable("id") String id, @RequestParam("itemType") String itemType, @RequestParam(value = "note-file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
        boolean edited = false;
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            log.debug("User [ " + user.getName() + " ] wants to add property of type [ " + itemType + " ] to Candidate [ " + id + " ]");
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            CandidateNote note = null;
            if (c != null) {
                switch (itemType) {
                    case "phone": {
                        String val = request.getParameter("number");
                        if (val != null && !val.equals("") && validatePhoneNumber(val)) {
                            Phone p = new Phone();
                            p.setNumber(val);
                            if (c.getPhone() == null) {
                                List<Phone> l = new ArrayList<>();
                                c.setPhone(l);
                            }
                            c.getPhone().add(p);
                            edited = true;
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid phone number");
                            edited = false;
                        }
                        break;
                    }
                    case "email": {
                        String val = request.getParameter("email");
                        if (val != null && !val.equals("")) {
                            Email v = new Email();
                            v.setAddress(val);
                            if (c.getEmail() == null) {
                                List<Email> l = new ArrayList<>();
                                c.setEmail(l);
                            }
                            c.getEmail().add(v);
                            edited = true;
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid email address");
                            edited = false;
                        }
                        break;
                    }
                    case "web": {
                        String val = request.getParameter("web");

                        if (c.getWeb() == null) {
                            List<Website> l = new ArrayList<>();
                            c.setWeb(l);
                        }
                        if (val != null && !val.equals("")) {
                            if (val.contains("facebook")) {
                                redirectAttributes.addFlashAttribute("error", true);
                                redirectAttributes.addFlashAttribute("errMessage", "Please select Facebook from the dropdown instead");
                                edited = false;
                                break;
                            }

                            if (val.contains("twitter")) {
                                redirectAttributes.addFlashAttribute("error", true);
                                redirectAttributes.addFlashAttribute("errMessage", "Please select Twitter from the dropdown instead");
                                edited = false;
                                break;
                            }

                            if (val.contains("linkedin")) {
                                redirectAttributes.addFlashAttribute("error", true);
                                redirectAttributes.addFlashAttribute("errMessage", "Please select Linkedin from the dropdown instead");
                                edited = false;
                                break;
                            }

                            if (val.contains("github.com")) {
                                redirectAttributes.addFlashAttribute("error", true);
                                redirectAttributes.addFlashAttribute("errMessage", "Please select GitHub from the dropdown instead");
                                edited = false;
                                break;
                            }

                            if (c.getWeb() == null) {
                                List<Website> l = new ArrayList<>();
                                c.setWeb(l);
                            }
                            if (!val.startsWith("http://")) {
                                String val2 = "http://" + val;
                                Website v2 = new Website();
                                v2.setAddress(val2);
                                c.getWeb().add(v2);
                            } else {
                                Website v = new Website();
                                v.setAddress(val);
                                c.getWeb().add(v);
                            }
                            edited = true;
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid website");
                            edited = false;
                        }
                        break;
                    }
                    case "facebook": {
                        String val = request.getParameter("facebook");

                        if (val != null && val.contains("facebook")) {
                            if (c.getWeb() == null) {
                                List<Website> l = new ArrayList<>();
                                c.setWeb(l);
                            }

                            if (val.contains("facebook")) {
                                if (val.startsWith("http://") || val.startsWith("https://")) {
                                    c.updateFacebook(val);
                                } else {
                                    // add http:// to URL
                                    String val2 = "http://" + val;
                                    c.updateFacebook(val2);
                                }
                                edited = true;
                            }
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid Facebook URL");
                            edited = false;
                        }
                        break;
                    }
                    case "twitter": {
                        String val = request.getParameter("twitter");

                        if (val != null && val.contains("twitter")) {
                            if (c.getWeb() == null) {
                                List<Website> l = new ArrayList<>();
                                c.setWeb(l);
                            }

                            if (val.startsWith("http://") || val.startsWith("https://")) {
                                c.updateTwitter(val);
                            } else {
                                // add http:// to URL
                                String val2 = "http://" + val;
                                c.updateTwitter(val2);
                            }

                            edited = true;
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid Twitter URL");
                            edited = false;
                        }
                        break;
                    }
                    case "linkedin": {
                        String val = request.getParameter("linkedin");

                        if (val != null && val.contains("linkedin")) {
                            if (c.getWeb() == null) {
                                List<Website> l = new ArrayList<>();
                                c.setWeb(l);
                            }

                            if (val.startsWith("http://") || val.startsWith("https://")) {
                                c.updateLinkedin(val);
                            } else {
                                // add http:// to URL
                                String val2 = "http://" + val;
                                c.updateLinkedin(val2);
                            }

                            edited = true;
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid Linkedin URL");
                            edited = false;
                        }
                        break;
                    }
                    case "github": {
                        String val = request.getParameter("github");

                        if (val != null && val.contains("github")) {
                            if (c.getWeb() == null) {
                                List<Website> l = new ArrayList<>();
                                c.setWeb(l);
                            }

                            if (val.startsWith("http://") || val.startsWith("https://")) {
                                c.updateGithub(val);
                            } else {
                                // add http:// to URL
                                String val2 = "http://" + val;
                                c.updateGithub(val2);
                            }

                            edited = true;
                        } else {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please provide a valid GitHub URL");
                            edited = false;
                        }
                        break;
                    }
                    case "address": {
                        //get all the things
                        String address = request.getParameter("loc-street-address");
                        String city = request.getParameter("loc-city");
                        String state = request.getParameter("loc-state");
                        String zip = request.getParameter("loc-zipcode");

                        if (address.equals("") || city.equals("") || zip.equals("")) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please enter a valid address");
                            edited = false;
                        } else {
                            Address a = new Address();
                            a.setAddressLine(address);
                            a.setCityName(city);
                            a.setState(state);
                            a.setPostalCode(zip);

                            if (c.getAddress() == null) {
                                List<Address> l = new ArrayList<>();
                                c.setAddress(l);
                            }
                            c.getAddress().add(a);
                            edited = true;
                        }
                        break;
                    }
                    case "positionHistory": {
                        String employer = request.getParameter("emp-name");
                        String position = request.getParameter("emp-pos-title");
                        String startDate = request.getParameter("emp-start-date");
                        String endDate = request.getParameter("emp-end-date");

                        if (employer.equals("") || position.equals("") || startDate.equals("") || endDate.equals("")) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please enter an employer, position, and start and end dates");
                            edited = false;
                        } else {
                            Position p = new Position();
                            p.setEmployer(employer);
                            p.setPositionTitle(position);
                            p.setStartDate(startDate);
                            p.setEndDate(endDate);

                            if (c.getPositionHistory() == null) {
                                List<Position> l = new ArrayList<>();
                                c.setPositionHistory(l);
                            }
                            c.getPositionHistory().add(p);
                            edited = true;
                        }
                        break;
                    }
                    case "educationalOrganizations": {
                        String school = request.getParameter("school-name");
                        String startDate = request.getParameter("school-start-date");
                        String endDate = request.getParameter("school-end-date");

                        if (school.equals("") || startDate.equals("") || endDate.equals("")) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please enter a school, start date, and end date");
                            edited = false;
                        } else {
                            EducationalOrganization edu = new EducationalOrganization();
                            edu.setSchool(school);
                            edu.setAttendanceStartDate(startDate);
                            edu.setAttendanceEndDate(endDate);

                            if (c.getEducationalOrganizations() == null) {
                                List<EducationalOrganization> l = new ArrayList<>();
                                c.setEducationalOrganizations(l);
                            }
                            c.getEducationalOrganizations().add(edu);
                            edited = true;
                        }
                        break;
                    }
                    case "personCompetencies": {
                        String[] skills = request.getParameterValues("skills");
                        List<Competency> skillList = constants.getSkillsList(skills, u.getCompany());

                        if (c.getPersonCompetencies() == null) {
                            c.setPersonCompetencies(skillList);
                        } else if (skillList == null) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please select a skill to be added");
                            edited = false;
                        } else {
                            c.getPersonCompetencies().addAll(skillList);
                            edited = true;
                        }

                        break;
                    }
                    case "certifications": {
                        String[] certs = request.getParameterValues("certs");
                        List<Certification> certList = constants.getCertificationList(certs, u.getCompany());

                        if (c.getCertifications() == null) {
                            List<net.acesinc.ats.model.candidate.Certification> candCert = new ArrayList<>();
                            c.setCertifications(candCert);
                        }

                        if (certList == null) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Please select a certification to be added");
                            edited = false;
                        } else {
                            for (Certification dbCert : certList) {
                                net.acesinc.ats.model.candidate.Certification cert = new net.acesinc.ats.model.candidate.Certification();
                                cert.setCertificationName(dbCert.getDisplayName());
                                cert.setCertificationTypeCode(dbCert.getName());
                                c.getCertifications().add(cert);
                            }
                            edited = true;
                        }

                        break;
                    }
                    case "note-text": {
                        String title = request.getParameter(CandidateTextNote.titleFieldName);
                        String text = request.getParameter(CandidateTextNote.textFieldName);
                        text = text.replace("\r\n", " ").replace("\n", "").replace("\t", "").replace("&nbsp;", "");
                        boolean alreadyUsed = false;

                        List<CandidateNote> list = (List<CandidateNote>) c.getNotes();
                        if (list != null && !list.isEmpty()) {
                            int counter = list.size();
                            for (CandidateNote n : list) {
                                if ((n.getRequiredInfoFieldValue("note-title")).equals(title)) {
                                    title = title + counter;
                                    // alreadyUsed = true;
                                }
                            }
                        }

                        if (title.trim().equals("") || text.trim().equals("")) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "Cannot create note with blank Title or Text");
                            edited = false;
                        } else if (alreadyUsed) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("errMessage", "A note with that title already exists");
                            edited = false;
                        } else {
                            note = new CandidateTextNote();
                            note.setTitle(title);
                            ((CandidateTextNote) note).setText(text);
                            c.addCandidateNote(note);

                            notificationService.nofifyUsersOfCandidateNote(c, note, u);
                            edited = true;
                        }
                        break;
                    }
                    case "note-meeting": {
                        String title = request.getParameter(CandidateNote.titleFieldName);
                        String date = request.getParameter(CandidateMeetingNote.meetingDateFieldName);
                        String time = request.getParameter(CandidateMeetingNote.meetingTimeFieldName);
                        String loc = request.getParameter(CandidateMeetingNote.meetingLocationFieldName);
                        String desc = request.getParameter(CandidateMeetingNote.meetingDescriptionFieldName);
                        boolean alreadyUsed = false;

                        List<CandidateNote> list = (List<CandidateNote>) c.getNotes();
                        if (list != null && !list.isEmpty()) {
                            for (CandidateNote n : list) {
                                if ((n.getRequiredInfoFieldValue("note-title")).equals(title)) {
                                    alreadyUsed = true;
                                }
                            }
                        }

                        log.debug("Creating MeetingNote: what: [ " + title + "], where: [ " + loc + " ], when: [ " + date + " " + time + " ] desc: " + desc);
                        try {
                            if (title.trim().equals("") || desc.trim().equals("")) {
                                redirectAttributes.addFlashAttribute("error", true);
                                redirectAttributes.addFlashAttribute("errMessage", "Cannot create note with blank Title or Description");
                                edited = false;
                            } else if (alreadyUsed) {
                                redirectAttributes.addFlashAttribute("error", true);
                                redirectAttributes.addFlashAttribute("errMessage", "A note with that title already exists");
                                edited = false;
                            } else {
                                CandidateMeetingNote meetingNote = new CandidateMeetingNote();
                                meetingNote.setTitle(title);
                                meetingNote.setMeetingLocation(loc);
                                meetingNote.setMeetingDescription(desc);

                                meetingNote.setMeetingDate(inboundDateTimeFormat.parse(date + " " + time));

                                c.addCandidateNote(meetingNote);
                                note = meetingNote;

                                edited = true;
                            }
                            break;
                        } catch (ParseException ex) {
                            log.warn("Error parsing Date [ " + date + " " + time + " ]");
                        }
                        break;
                    }
                    case "note-file": {
                        if (file != null) {
                            log.debug("Creating File Note with file [ " + file.getOriginalFilename() + " ]");
                            UploadResponse upResp = uploadController.handleFileUpload(user, file);
                            if (upResp != null) {
                                if (upResp.isError()) {
                                    log.warn("There was an saving uploaded file [ " + file.getOriginalFilename() + " ]. Status: " + upResp.getTextStatus());
                                } else {
                                    UploadFile uf = upResp.getFiles().get(0);
                                    String title = request.getParameter(CandidateNote.titleFieldName);
                                    String desc = request.getParameter(CandidateFileNote.fileDescriptionFieldName);

                                    boolean alreadyUsed = false;

                                    List<CandidateNote> list = (List<CandidateNote>) c.getNotes();
                                    if (list != null && !list.isEmpty()) {
                                        for (CandidateNote n : list) {
                                            if ((n.getRequiredInfoFieldValue("note-title")).equals(title)) {
                                                alreadyUsed = true;
                                            }
                                        }
                                    }

                                    if (title.trim().equals("") || desc.trim().equals("")) {
                                        redirectAttributes.addFlashAttribute("error", true);
                                        redirectAttributes.addFlashAttribute("errMessage", "Cannot create note with blank Title or Description");
                                        edited = false;
                                    } else if (alreadyUsed) {
                                        redirectAttributes.addFlashAttribute("error", true);
                                        redirectAttributes.addFlashAttribute("errMessage", "A note with that title already exists");
                                        edited = false;
                                    } else {
                                        CandidateFileNote fileNote = new CandidateFileNote();
                                        fileNote.setTitle(title);
                                        fileNote.setFileDescription(desc);

                                        StoredFile storedFile = getStoredFile(uf);
                                        fileNote.setStoredFile(storedFile);

                                        c.addCandidateNote(fileNote);
                                        note = fileNote;
                                        edited = true;
                                    }
                                    break;
                                }
                            }
                        } else {
                            log.debug("File note sent with no file.");
                        }
                        break;
                    }
                    case "eeo": {

                        List<String> eeo = new ArrayList();
                        eeo.add(request.getParameter("gender"));
                        eeo.add(request.getParameter("race"));
                        eeo.add(request.getParameter("vet"));
                        eeo.add(request.getParameter("dis"));
                        c.setEEO(eeo);
                        edited = true;
                        break;

                    }
                }

                if (edited) {
                    log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] added new item of type [ " + itemType + " ] to Candidate[ " + id + " ]");
                    candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
                    candidateRepo.save(c);

                    //send notifications
                    if (note != null) {
                        notificationService.nofifyUsersOfCandidateNote(c, note, u);
                    }
                }
            } else {
                log.debug("Unable to find Candidate [ " + id + " ] in Company [ " + u.getCompany().getName() + " ]");
            }
        }

        if (edited) {
            redirectAttributes.addFlashAttribute("message", "Candidate has been edited successfully");
        }
        /*else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errMessage", "Sorry. An error occured editing the Candidate");
        } */

        // Else statement was overriding error messages above. Now allows for specific error messages
        return "redirect:/candidate/" + id;
    }

    protected StoredFile getStoredFile(UploadFile uf) {
        StoredFile storedFile = new StoredFile();
        storedFile.setDateAdded(new Date());
        storedFile.setFilename(uf.getName());
        storedFile.setStorageId(uf.getStorageId());
        try {
            storedFile.setFormat(FileFormat.fromMimeType(uf.getContentType()));
        } catch (Exception e) {
            //unknown type
            log.debug("Could not determine the format of file [ " + uf.getName() + " ] with content type [ " + uf.getContentType() + " ]");
        }
        return storedFile;
    }

    @RequestMapping(value = "/candidate/{id}/add/interestedUser", method = RequestMethod.POST)
    public @ResponseBody
    Result addInterestedUserToCandidate(Principal user, @PathVariable("id") String id, @RequestParam("userId") String userId, HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            if (c != null) {
                User userToAdd = userRepo.findOne(userId);
                if (userToAdd != null) {
                    c.addInterestedUser(userToAdd);

                    log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] Added Interested user [ " + userToAdd.getId() + " ] to Candidate [ " + c.getId() + " ]");
                    candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
                    candidateRepo.save(c);

                    return Result.ok(c);
                }
            }

        }
        return Result.error(null, "Error adding Interested User to Candidate");
    }

    //FIXME method name is bad English.
    @RequestMapping(value = "/candidate/{id}/remove/interestedUser", method = RequestMethod.POST)
    public @ResponseBody
    Result removeInterestedUserToCandidate(Principal user, @PathVariable("id") String id, @RequestParam("userId") String userId, HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            if (c != null) {

                for (User userToRemove : c.getInterestedUsers()) {
                    if (userToRemove.getId().equals(userId)) {
                        c.getInterestedUsers().remove(userToRemove);
                        log.info("User [ " + u.getEmail() + " ( " + u.getId() + " ) ] Removed Interested user [ " + userToRemove.getId() + " ] from Candidate [ " + c.getId() + " ]");
                        candidateHistService.updateCandidateHistory(c, ValueSource.MANUAL, u.getFullName() + " ( " + u.getEmail() + " )");
                        candidateRepo.save(c);

                        return Result.ok(c);
                    }
                }
                return Result.error(null, "User [ " + userId + " ] was not in the list of Interested Users.");
            }
        }
        return Result.error(null, "Error Removing Interested User from Candidate");
    }

   
  

    @RequestMapping(value = "/candidate/{id}/history", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    CandidateHistory getCandidateHistoryById(Principal user, @PathVariable("id") String id, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
            if (c != null) {

                CandidateHistory hist = candidateHistRepo.findByCandidateId(c.getId());
                return hist;
            }
        }
        return null;
    }

    @RequestMapping(value = "/candidates", method = RequestMethod.GET)
    public String getCandidatesPage(Principal user, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            constants.populateModel(u.getCompany(), model);
        }
        model.addAttribute("pageName", "Candidates");
        model.addAttribute("company", u.getCompany());
        return "candidates_1";
    }

    //FIXME should this be candidate without the 's'?
    @RequestMapping(value = "/candidates/add", method = RequestMethod.GET)
    public String getCandidateAddPage(Principal user, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            constants.populateModel(u.getCompany(), model);
        }
        model.addAttribute("pageName", "Add Candidates");
        return "candidate-add";
    }

    @RequestMapping(value = "/candidates/archive", method = RequestMethod.GET)
    public String getCandidateArchivePage(Principal user, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            constants.populateModel(u.getCompany(), model);
        }
        model.addAttribute("pageName", "Archive Candidates");
        return "candidate-archive";
    }

    @RequestMapping(value = "/candidate", method = RequestMethod.GET)
    public String getCandidatePage(ModelMap model) {
        model.addAttribute("pageName", "Candidate");
        return "candidate";
    }

    protected String getBoxIdForStoredFile(String storageId) {
        Query query = new Query(GridFsCriteria.where("_id").is(new ObjectId(storageId)));
        List<GridFSDBFile> files = mongoFs.find(query);
        if (files != null && files.size() > 0) {
            GridFSDBFile file = files.get(0);
            Object o = file.getMetaData().get("boxId");
            if (o != null) {
                return o.toString();
            } else {
                boxService.sendFileToBoxServiceAsync(file);
                return null;
            }
        } else {
            return null;
        }
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        if (phoneNo.matches("\\d{10}")) {
            return true;
        } //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        } //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        } //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        } //return false if nothing matches the input
        else {
            return false;
        }

    }

    @RequestMapping(value = "/candidate/{id}/addName", method = RequestMethod.POST)
    public String addName(Principal user, @PathVariable("id") String id, @RequestParam("first") String first, @RequestParam("last") String last) {
        User u = userRepo.findByEmail(user.getName());

        Candidate c = candidateRepo.findByIdAndOwnerCompany(id, u.getCompany());
        c.setFamilyName(last);
        c.setGivenName(first);
        c.setFormattedName(first + " " + last);
        candidateRepo.save(c);

        return "candidate/add";

    }
}
