/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.converter.candidate;

import java.util.ArrayList;
import java.util.List;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.common.Competency;
import net.acesinc.ats.model.common.ContactType;
import net.acesinc.ats.model.candidate.EducationalOrganization;
import net.acesinc.ats.model.common.Email;
import net.acesinc.ats.model.candidate.License;
import net.acesinc.ats.model.common.NameAndCode;
import net.acesinc.ats.model.common.Phone;
import net.acesinc.ats.model.candidate.Position;
import net.acesinc.ats.model.common.Website;
import net.acesinc.ats.model.common.WebsiteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author andrewserff
 */
public class HRXML25CandidateConverter extends BaseXMLConverter {

    private static final Logger log = LoggerFactory.getLogger(HRXML25CandidateConverter.class);

    @Override
    public Candidate getCandidateForDocument(Document doc) {
        log.debug("Getting Candidate for doc");
        NodeList rootChildren = doc.getChildNodes();
        Node resumeNode = getNode("Resume", rootChildren);
        if (resumeNode != null) {
            Node xmlResume = getNode("StructuredXMLResume", resumeNode.getChildNodes());
            if (xmlResume != null) {
                Candidate c = new Candidate();
                Node contactInfo = getNode("ContactInfo", xmlResume.getChildNodes());
                c = populateContactInfo(contactInfo, c);
                Node employmentHistory = getNode("EmploymentHistory", xmlResume.getChildNodes());
                c.setPositionHistory(getPositions(employmentHistory));
                Node educationHistory = getNode("EducationHistory", xmlResume.getChildNodes());
                c.setEducationalOrganizations(getEducation(educationHistory));
                Node qualifications = getNode("Qualifications", xmlResume.getChildNodes());
                c.setPersonCompetencies(getCompetencies(qualifications));
                Node licenses = getNode("LicensesAndCertifications", xmlResume.getChildNodes());
                c.setLicenses(getLicenses(licenses));

                return c;
            }
        }
        return null;
    }

    protected Candidate populateContactInfo(Node contactInfo, Candidate c) {
        c = getPersonInfo(contactInfo, c);
        List<Node> contactMethods = getNodes("ContactMethod", contactInfo.getChildNodes());
        if (contactMethods != null) {
            List<Address> addresses = new ArrayList<>();
            List<Phone> phones = new ArrayList<>();
            List<Email> emails = new ArrayList<>();
            List<Website> websites = new ArrayList<>();

            for (Node contactMethod : contactMethods) {
                addresses.addAll(getAddressInfo(contactMethod));
                phones.addAll(getPhoneInfo(contactMethod));
                emails.addAll(getEmailInfo(contactMethod));
                websites.addAll(getWebInfo(contactMethod));
            }

            if (!addresses.isEmpty()) {
                c.setAddress(addresses);
            }
            if (!phones.isEmpty()) {
                c.setPhone(phones);
            }
            if (!emails.isEmpty()) {
                c.setEmail(emails);
            }
            if (!websites.isEmpty()) {
                c.setWeb(websites);
            }
        }

        return c;
    }

    protected Candidate getPersonInfo(Node contactInfo, Candidate c) {
        Node person = getNode("PersonName", contactInfo.getChildNodes());
        if (person != null) {
            c.setFormattedName(getNodeValue(getFormattedNameNodeName(), person.getChildNodes()));
            c.setGivenName(getNodeValue(getGivenNameNodeName(), person.getChildNodes()));
            c.setFamilyName(getNodeValue(getFamilyNameNodeName(), person.getChildNodes()));
        }
        return c;
    }

    protected ContactType getContactTypeFromUse(Node use) {
        if (use != null) {
            ContactType type = null;
            try {
                type = ContactType.valueOf(getNodeValue(use));
            } catch (Exception e) {
                log.debug("Unknown Use Type [ " + getNodeValue(use) + " ]");
            }
            return type;
        }
        return null;
    }

