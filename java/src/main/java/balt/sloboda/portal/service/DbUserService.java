package balt.sloboda.portal.service;



import balt.sloboda.portal.model.User;
import balt.sloboda.portal.repository.DbUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by evolshan on 06.03.2020.
 */
@Service
public class DbUserService {

    @Autowired
    private DbUserRepository dbUserRepository;

    public List<User> selectAll(){
        return dbUserRepository.findAll();
    }

    public User createUser(User user){
         return dbUserRepository.save(user);
    }

    public void deleteUser(Long id){
        dbUserRepository.deleteById(id);

    }

    public boolean alreadyExists(User user){
        User found = dbUserRepository.findByUser(user.getUser()).stream().findFirst().orElse(null);
        return (found != null);
    }

    public User findByUserName(String userName){
        return dbUserRepository.findByUser(userName).stream().findFirst().orElse(null);
    }

    public User findById(Long id){
       return dbUserRepository.findById(id).orElse(null);
    }



}
