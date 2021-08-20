package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.Resident;
import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.repository.DbAddressRepository;
import balt.sloboda.portal.repository.DbResidentRepository;
import balt.sloboda.portal.repository.DbUserRepository;
import balt.sloboda.portal.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private DbUserRepository dbUserRepository;

    @Autowired
    private DbAddressRepository dbAddressRepository;

    @Autowired
    private DbResidentRepository dbResidentRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private ResidentService residentService;

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

    public Optional<User> findById(Long id){
       return dbUserRepository.findById(id);
    }

    public void acceptUser(String userName) {
        String token = jwtTokenUtil.generatePasswordResetToken(userName);
        Optional<Request> request = requestsService.getNewUserRequestByUser(userName);
        if (request.isPresent()){
            User user = dbUserRepository.save(new User()
                    .setUserName(userName)
                    .setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER)))
                    .setPassword("")
                    .setPasswordResetToken(token));

            Address address = dbAddressRepository.save(new Address()
                    .setStreet(request.get().getParamValues().get("street"))
                    .setHouseNumber(Integer.parseInt(request.get().getParamValues().get("houseNumber")))
                    .setPlotNumber(Integer.parseInt(request.get().getParamValues().get("plotNumber"))));
            dbResidentRepository.save(new Resident()
                    .setFirstName(request.get().getParamValues().get("firstName"))
                    .setLastName(request.get().getParamValues().get("lastName"))
                    .setUser(user)
                    .setAddress(address));
            emailService.sendUserRegistrationRequestConfirmation(userName);
        } else {
            throw new RuntimeException("newUserRequestNotFound");
        }
    }

}