    protected List<Address> getAddressInfo(Node contactMethod) {
        Node postal = getNode("PostalAddress", contactMethod.getChildNodes());
        Node use = getNode("Use", contactMethod.getChildNodes());
        List<Address> addresses = new ArrayList<>();
        if (postal != null) {

            Address a = new Address();
            a.setCountryCode(getNodeValue("CountryCode", postal.getChildNodes()));
            a.setPostalCode(getNodeValue("PostalCode", postal.getChildNodes()));
            a.setCityName(getNodeValue(getCityNameNodeName(), postal.getChildNodes()));
            a.setState(getNodeValue("Reigon", postal.getChildNodes()));

            Node deliveryAddress = getNode("DeliveryAddress", postal.getChildNodes());
            if (deliveryAddress != null) {
                String addressLine1 = getNodeValue("AddressLine", deliveryAddress.getChildNodes());
                String addressLine2 = getNodeValue("AddressLine2", deliveryAddress.getChildNodes());
                if (addressLine1 != null && !addressLine1.trim().isEmpty()) {
                    String address = addressLine1;
                    if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
                        address += ", " + addressLine2.trim();
                    }
                    a.setAddressLine(address);
                }
            }

            ContactType type = getContactTypeFromUse(use);
            if (type != null) {
                a.setLabel(type.toString());
            }

            if (a.getCountryCode() != null || a.getAddressLine() != null || a.getCityName() != null || a.getCountrySubDivisionCode() != null || a.getLabel() != null || a.getPostalCode() != null || a.getState() != null) {
                addresses.add(a);
            }
        }
        return addresses;
    }

    protected Phone getPhoneFromNode(Node phone, String childNodeName, ContactType type) {
        if (phone != null) {
            String number = getNodeValue(childNodeName, phone.getChildNodes());
            if (number != null && !number.trim().isEmpty()) {
                Phone p = new Phone();
                p.setNumber(number);
                p.setLabel(type);
                return p;
            }
        }
        return null;
    }

    protected List<Phone> getPhoneInfo(Node contactMethod) {
        List<Phone> phones = new ArrayList<>();
//        Node use = getNode("Use", contactMethod.getChildNodes());
//        ContactType type = getContactTypeFromUse(use);

        Phone tel = getPhoneFromNode(getNode("Telephone", contactMethod.getChildNodes()), getPersonalPhoneNodeName(), ContactType.personal);
        if (tel != null) {
            phones.add(tel);
        }

        Phone f = getPhoneFromNode(getNode("Fax", contactMethod.getChildNodes()), getFaxNodeName(), ContactType.fax);
        if (f != null) {
            phones.add(f);
        }
        Phone mobile = getPhoneFromNode(getNode("Mobile", contactMethod.getChildNodes()), getMobilePhoneNodeName(), ContactType.mobile);
        if (mobile != null) {
            phones.add(mobile);
        }

        return phones;
    }

    protected List<Email> getEmailInfo(Node contactMethod) {
        List<Node> emailNodes = getNodes("InternetEmailAddress", contactMethod.getChildNodes());
        Node use = getNode("Use", contactMethod.getChildNodes());
        ContactType type = getContactTypeFromUse(use);
        List<Email> emails = new ArrayList<>();
        if (emailNodes != null) {

            for (Node email : emailNodes) {
                if (email != null) {

                    Email e = new Email();
                    String emailAddr = getNodeValue(email);
                    if (emailAddr != null) {
                        e.setAddress(emailAddr);
                        e.setLabel(type);
//                    e.setPreferred(PreferenceType.primary);
                        emails.add(e);
                    }

                }
            }

        }
        return emails;
    }

    protected WebsiteType getWebsiteTypeFromUse(Node use) {
        if (use != null) {
            WebsiteType type = null;
            try {
                type = WebsiteType.valueOf(getNodeValue(use));
            } catch (Exception e) {
                log.debug("Unknown Use Type [ " + getNodeValue(use) + " ]");
            }
            return type;
        }
        return null;
    }

    protected List<Website> getWebInfo(Node contactMethod) {
        Node website = getNode("InternetWebAddress", contactMethod.getChildNodes());
        Node use = getNode("Use", contactMethod.getChildNodes());
        WebsiteType type = getWebsiteTypeFromUse(use);
        List<Website> websites = new ArrayList<>();
        if (website != null) {
            Website w = new Website();
            String websiteAddr = getNodeValue(website);
            if (websiteAddr != null) {
                w.setAddress(getNodeValue(website));
                w.setLabel(type);
                websites.add(w);
            }
        }
        return websites;
    }

    protected String getFormattedNameNodeName() {
        return "FormattedName";
    }

    protected String getGivenNameNodeName() {
        return "GivenName";
    }

    protected String getFamilyNameNodeName() {
        return "FamilyName";
    }

    protected String getCityNameNodeName() {
        return "Municipality";
    }

    protected String getPersonalPhoneNodeName() {
        return "FormattedNumber";
    }

    protected String getFaxNodeName() {
        return "FormattedNumber";
    }

    protected String getMobilePhoneNodeName() {
        return "FormattedNumber";
    }

    protected String getDateFromNode(Node node) {
        if (node != null) {
            return getNodeValue("AnyDate", node.getChildNodes());
        }
        return null;
    }

    protected List<Position> getPositions(Node employmentHistory) {
        List<Position> positions = new ArrayList<>();
        if (employmentHistory != null) {
            List<Node> employers = getNodes("EmployerOrg", employmentHistory.getChildNodes());
            if (employers != null) {
                for (Node emp : employers) {
                    Position p = new Position();
                    p.setEmployer(getNodeValue("EmployerOrgName", emp.getChildNodes()));
                    Node posHist = getNode("PositionHistory", emp.getChildNodes());
                    if (posHist != null) {
                        p.setPositionTitle(getNodeValue("Title", posHist.getChildNodes()));
                        p.setStartDate(getDateFromNode(getNode("StartDate", posHist.getChildNodes())));
                        p.setEndDate(getDateFromNode(getNode("EndDate", posHist.getChildNodes())));
                        p.setDescription(getNodeValue("Description", posHist.getChildNodes()));
                        p.setOrganizationUnitName(getNodeValue("OrganizationName", getNode("OrgName", posHist.getChildNodes()).getChildNodes()));
                        
                        Node orgInfo = getNode("OrgInfo", posHist.getChildNodes());
                        if (orgInfo != null) {
                            p.setReferenceLocation(getAddressFromNode(getNode("PositionLocation", orgInfo.getChildNodes())));
                        }
                        
                        List<Node> orgIndNodes = getNodes("OrgIndustry", posHist.getChildNodes());
                        if (orgIndNodes != null) {
                            List<NameAndCode> industries = new ArrayList<>();
                            for (Node orgInd : orgIndNodes) {
                                String indDesc = getNodeValue("IndustryDescription", orgInd.getChildNodes());
                                if (indDesc != null) {
                                    NameAndCode i = new NameAndCode();
                                    i.setName(indDesc);
                                    industries.add(i);
                                }
                            }

                            if (!industries.isEmpty()) {
                                p.setIndustry(industries);
                            }
                        }

                    }
                    positions.add(p);
                }
            }
        }
        return positions;
    }

    protected List<EducationalOrganization> getEducation(Node educationHistory) {
        List<EducationalOrganization> edus = new ArrayList<>();
        if (educationHistory != null) {
            List<Node> schools = getNodes("SchoolOrInstitution", educationHistory.getChildNodes());
            if (schools != null) {
                for (Node school : schools) {
                    EducationalOrganization edu = new EducationalOrganization();
                    Node s = getNode("School", school.getChildNodes());
                    if (s != null) {
                        edu.setSchool(getNodeValue("SchoolName", s.getChildNodes()));
                    }
                    edu.setReferenceLocation(getAddressFromNode(getNode("PostalAddress", school.getChildNodes())));
                    List<Node> degrees = getNodes("Degree", school.getChildNodes());
                    if (degrees != null) {
                        List<NameAndCode> eduLevels = new ArrayList<>();
                        List<NameAndCode> degTypes = new ArrayList<>();
                        for (Node degree : degrees) {
                            String degreeType = getNodeAttr("degreeType", degree);
                            NameAndCode deg = new NameAndCode();
                            deg.setName(degreeType);
                            eduLevels.add(deg);
                            
                            String degreeName = getNodeValue("DegreeName", degree.getChildNodes());
                            
                            edu.setDegreeDate(getDateFromNode(getNode("DegreeDate", degree.getChildNodes())));
                            
                            List<Node> majors = getNodes("DegreeMajor", degree.getChildNodes());
                            if (majors != null) {
                                List<String> majs = new ArrayList<>();
                                for (Node major : majors) {
                                    String maj = getNodeValue("Name", major.getChildNodes());
                                    if (maj != null) {
                                        majs.add(maj);
                                        NameAndCode degType = new NameAndCode();
                                        degType.setName(degreeName + " in " + maj);
                                        degTypes.add(degType);
                                    }
                                }
                                if (!majs.isEmpty()) {
                                    edu.setMajorProgramName(majs);
                                }
                            }
                            List<Node> minors = getNodes("DegreeMajor", degree.getChildNodes());
                            if (minors != null) {
                                List<String> mins = new ArrayList<>();
                                for (Node minor : minors) {
                                    String min = getNodeValue("Name", minor.getChildNodes());
                                    if (min != null) {
                                        mins.add(min);
                                        
                                        NameAndCode degType = new NameAndCode();
                                        degType.setName("Minor in " + min);
                                        degTypes.add(degType);
                                    }
                                }
                                if (!mins.isEmpty()) {
                                    edu.setMinorProgramName(mins);
                                }
                            }
                        }
                        
                        if (!eduLevels.isEmpty()) {
                            edu.setEducationLevel(eduLevels);
                        }
                        if (!degTypes.isEmpty()) {
                            edu.setDegreeType(degTypes);
                        }
                    }
                    
                    edus.add(edu);
                }
            }
        }
        return edus;
    }

    protected List<Competency> getCompetencies(Node qualifications) {
        List<Competency> comps = new ArrayList<>();
        if (qualifications != null) {
            List<Node> compenticies = getNodes("Competency", qualifications.getChildNodes());
            if (compenticies != null) {
                
                for (Node compentency : compenticies) {
                    Competency comp = new Competency();
                    comp.setCompetencyID(getNodeValue("CompetencyId", compentency.getChildNodes()));
                    comp.setCompetencyName(getNodeAttr("name", compentency));
                    Node compEvidence = getNode("CompetencyEvidence", compentency.getChildNodes());
                    if (compEvidence != null) {
                        comp.setCompetencyLevel(getNodeValue("NumericValue", compEvidence.getChildNodes()));
                    }
                    
                    comps.add(comp);
                }
            }
        }
        return comps;
    }

    protected List<License> getLicenses(Node licenses) {
        List<License> licenseList = new ArrayList<>();
        if (licenses != null) {
            List<Node> lics = getNodes("LicenseOrCertification", licenses.getChildNodes());
            if (lics != null) {
                for (Node lic : lics) {
                    License l = new License();
                    l.setLicenseName(getNodeValue("Name", lic.getChildNodes()));
                    l.setIssuingAuthorityName(getNodeValue("IssuingAuthority", lic.getChildNodes()));
                    l.setDescription(getNodeValue("Description", lic.getChildNodes()));
                    
                    Node effectiveDate = getNode("EffectiveDate", lic.getChildNodes());
                    if (effectiveDate != null) {
                        l.setFirstIssuedDate(getDateFromNode(getNode("ValidFrom", effectiveDate.getChildNodes())));
                        l.setEndDate(getDateFromNode(getNode("ValidTo", effectiveDate.getChildNodes())));
                    }
                    licenseList.add(l);
                }
            }
        }
        return licenseList;
    }

    protected Address getAddressFromNode(Node node) {
        if (node != null) {
            Address a = new Address();
            a.setCountryCode(getNodeValue("CountryCode", node.getChildNodes()));
            a.setPostalCode(getNodeValue("PostalCode", node.getChildNodes()));
            a.setCityName(getNodeValue(getCityNameNodeName(), node.getChildNodes()));
            a.setState(getNodeValue("Reigon", node.getChildNodes()));

            Node deliveryAddress = getNode("DeliveryAddress", node.getChildNodes());
            if (deliveryAddress != null) {
                String addressLine1 = getNodeValue("AddressLine1", deliveryAddress.getChildNodes());
                String addressLine2 = getNodeValue("AddressLine2", deliveryAddress.getChildNodes());
                if (addressLine1 != null && !addressLine1.trim().isEmpty()) {
                    String address = addressLine1;
                    if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
                        address += ", " + addressLine2.trim();
                    }
                    a.setAddressLine(address);
                }
            }

            return a;
        }
        return null;
    }

}
