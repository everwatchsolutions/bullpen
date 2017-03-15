/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import java.util.List;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    public User findByEmailAndPassword(String email, String password);
    public User findByEmail(String email);
    @Query(value = "{'lastName': {$regex : ?0, $options: 'i'}}")
    public List<User> findByLastNameRegex(String query);
    @Query(value = "{'firstName': {$regex : ?0, $options: 'i'}}")
    public List<User> findByFirstNameRegex(String query);
    @Query(value = "{'fullName': {$regex : ?0, $options: 'i'}}")
    public List<User> findByFullNameRegex(String query);
    public List<User> findByCompany(Company company);
}
