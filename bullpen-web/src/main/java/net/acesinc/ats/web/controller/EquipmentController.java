package net.acesinc.ats.web.controller;

import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.equipment.Equipment;
import net.acesinc.ats.model.equipment.ProductLicense;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.data.UploadFile;
import net.acesinc.ats.web.data.UploadResponse;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.EquipmentRepository;
import net.acesinc.ats.web.repository.ProductLicenseRepository;
import net.acesinc.ats.web.repository.UserRepository;
import net.acesinc.ats.web.service.BoxViewerService;
import net.acesinc.ats.web.service.box.BoxSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Myles on 1/10/17.
 */

@Controller
public class EquipmentController {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentRepository equipmentRepo;
    @Autowired
    private ProductLicenseRepository licenseRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UploadController uploadController;
    @Autowired
    private CandidateController candidateController;
    @Autowired
    private BoxViewerService boxService;


    @RequestMapping(value = {"/equipment"}, method = RequestMethod.GET)
    public String getEquipmentPage(Principal user, ModelMap model){

        User u = userRepo.findByEmail(user.getName());
        //constants.populateModel(u.getCompany(), model);
        model.addAttribute("pageName", "Equipment");
        List<Equipment> equipmentList = equipmentRepo.findByCompany(u.getCompany());
        List<ProductLicense> lcList = licenseRepo.findByCompany(u.getCompany());
        Equipment currentEquip = null;

        model.addAttribute("currentEquip",currentEquip);
        model.addAttribute("equipmentList",equipmentList);
       model.addAttribute("lcList",lcList);
        return "equipment";
    }


    @RequestMapping(value={"/equipment/add"}, method = RequestMethod.POST)
    public String addEquipment(Principal user,@RequestParam("type") String type, @RequestParam("itemModel") String itemModel, @RequestParam("serial") String serial, @RequestParam(value = "datePurchasedString", required = false) String datePurchaseString,ModelMap model, final RedirectAttributes redirectAttributes){

        LOG.debug("Preparing to add a new item to equipment list");
        User u = userRepo.findByEmail(user.getName());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date datePurchased = null;

        Equipment e = null;
        e= new Equipment();

        e.setCompany(u.getCompany());
        e.setType(type);

        if(StringUtils.isBlank(itemModel)){
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "Please enter an item model.");
            return "redirect:/equipment";
        }else{
            itemModel = WordUtils.capitalizeFully(itemModel);
            e.setModel(itemModel);
        }

        if(StringUtils.isBlank(serial)){
            e.setSerial("N/A");
        }else{
            serial = serial.toUpperCase();
            e.setSerial(serial);
        }

        try {
            datePurchased = df.parse(datePurchaseString);
            e.setDatePurchased(datePurchased);

        } catch (ParseException err) {
            if(!StringUtils.isBlank(datePurchaseString))
            {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("errMessage", "The date of purchase must be in this format: MM/DD/YYY.");
                LOG.debug(err.toString());
                return "redirect:/equipment";
            }
        }

        equipmentRepo.save(e);

