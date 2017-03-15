/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.company.CompanyInvite;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.data.EmailVerifyRequest;
import net.acesinc.ats.web.data.PasswordResetRequest;
import net.acesinc.ats.web.data.Result;
import net.acesinc.ats.web.repository.CompanyInviteRepository;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.EmailVerifyRequestRepository;
import net.acesinc.ats.web.repository.PasswordResetRequestRepository;
import net.acesinc.ats.web.repository.UserRepository;
import net.acesinc.ats.web.service.EmailService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author andrewserff
 */
@Controller
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private CompanyInviteRepository companyInviteRepo;
    @Autowired
    private PasswordResetRequestRepository passwordResetRepo;
    @Autowired
    private EmailVerifyRequestRepository emailVerifyRepo;
    @Autowired
    private ConstantsController constants;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CompanyController companyController;
    @Value("${ats.registrationMode}")
    private String registrationMode;
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegistrationPage(@RequestParam(value = "companyInvite", required = false) String companyInvite, ModelMap model) {
        if (companyInvite != null && !companyInvite.isEmpty()) {
            CompanyInvite c = companyInviteRepo.findOne(companyInvite);
            if (c != null) {
                model.addAttribute("company", c.getInvitedToCompany());
                model.addAttribute("email", c.getEmailInvited());
                model.addAttribute("invite", c);
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errMessage", "Invalid Invite Code specified");
            }
        }
        model.addAttribute("states", constants.getStateList());
        model.addAttribute("pageName", "Register");
        model.addAttribute("registrationMode", registrationMode);
        model.addAttribute("currentYear", yearFormat.format(new Date()));

        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestParam("email") String email, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("password") String password, @RequestParam("password_confirm") String password_confirm,
            @RequestParam("companyName") String companyName, @RequestParam("companyAddress") String companyAddress, @RequestParam("companyCity") String companyCity,
            @RequestParam("companyState") String companyState, @RequestParam("companyZipcode") String companyZip, @RequestParam(value = "invite", required = false) String invite, ModelMap model, @RequestParam(value = "agreeterms", required = false) String agreeTerms) {

        model.addAttribute("states", constants.getStateList());// add state list if page needs to be reloaded

        boolean error = false;

        if (agreeTerms == null) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Please accept the terms of use");

            model.addAttribute("password", password);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email_notInvite", email);

            error = true;
        }
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Please provide a password");

            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email_notInvite", email);

            error = true;
        }
        if (!(password.equals(password_confirm))) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Passwords do not match");

            model.addAttribute("password", password);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email_notInvite", email);

            error = true;
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Please provide your First Name");

            model.addAttribute("password", password);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email_notInvite", email);

            error = true;
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Please provide your Last Name");

            model.addAttribute("password", password);
            model.addAttribute("firstName", firstName);
            model.addAttribute("email_notInvite", email);

            error = true;
        }
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Please provide your email");

            model.addAttribute("password", password);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);

            error = true;
        }

        if (registrationMode == null || "closed".equalsIgnoreCase(registrationMode)) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Sorry, registrations are currently closed. Please try later!");
            return "register";
        }

        CompanyInvite i = null;

        if (invite != null && !invite.isEmpty()) {
            i = companyInviteRepo.findOne(invite);
        }
        
        if(i!=null && error)
        {
             model.addAttribute("invite_error", i.getId());
        }
        
        if (error) {
            return "register";
        }

        //if they are using an invite, we should verify their email is what was in the invite
        Company company = null;

        if (invite != null && !invite.isEmpty()) {
            i = companyInviteRepo.findOne(invite);
            if (i != null) {
                if (!i.getEmailInvited().equals(email)) {
                    model.addAttribute("error", true);
                    model.addAttribute("errMessage", "Email Address does not match the email you were invited with");
                    return "register";
                } else {
                    //ok to proceed
                    company = i.getInvitedToCompany();
                }

            } else {
                model.addAttribute("error", true);
                model.addAttribute("errMessage", "You are attempting to use an invalid invite code");
                return "register";
            }
        } else {
            //They have no invite, make sure we are allowing that
            if ("inviteOnly".equalsIgnoreCase(registrationMode)) {
                model.addAttribute("error", true);
                model.addAttribute("errMessage", "Sorry, we are only allowing registrations by invite only right now. If your company is already using PolarisATS, please ask your administrator to add you!");
                return "register";
            }
            if (companyName != null && !companyName.isEmpty()) {

                company = companyRepo.findByNameIgnoreCase(companyName);
                if (company != null) {
                    model.addAttribute("error", true);
                    model.addAttribute("errMessage", "Company " + companyName + " already exists.  Please ask your administrator to add you.");
                    return "register";
                } else {
                    company = new Company();
                    company.setName(companyName);
                    try {
                        company.setShortName(companyController.getDefaultShortName(companyName));
                    } catch (UnsupportedEncodingException ex) {
                        log.debug("Error generating Short Name for Company [ " + companyName + " ]", ex);
                    }
                    Address a = new Address();
                    a.setAddressLine(companyAddress);
                    a.setCityName(companyCity);
                    a.setState(companyState);
                    a.setPostalCode(companyZip);
                    a.setLocationName(companyName);

                    company.setPrimaryAddress(a);

                    companyRepo.save(company);

                    constants.createCategory(ConstantsController.UNCATEGORIZED_CATEGORY_NAME, ConstantsController.UNCATEGORIZED_CATEGORY_LABEL, company);
                }
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errMessage", "You must provide a Company Name in order to register a new account.");
                return "register";
            }
        }

        User test = userRepo.findByEmail(email);
        if (test != null) {
            log.warn("User tried to register with email that already has an account");
            model.addAttribute("error", true);
            if (test.isEnabled()) {
                model.addAttribute("errMessage", "Email Address is already registered.  Did you forget your password?");
            } else {
                model.addAttribute("errMessage", "Email Address is already registered but has not been verified");
            }
            return "register";
        } else {

            User u = new User();

            //set the company they belong to
            u.setCompany(company);

            u.setEmail(email);
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setFullName(u.getFirstName() + " " + u.getLastName());
            u.setPassword(encodePassword(password));
            u.setEmailMD5Hash(DigestUtils.md5Hex(email));
            if(i != null && i.getRole() != null)
            u.setAuthorities(i.getRole());
            else
            u.setAuthorities("ROLE_ADMIN");//means they are the first company user
//        u.setEnabled(true); //accounts are enabled after email verification

            log.info("Registering new user [ " + u.getUsername() + ", " + password + " ]");
            userRepo.save(u);
            log.info("New User [ " + u.getUsername() + " ] saved with id [ " + u.getId() + "]");

            //update the invite
            if (i != null) {
                i.setUsedByUser(u);
                i.setDateAccepted(new Date());
                companyInviteRepo.save(i);
            }

            emailService.sendWelcomeEmail(u);

            model.addAttribute("message", "Please check your email to verify your account");

            return "login";
        }
    }

    @RequestMapping(value = "/email/verify")
    public String verifyEmail(@RequestParam("email") String email, @RequestParam("verifyToken") String verifyToken, ModelMap model) {
        EmailVerifyRequest req = emailVerifyRepo.findByIdAndEmail(verifyToken, email);
        if (req != null) {
            log.info("Successful verification of email [ " + email + " ]. Enabling account. ");
            User u = userRepo.findByEmail(email);
            u.setEnabled(true);
            userRepo.save(u);

            emailVerifyRepo.delete(req);
            model.addAttribute("message", "Email successfully verified.  Please log in!");
            return "login";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Email verification token is invalid");
            return "login";
        }
    }

    @RequestMapping(value = "/company/invite", method = RequestMethod.GET)
    public String getInviteUserPage(Principal user, ModelMap model) {
        model.addAttribute("pageName", "Invite");
        return "company-invite";
    }

    @RequestMapping(value = "/company/invite", method = RequestMethod.POST)
    public String createInvite(Principal user, @RequestParam("emailToInvite") String emailToInvite, @RequestParam("premission") String premission, ModelMap model) {
        User test = userRepo.findByEmail(emailToInvite);
        if (test != null) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "A user account already exists for email address " + emailToInvite);
            return "company-invite";
        }

        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            CompanyInvite i = companyInviteRepo.findByEmailInvited(emailToInvite);
            if (i == null) {

                i = new CompanyInvite();
                i.setInvitedToCompany(u.getCompany());
                i.setUserWhoInvited(u);
                i.setDateCreated(new Date());
                i.setEmailInvited(emailToInvite);
                i.setRole(premission);

                companyInviteRepo.save(i);
                log.info("Created new Company Invite for email [ " + emailToInvite + " ] for Company [ " + u.getCompany().getId() + " ] with id [ " + i.getId() + " ]");
            } else {
                log.debug("Reusing unused invite for email [ " + emailToInvite + " ]");
            }
            emailService.sendCompanyInviteEmail(i);
            model.addAttribute("message", "Successfully sent Company Invitation to " + emailToInvite);
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Unable to locate your user profile");
        }
        return "company-invite";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfilePage(Principal user, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        model.addAttribute("user", u);
        model.addAttribute("role", u.getAuthority());
        log.info("retruning profile for " + u.getFirstName() + " " + u.getLastName() + " " + u.getEmail());
        model.addAttribute("pageName", "Profile");
        return "profile";
    }

    //FIXME spelling and should be a POST potentially
    @RequestMapping(value = "/setCompany/{name}", method = RequestMethod.GET)
    public String setCompnay(Principal user, ModelMap model, @PathVariable("name") String companyName) {
        User u = userRepo.findByEmail(user.getName());
        u.setCompany(companyRepo.findByNameIgnoreCase(companyName));
        userRepo.save(u);
        return "redirect:/company/profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String updateProfilePage(Principal user, @RequestParam("email") String email, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("password") String password, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            if (firstName != null && !firstName.isEmpty()) {
                u.setFirstName(firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                u.setLastName(lastName);
            }
            u.setFullName(u.getFirstName() + " " + u.getLastName());

            if (email != null && !email.isEmpty() && !email.equalsIgnoreCase(u.getEmail())) {
                //you want to change your email?  Then disable you until you verify your new email again!
                emailService.sendWelcomeEmail(u);
                u.setEnabled(false);
                u.setEmail(email);
                u.setEmailMD5Hash(DigestUtils.md5Hex(email));
            }

            if (password != null && !password.isEmpty()) {
                u.setPassword(encodePassword(password));
            }

            userRepo.save(u);
            model.addAttribute("user", u);
            model.addAttribute("message", "Successfully updated your profile.  Note if you changed your email, you must verify your email before you can login again");
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Profile update failed. Unable to locate user account for name " + user.getName());
        }

        return "profile";
    }

    private String encodePassword(String password) {
        //encode the password
        String encodedPassword = encoder.encode(password);
        log.debug("Encoded password [ " + password + " ] to [ " + encodedPassword + " ]");
        return encodedPassword;
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String getPasswordResetPage() {
        return "password-reset";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    public String requestPasswordReset(@RequestParam("email") String email, ModelMap model) {
        User test = userRepo.findByEmail(email);
        if (test == null) {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "No account with email " + email + " exists");
        } else {
            PasswordResetRequest req = passwordResetRepo.findByEmail(email);
            if (req == null) {
                req = new PasswordResetRequest();
                req.setEmail(email);
                passwordResetRepo.save(req);

            } else {
                //TODO Auditing/Fraud protection
                log.debug("User re-requested password reset for email [ " + email + " ]");
            }
            model.addAttribute("message", "Instructions have been sent to your email: " + email);

            emailService.sendPasswordResetEmail(req);
        }
        return "password-reset";
    }

    @RequestMapping(value = "/password/reset/complete", method = RequestMethod.GET)
    public String getPasswordResetCompletePage(@RequestParam("email") String email, @RequestParam("resetToken") String resetToken, ModelMap model) {
        model.addAttribute("email", email);
        model.addAttribute("resetToken", resetToken);
        return "password-reset-complete";
    }

    @RequestMapping(value = "/password/reset/complete", method = RequestMethod.POST)
    public String completePasswordReset(@RequestParam("email") String email, @RequestParam("resetToken") String resetToken, @RequestParam("password") String newPassword, ModelMap model) {
        PasswordResetRequest req = passwordResetRepo.findByIdAndEmail(resetToken, email);
        if (req != null) {
            log.info("Completing password reset for email [ " + email + " ]");
            User u = userRepo.findByEmail(email);
            u.setPassword(encodePassword(newPassword));
            userRepo.save(u);

            passwordResetRepo.delete(req);
            return "redirect:/login";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "Invalid resetToken was provided.  Password update failed");
            return "password-reset";
        }

    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getUsers(Principal user, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "firstName", required = false) String firstName, @RequestParam(value = "lastName", required = false) String lastName, @RequestParam(value = "company", required = false) String company) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            List<User> users = new ArrayList<>();
            List<User> companyOnlyUsers = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                users = userRepo.findByFullNameRegex(name);
            } else if (firstName != null && !firstName.isEmpty()) {
                users = userRepo.findByFirstNameRegex(firstName);
            } else if (lastName != null && !lastName.isEmpty()) {
                users = userRepo.findByLastNameRegex(lastName);
            } else if (company != null && !company.isEmpty()) {
                Company c = companyRepo.findByNameIgnoreCase(company);
                if (c != null) {
                    return Result.ok(userRepo.findByCompany(c));
                } else {
                    return Result.error(null, "No company by name " + company + " could not be found.");
                }
            } else {
                return Result.error(null, "Invalid query specified. You must specify [ firstName | lastName | name | company ]");
            }

            //this is a hacky workaround because spring data mongo repositories don't support case insensitive regex queries and I can't get DBRef queries to work with @Query either. So we are filtering out users in the application
            for (User test : users) {
                if (test.getCompany().getId().equals(u.getCompany().getId())) {
                    companyOnlyUsers.add(test);
                }
            }
            return Result.ok(companyOnlyUsers);
        } else {
            return Result.error(null, "Unable to verify User Profile");
        }
    }
}
