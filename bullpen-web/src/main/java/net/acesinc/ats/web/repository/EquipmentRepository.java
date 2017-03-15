package net.acesinc.ats.web.repository;

import java.util.List;

import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.equipment.Equipment;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Myles on 1/10/17.
 */
public interface EquipmentRepository extends PagingAndSortingRepository<Equipment, String> {

    public Equipment findById(String id);
    public List<Equipment> findByType(String type);
    public List<Equipment> findByCompany(Company c);

}
