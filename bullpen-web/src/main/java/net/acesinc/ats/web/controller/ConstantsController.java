/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.common.Category;
import net.acesinc.ats.model.common.Certification;
import net.acesinc.ats.model.common.Competency;
import net.acesinc.ats.model.common.EducationLevel;
import net.acesinc.ats.model.common.NameAndCode;
import net.acesinc.ats.model.common.Skill;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.data.Result;
import net.acesinc.ats.web.data.State;
import net.acesinc.ats.web.repository.CandidateRepository;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.CategoryRepository;
import net.acesinc.ats.web.repository.CertificationRepository;
import net.acesinc.ats.web.repository.SkillRepository;
import net.acesinc.ats.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author andrewserff
 */
@Controller
public class ConstantsController {

    private static final Logger log = LoggerFactory.getLogger(ConstantsController.class);

    public static final String UNCATEGORIZED_CATEGORY_NAME = "uncategorized";
    public static final String UNCATEGORIZED_CATEGORY_LABEL = "Uncategorized";

    @Autowired
    private SkillRepository skillRepo;
    @Autowired
    private CertificationRepository certRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CandidateRepository candidateRepo;
    
    public final String REMOVE_ONLY_USER_ERROR = "Cannot remove the only user of a company";
    public final String REMOVE_YOURSELF_USER_ERROR = "You cannot remove yourself from the company";

    public ModelMap populateModel(Company company, ModelMap model) {
        Map<String, List<Skill>> skillCatMap = new HashMap<>();
        Iterable<Skill> skills = (Iterable<Skill>) getAllSkills(company).getData();
        for (Skill s : skills) {
            List<Skill> sList = skillCatMap.get(s.getCategory().getDisplayName());
            if (sList == null) {
                sList = new ArrayList<>();
                skillCatMap.put(s.getCategory().getDisplayName(), sList);
            }
            sList.add(s);
        }
        model.addAttribute("skills", skillCatMap);

        Map<String, List<Certification>> certCatMap = new HashMap<>();
        Iterable<Certification> certs = (Iterable<Certification>) getAllCertifications(company).getData();
        for (Certification c : certs) {
            List<Certification> cList = certCatMap.get(c.getCategory().getDisplayName());
            if (cList == null) {
                cList = new ArrayList<>();
                certCatMap.put(c.getCategory().getDisplayName(), cList);
            }
            cList.add(c);
        }
        model.addAttribute("certifications", certCatMap);

        model.addAttribute("categories", getAllCategories(company).getData());
        model.addAttribute("states", getStateList());
        model.addAttribute("educationLevels", EducationLevel.values());
        

        return model;
    }

