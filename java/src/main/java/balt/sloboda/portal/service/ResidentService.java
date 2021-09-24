package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Resident;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.repository.DbAddressRepository;
import balt.sloboda.portal.repository.DbResidentRepository;
import balt.sloboda.portal.repository.DbUserRepository;
import balt.sloboda.portal.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentService {

    @Autowired
    private DbAddressRepository dbAddressRepository;

    @Autowired
    private DbResidentRepository dbResidentRepository;


    public boolean addressAlreadyUsed(Long addressId){
        return !dbResidentRepository.findByAddressId(addressId).isEmpty();
    }

    public List<Resident> selectAllResidents(){
        return dbResidentRepository.findAll();
    }

    public Resident getResidentByUserName(String userName) {
        return dbResidentRepository.findByUserUserName(userName).stream().findFirst().orElse(null);
    }

    public Resident save(Resident resident){
        return dbResidentRepository.save(resident);
    }

}
