package net.acesinc.ats.web.repository;

import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.equipment.ProductLicense;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Myles on 2/6/17.
 */
public interface ProductLicenseRepository extends PagingAndSortingRepository<ProductLicense,String>{
    public ProductLicense findById(String id);
    public List<ProductLicense> findByCompany(Company c);

}