    @RequestMapping(value = "/skill/all", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getAllSkills(Company company) {
        return Result.ok(skillRepo.findByOwnerCompany(company));
    }

    @RequestMapping(value = "/skill/{name}", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getSkillByName(@PathVariable("name") String name, Company company) {
        Skill s = skillRepo.findByNameAndOwnerCompany(name, company);
        return Result.ok(s);
    }

    @RequestMapping(value = "/certification/all", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getAllCertifications(Company company) {
        return Result.ok(certRepo.findByOwnerCompany(company));
    }

    @RequestMapping(value = "/certification/{name}", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getCertificationByName(@PathVariable("name") String name, Company company) {
        Certification s = certRepo.findByNameAndOwnerCompany(name, company);
        return Result.ok(s);
    }

    @RequestMapping(value = "/category/all", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getAllCategories(Company company) {
        return Result.ok(categoryRepo.findByOwnerCompany(company));
    }

    @RequestMapping(value = "/category/{name}", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getCategoryByName(@PathVariable("name") String name, Company company) {
        Category s = categoryRepo.findByNameAndOwnerCompany(name, company);
        return Result.ok(s);
    }

    @RequestMapping(value = "/skill", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result createSkill(Principal user, @RequestParam(value = "name", required = false) String name, @RequestParam("label") String label, @RequestParam(value = "origLabel", required = false) String origLabel, @RequestParam("category") String category) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            if (origLabel == null) {
                origLabel = label;
            }
            Skill s = createSkill(name, label, origLabel, category, u.getCompany());
            return Result.ok(s);
        } else {
            return Result.error(null, "Unknown User");
        }
    }

    public Skill createSkill(String name, String label, String category, Company company) {
        return createSkill(name, label, label, category, company);
    }

    public Skill createSkill(String name, String label, String origLabel, String category, Company company) {
        if (name == null) {
            name = origLabel.toLowerCase().replaceAll(" ", "-");
        }
        Category dbCat = categoryRepo.findByNameAndOwnerCompany(category, company);
        Skill s = skillRepo.findByNameAndOwnerCompany(name.toLowerCase().trim(), company);
        if (s == null) {
            log.info("Creating new Skill [ " + name.toLowerCase().trim() + ", " + label + ", " + category + " ]");
            s = new Skill();
            s.setName(name.toLowerCase().trim());
            if (label == null || label.isEmpty()) {
                label = name;
            }
            s.setDisplayName(label.trim());
            s.setCategory(dbCat);
            s.setOwnerCompany(company);

            skillRepo.save(s);
        } else {
            if (!s.getName().equals(name)) {
                s.setName(name);
            }
            if (!s.getDisplayName().equals(label)) {
                //see if there is already a cert with this name:
                String newName = label.toLowerCase().replaceAll(" ", "-");
                Skill test = skillRepo.findByNameAndOwnerCompany(newName, company);
                if (test == null) {
                    s.setDisplayName(label);
                    s.setName(newName);
                } else {
                    skillRepo.delete(s);
                    s = test;
                }
            }
            if (!dbCat.getName().equals(UNCATEGORIZED_CATEGORY_NAME) && !s.getCategory().getName().equals(dbCat.getName())) {
                s.setCategory(dbCat);
            }
            skillRepo.save(s);
        }
        return s;
    }

    @RequestMapping(value = "/certification", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity createCertification(Principal user, @RequestParam(value = "name", required = false) String name, @RequestParam("label") String label, @RequestParam(value = "origLabel", required = false) String origLabel, @RequestParam("category") String category) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            if (origLabel == null) {
                origLabel = label;
            }

            Certification c = createCertification(name, label, origLabel, category, u.getCompany());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
            //return Result.ok(c);
        } else {
            //return Result.error(null, "Unknown User");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/remove-skill", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity removeSkill(Principal user, @RequestParam("skill") String skillToRemove, final RedirectAttributes redirectAttributes) {
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();

        if (u != null && c != null) {
            Skill s = skillRepo.findByNameAndOwnerCompany(skillToRemove.replaceAll(" ", "-"), c);
            if (s != null) {
                skillRepo.delete(s);
                redirectAttributes.addFlashAttribute("errMessage", "Successfully removed the certification");
                return new ResponseEntity<>(HttpStatus.FOUND);
            } else {
                redirectAttributes.addFlashAttribute("errMessage", "Error accessing certification info");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            redirectAttributes.addFlashAttribute("errMessage", "Error accessing certification info");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/remove-certification", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity removeCertification(Principal user, @RequestParam("certification") String certificationToRemove, final RedirectAttributes redirectAttributes) {
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();

        if (u != null && c != null) {
            Certification cert = certRepo.findByNameAndOwnerCompany(certificationToRemove.replaceAll(" ", "-"), c);
            if (cert != null) {
                certRepo.delete(cert);
                redirectAttributes.addFlashAttribute("errMessage", "Successfully removed the certification");
                return new ResponseEntity<>(HttpStatus.FOUND);
            } else {
                redirectAttributes.addFlashAttribute("errMessage", "Error accessing certification info");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            redirectAttributes.addFlashAttribute("errMessage", "Error accessing company info");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public Certification createCertification(String name, String label, String category, Company company) {
        return createCertification(name, label, label, category, company);
    }

    public Certification createCertification(String name, String label, String origLabel, String category, Company company) {
        if (name == null) {
            name = origLabel.toLowerCase().replaceAll(" ", "-");
        }
        Category dbCat = categoryRepo.findByNameAndOwnerCompany(category, company);
        Certification c = certRepo.findByNameAndOwnerCompany(name.toLowerCase().trim(), company);
        if (c == null) {
            log.info("Creating new Certification [ " + name + ", " + label + ", " + category + " ]");
            c = new Certification();
            c.setName(name.toLowerCase().trim());
            if (label == null || label.isEmpty()) {
                label = name;
            }
            c.setDisplayName(label.trim());
            c.setCategory(dbCat);
            c.setOwnerCompany(company);

            certRepo.save(c);
        } else {
            if (!c.getName().equals(name)) {
                c.setName(name);
            }
            if (!c.getDisplayName().equals(label)) {
                //see if there is already a cert with this name:
                String newName = label.toLowerCase().replaceAll(" ", "-");
                Certification test = certRepo.findByNameAndOwnerCompany(newName, company);
                if (test == null) {
                    c.setDisplayName(label);
                    c.setName(newName);
                } else {
                    certRepo.delete(c);
                    c = test;
                }
            }
            if (!dbCat.getName().equals(UNCATEGORIZED_CATEGORY_NAME) && !c.getCategory().getName().equals(dbCat.getName())) {
                c.setCategory(dbCat);
            }
            certRepo.save(c);
        }
        return c;
    }

   
   

    @RequestMapping(value = "/category", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result createCategory(Principal user, @RequestParam(value = "name", required = false) String name, @RequestParam("label") String label, @RequestParam(value = "origLabel", required = false) String origLabel) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            if (origLabel == null) {
                origLabel = label;
            }
            Category c = createCategory(name, label, u.getCompany());
            return Result.ok(c);
        } else {
            return Result.error(null, "Unknown User");
        }
    }

    //method to remove user for company
    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity removeUser(Principal user, @RequestParam("user") String userToremove, final RedirectAttributes redirectAttributes) {

        //make sure the company has atleast 1 user
        User x = userRepo.findByEmail(user.getName());
        Company C = x.getCompany();
        List<User> users = userRepo.findByCompany((C));

        if (users.size() > 1) {
            //remove the user

            User u = userRepo.findByEmail(userToremove);
            //only remove if they are not trying to remove themself
            if (!x.getEmail().equalsIgnoreCase(u.getEmail())) {

               

                //remove user from all candiates
                List<Candidate> candidates = candidateRepo.findByOwnerCompanyOrderByFormattedNameAsc(C);
                for (Candidate candiate : candidates) {
                    if (candiate.getInterestedUsers() != null) {
                        List<User> names = candiate.getInterestedUsers();
                        Iterator<User> i = names.iterator();

                        while (i.hasNext()) {
                            User instrested_user = i.next();
                            if (u.getId().equalsIgnoreCase(instrested_user.getId())) {
                                i.remove();
                            }
                            
                        

                        }
                        
                        
                        candiate.setInterestedUsers(names);
                        candidateRepo.save(candiate);

                    }

                }

                userRepo.delete(u);
                return new ResponseEntity<>(HttpStatus.FOUND);
                //return Result.ok(null, "Removed user successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("errMessage", REMOVE_YOURSELF_USER_ERROR);
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
                //return Result.error(null, "Can't remove yourself");
            }
        } else {
            redirectAttributes.addFlashAttribute("errMessage", REMOVE_ONLY_USER_ERROR);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            //throw an error because a company has to have one user
            //return Result.error(null, "Must have atleast one user");

        }

        //return Result.error(null, "Unknown User");
        //return null;
    }


    public Category createCategory(String name, String label, Company company) {
        return createCategory(name, label, label, company);
    }

    public Category createCategory(String name, String label, String origLabel, Company company) {
        if (name == null) {
            name = origLabel.toLowerCase().replaceAll(" ", "-");
        }
        Category c = categoryRepo.findByNameAndOwnerCompany(name.toLowerCase().trim(), company);
        if (c == null) {
            log.info("Creating new Category [ " + name + ", " + label + " ]");
            c = new Category();
            c.setName(name.toLowerCase().trim());
            if (label == null || label.isEmpty()) {
                label = name;
            }
            c.setDisplayName(label.trim());
            c.setOwnerCompany(company);

            categoryRepo.save(c);
        } else if (!c.getName().equals(UNCATEGORIZED_CATEGORY_NAME)) { //don't allow changes to Uncategorized Category. 
            if (!c.getName().equals(name)) {
                c.setName(name);
            }
            if (!c.getDisplayName().equals(label)) {
                c.setDisplayName(label);
                c.setName(label.toLowerCase().replaceAll(" ", "-"));
            }
            categoryRepo.save(c);
        }
        return c;
    }

    public List<Competency> getSkillsList(String[] list, Company company) {
        if (list != null && list.length > 0) {
            List<Competency> items = new ArrayList<>();
            for (String item : list) {
                Competency c = new Competency();
                Skill s = skillRepo.findByNameAndOwnerCompany(item, company);
                if (s != null) {
                    c.setCompetencyID(s.getName());
                    c.setCompetencyName(s.getDisplayName());
                } else {
                    c.setCompetencyID(item);
                    c.setCompetencyName(item);
                }
                items.add(c);
            }
            return items;
        } else {
            return null;
        }
    }

    public List<NameAndCode> getCertificationListAsNameAndCode(String[] list, Company company) {
        if (list != null && list.length > 0) {
            List<NameAndCode> items = new ArrayList<>();
            for (String item : list) {
                NameAndCode c = new NameAndCode();
                Certification cert = certRepo.findByNameAndOwnerCompany(item, company);
                if (cert != null) {
                    c.setCode(cert.getName());
                    c.setName(cert.getDisplayName());
                } else {
                    c.setCode(item);
                    c.setName(item);
                }
                items.add(c);
            }
            return items;
        } else {
            return null;
        }
    }

    public List<Certification> getCertificationList(String[] list, Company company) {
        if (list != null && list.length > 0) {
            List<Certification> items = new ArrayList<>();
            for (String item : list) {
                NameAndCode c = new NameAndCode();
                Certification cert = certRepo.findByNameAndOwnerCompany(item, company);
                items.add(cert);
            }
            return items;
        } else {
            return null;
        }
    }

    private List<State> stateList = null;

    public List<State> getStateList() {
        if (stateList == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                InputStream is = getClass().getClassLoader().getResourceAsStream("states.json");
                stateList = mapper.readValue(is, new TypeReference<List<State>>() {
                });
            } catch (Exception e) {
                log.warn("Unable to get State List", e);
            }
        }

        return stateList;

    }

    /**
     * This method takes a name and converts it to a short name that can be used
     * in a URL. Ex: Software Engineer -> software-engineer
     *
     * @param name
     * @return A URL compliant shortname in all lowercase
     */
    public String convertToShortName(String name) {
        return name.toLowerCase().replaceAll(" ", "-").replaceAll("/", "_").replaceAll("\\.", "_");
    }
}