        return "redirect:/equipment";
    }

    @RequestMapping(value={"/license/add"}, method = RequestMethod.POST)
    public String addLicense(Principal user,@RequestParam("productName") String productName, @RequestParam("licenseKey") String licenseKey, @RequestParam(value = "expirationDateString", required = false) String expirationDateString,ModelMap model, final RedirectAttributes redirectAttributes){

        LOG.debug("Adding new license ");
        User u = userRepo.findByEmail(user.getName());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date expirationDate = null;

        ProductLicense lc = null;
        lc = new ProductLicense();

        lc.setCompany(u.getCompany());

        if(StringUtils.isBlank(productName)){
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "You must enter a product name to add a new license.");
            return "redirect:/equipment";
        }else{
            productName = WordUtils.capitalizeFully(productName);
            lc.setProductName(productName);
        }

        if(StringUtils.isBlank(licenseKey)){
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "You did not provide a license key for this product.");
            return "redirect:/equipment";
        }else{
            licenseKey = licenseKey.toUpperCase();
            lc.setLicenseKey(licenseKey);
        }

        try {
            expirationDate = df.parse(expirationDateString);
            lc.setExpirationDate(expirationDate);

        } catch (ParseException err) {
            if(!StringUtils.isBlank(expirationDateString))
            {
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("errMessage", "The expiration date must be in this format: MM/DD/YYY.");
                LOG.debug(err.toString());
                return "redirect:/equipment";
            }
        }
        licenseRepo.save(lc);

        return "redirect:/equipment";
    }

    @RequestMapping(value = {"/equipment/{id}"}, method = RequestMethod.GET)
    public String getAssignPage(Principal user,@PathVariable("id") String id ,ModelMap model,final RedirectAttributes redirectAttributes){
        User u = userRepo.findByEmail(user.getName());

        Equipment e = equipmentRepo.findById(id);

        if(e!=null){
            LOG.debug("Viewing assign page for " +e.getModel() );

            if (e.getAgreement() != null) {
                model.addAttribute("agreement", e.getAgreement());
                String boxId = candidateController.getBoxIdForStoredFile(e.getAgreement().getStorageId());
                if (boxId != null) {
                    BoxSessionResponse boxSession = boxService.createBoxViewerSession(boxId);
                    if (boxSession != null) {
                        model.addAttribute("agreementViewUrl", boxSession.getUrls().getView());
                    }
                }
            }
            model.addAttribute("equipment",e);
            model.addAttribute("pageName", "Assign Equipment");
            return "equipment-assign";
        }else{
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "That equipment ID does not exist.");
            return "redirect:/equipment";
        }

    }

    //**
    @RequestMapping(value={"/equipment/remove/{id}"},method = RequestMethod.POST)
    public String removeItem(Principal user,@PathVariable("id") String id ,ModelMap model,final RedirectAttributes redirectAttributes){
        Equipment e = equipmentRepo.findById(id);
        User u = userRepo.findByEmail(user.getName());
        if(u.getAuthority().equalsIgnoreCase("user")){
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "You are not authorized to delete equipment.");
            return "redirect:/equipment";
        }else{
            if (e != null){
                equipmentRepo.delete(e);

            }else{
                redirectAttributes.addFlashAttribute("error",true);
                redirectAttributes.addFlashAttribute("errMessage", "Unable to find equipment id");
                return "redirect:/equipment";
            }

            return "redirect:/equipment";
        }


    }

    @RequestMapping(value={"/equipment/assign/{id}"},method = RequestMethod.POST)
    public String assignEquipment(Principal user, @PathVariable("id") String id, @RequestParam(value = "equipment-agreement", required = false) MultipartFile file,@RequestParam(value="first",required = false) String first,@RequestParam(value="last",required=false) String last,@RequestParam("mainUse") String mainUse,ModelMap model,final RedirectAttributes redirectAttributes){

        Equipment e = equipmentRepo.findById(id);
        if(e != null){
            if(StringUtils.isBlank(first)||StringUtils.isBlank(last)){
                redirectAttributes.addFlashAttribute("error",true);
                redirectAttributes.addFlashAttribute("errMessage", "Please enter the borrower's full name.");
                return "redirect:/equipment/"+id;
            }else{
                e.setAssignedTo(first +" " +last);
            }


            if (file.getSize()>0){
                UploadResponse upResp = uploadController.handleFileUpload(user, file);
                UploadFile uf = upResp.getFiles().get(0);
                StoredFile storedFile = candidateController.getStoredFile(uf);
                e.setAgreement(storedFile);
                LOG.debug("saved agreement file " +uf.getName() +"/" +storedFile.getFilename());
            }else{
                redirectAttributes.addFlashAttribute("error",true);
                redirectAttributes.addFlashAttribute("errMessage", "You must attach a signed agreement for this item.");
                return "redirect:/equipment/"+id;
            }

            e.setDateAssigned(new Date());
            e.setUsedFor(mainUse);
            equipmentRepo.save(e);


            return "redirect:/equipment";
        }else{
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "That equipment ID does not exist.");
            return "redirect:/equipment";
        }
    }

    @RequestMapping(value={"/equipment/clear/{id}"},method = RequestMethod.POST)
    public String removeAssignment(Principal user, @PathVariable("id") String id,ModelMap model,final RedirectAttributes redirectAttributes){

        Equipment e = equipmentRepo.findById(id);
        User u = userRepo.findByEmail(user.getName());

        if(u.getAuthority().equalsIgnoreCase("user")) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("errMessage", "You are not authorized to do this.");
            return "redirect:/equipment";
        }else{

            if(e != null){
                e.setAssignedTo(null);
                e.setUsedFor(null);
                e.setAgreement(null);
                equipmentRepo.save(e);

                return "redirect:/equipment";
            }else{
                redirectAttributes.addFlashAttribute("error",true);
                redirectAttributes.addFlashAttribute("errMessage", "Unable to find equipment id");
                return "redirect:/equipment";
            }
        }
    }

    @RequestMapping(value={"/license/remove/{id}"},method = RequestMethod.POST)
    public String removeLicense(Principal user,@PathVariable("id") String id ,ModelMap model,final RedirectAttributes redirectAttributes){
        ProductLicense lc = licenseRepo.findById(id);
        User u = userRepo.findByEmail(user.getName());
        if(u.getAuthority().equalsIgnoreCase("user")){
            redirectAttributes.addFlashAttribute("error",true);
            redirectAttributes.addFlashAttribute("errMessage", "You are not authorized to delete licenses.");
            return "redirect:/equipment";
        }else{
            if (lc != null){
                licenseRepo.delete(lc);

            }else{
                redirectAttributes.addFlashAttribute("error",true);
                redirectAttributes.addFlashAttribute("errMessage", "Unable to find license id");
                return "redirect:/equipment";
            }
            return "redirect:/equipment";
        }
    }
}
