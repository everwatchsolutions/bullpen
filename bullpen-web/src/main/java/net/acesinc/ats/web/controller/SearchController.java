/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.candidate.QCandidate;
import net.acesinc.ats.model.common.EducationLevel;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.data.Result;
import net.acesinc.ats.web.repository.CandidateRepository;
import net.acesinc.ats.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author andrewserff
 */
@Controller
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    
    private static final String ARCHIVED = "Archived";
    private static final String ACTIVE = "Active";

    @Autowired
    private CandidateRepository candidateRepo;
    @Autowired
    private UserRepository userRepo;


    @RequestMapping(value = "/candidate/all", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getAllCandidates(Principal user) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            return Result.ok(candidateRepo.findByOwnerCompanyOrderByFormattedNameAsc(u.getCompany()));
        } else {
            return Result.error(null, "Unable to find user profile");
        }
    }

    @RequestMapping(value = "/candidates/search", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result searchForCandidates(Principal user, @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "skills", required = false) String[] skills,
            @RequestParam(value = "certs", required = false) String[] certs,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "yearsOfExperience", required = false) Double yearsOfExperience,
            @RequestParam(value = "searchFor", required = false) String searchFor,
            @RequestParam(value = "from", required = false) String dateFrom,
            @RequestParam(value = "to", required = false) String dateTo,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "educationLevel", required = false) String educationLevel) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date_from = Calendar.getInstance();
        Calendar date_to = Calendar.getInstance();

        if (dateFrom != null && !dateFrom.equals("")) {
            date_from.setTime(formatter.parse(dateFrom));
        }
        if (dateTo != null && !dateTo.equals("")) {
            date_to.setTime(formatter.parse(dateTo));
            date_to.add(Calendar.DATE, 1);
        }

        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            BooleanBuilder builder = new BooleanBuilder();
            QCandidate c = QCandidate.candidate;
            builder.and(c.ownerCompany.eq(u.getCompany()));
            log.debug("Starting to Build Query");
            if (name != null && !name.isEmpty()) {
                log.debug("Adding name [ " + name + " ] to query");
                builder.and(c.formattedName.containsIgnoreCase(name));
            }
            if (skills != null && skills.length > 0) {
                log.debug("Adding [ " + skills.length + " ] skills to query");
                for (String s : skills) {
                    builder.and(c.personCompetencies.any().competencyName.equalsIgnoreCase(s));
                }
                //if you want to do an OR with the Skills:
//                BooleanBuilder orSkills = new BooleanBuilder();
//                for (String s : skills) {
//                    orSkills.or(c.personCompetencies.any().competencyName.equalsIgnoreCase(s));
//                }
//                builder.and(orSkills.getValue());
            }
            if (certs != null && certs.length > 0) {
                log.debug("Adding [ " + certs.length + " ] certs to query");
                for (String s : certs) {
                    builder.and(c.certifications.any().certificationTypeCode.equalsIgnoreCase(s));
                }
                //if you want to do an OR with the Skills:
//                BooleanBuilder orSkills = new BooleanBuilder();
//                for (String s : skills) {
//                    orSkills.or(c.personCompetencies.any().competencyName.equalsIgnoreCase(s));
//                }
//                builder.and(orSkills.getValue());
            }
            if (city != null && !city.isEmpty()) {
                log.debug("Adding city [ " + city + " ] to query");
                builder.and(c.address.any().cityName.equalsIgnoreCase(city));
            }
            if (state != null && !state.isEmpty()) {
                log.debug("Adding state [ " + state + " ] to query");
                builder.and(c.address.any().state.equalsIgnoreCase(state));
            }
            if (yearsOfExperience != null) {
                log.debug("Adding years of experience >= " + yearsOfExperience + " to query");
                builder.and(c.yearsOfExperience.goe(yearsOfExperience));
            }
            if (educationLevel != null && !educationLevel.isEmpty()) {
                //TODO this might need to be an or with all levels equal to or greater the selected one. 
                log.debug("Adding education level [ " + educationLevel + " ] to query");
                EducationLevel lvl = EducationLevel.valueOf(educationLevel);
                BooleanBuilder orLvl = new BooleanBuilder();
                switch (lvl) {
                    case none:
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.none));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.highschool));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.bachelors));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.masters));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.doctorate));
                        break;
                    case highschool:
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.highschool));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.bachelors));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.masters));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.doctorate));
                        break;
                    case bachelors:
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.bachelors));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.masters));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.doctorate));
                        break;
                    case masters:
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.masters));
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.doctorate));
                        break;
                    case doctorate:
                        orLvl.or(c.highestEducationLevel.eq(EducationLevel.doctorate));
                        break;
                }
                builder.and(orLvl.getValue());
            }

            if (!builder.hasValue()) {
                return Result.error(null, "Invalid Query Parameters");
            } else {

                Predicate query = builder.getValue();

                Iterable<Candidate> results = candidateRepo.findAll(query, QCandidate.candidate.formattedName.asc());
                Iterator iterator = results.iterator();

                if (ACTIVE.equals(searchFor)) {
                    while (iterator.hasNext()) {
                        Candidate x = (Candidate) iterator.next();
                        if (x.isArchive()) {
                            iterator.remove();
                        }
                    }

                } else if (ARCHIVED.equals(searchFor)) {
                    while (iterator.hasNext()) {
                        Candidate x = (Candidate) iterator.next();
                        if (!x.isArchive()) {
                            iterator.remove();
                        }
                    }

                }

                iterator = results.iterator();

                if (location != null && !location.equalsIgnoreCase("any")) {
                    while (iterator.hasNext()) {
                        Candidate x = (Candidate) iterator.next();
                        if (x.getPreferedLocation() == null || !x.getPreferedLocation().equals(location)) {
                            iterator.remove();
                        }

                    }
                }

                //Search by Dates
                iterator = results.iterator();
                if (dateFrom != null && dateTo != null && !dateFrom.equals("") && !dateTo.equals(""))//search between to dates
                {
                    while (iterator.hasNext()) {
                        Candidate x = (Candidate) iterator.next();
                        if (x.getLastContact().before(date_from.getTime()) || x.getLastContact().after(date_to.getTime())) {
                            iterator.remove();
                        }

                    }
                } else if (dateFrom != null && !dateFrom.equals(""))//search from until present
                {
                    while (iterator.hasNext()) {
                        Candidate x = (Candidate) iterator.next();
                        if (x.getLastContact().before(date_from.getTime())) {
                            iterator.remove();
                        }
                    }
                } else if (dateTo != null && !dateTo.equals(""))//search from begining until this date
                {
                    while (iterator.hasNext()) {
                        Candidate x = (Candidate) iterator.next();
                        if (x.getLastContact().after(date_to.getTime())) {
                            iterator.remove();
                        }
                    }
                }

                return Result.ok(results);
            }

        } else {
            return Result.error(null, "Unable to find user profile");
        }
    }

   
}
