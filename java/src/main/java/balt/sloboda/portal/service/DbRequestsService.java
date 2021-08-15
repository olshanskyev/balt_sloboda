package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.repository.DbRequestTypesRepository;
import balt.sloboda.portal.repository.DbRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbRequestsService {

    @Autowired
    private DbRequestsRepository dbRequestsRepository;

    @Autowired
    private DbRequestTypesRepository dbRequestTypesRepository;

    public List<RequestType> getAllRequestTypes() {
        return dbRequestTypesRepository.findAll();
    }

    public List<RequestType> getRequestTypesAvailableForUser() {
        return dbRequestTypesRepository.findAll().stream().filter(item -> item.getRoles().contains(Role.ROLE_USER)).collect(Collectors.toList());
    }


}
