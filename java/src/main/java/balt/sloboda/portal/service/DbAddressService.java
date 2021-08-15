package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.repository.DbAddressRepository;
import balt.sloboda.portal.repository.DbUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbAddressService {

    @Autowired
    private DbAddressRepository dbAddressRepository;

    public List<Address> selectAll(){
        return dbAddressRepository.findAll();
    }

    public Address createAddress(Address address){
         return dbAddressRepository.save(address);
    }

    public List<String> getAllStreets() {
        return dbAddressRepository.findDistinctStreets();
    }

    public List<Address> getAddressesOnStreet(String street){
        return dbAddressRepository.findAllByStreet(street);
    }

    public Address getAddressById(Long id){
        return dbAddressRepository.findAddressById(id).stream().findFirst().orElse(null);
    }

}
