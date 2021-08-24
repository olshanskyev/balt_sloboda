package balt.sloboda.portal.service;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.repository.DbUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private DbUserRepository dbUserRepository;


    public List<User> selectAllUsers(){
        return dbUserRepository.findAll();
    }

    public User createUser(User user){
         return dbUserRepository.save(user);
    }

    public void deleteUser(Long id){
        dbUserRepository.deleteById(id);

    }

    public boolean alreadyExists(String userName){
        Optional<User> first = dbUserRepository.findByUserName(userName).stream().findFirst();
        return (first.isPresent());
    }

    public Optional<User> findByUserName(String userName){
        return dbUserRepository.findByUserName(userName).stream().findFirst();
    }

    public Optional<User> findByPasswordResetToken(String token){
        return dbUserRepository.findByPasswordResetToken(token).stream().findFirst();
    }

    public Optional<User> findById(Long id){
       return dbUserRepository.findById(id);
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    public void setNewPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password)).setPasswordResetToken("");
        dbUserRepository.save(user);
    }


}
