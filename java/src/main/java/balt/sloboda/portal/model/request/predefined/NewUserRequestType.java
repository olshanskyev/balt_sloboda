package balt.sloboda.portal.model.request.predefined;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NewUserRequestType implements PredefinedRequestType{

    private final String name = "NewUserRequest";

    @Override
    public RequestType getRequestType(){
        return new RequestType()
                .setName(name)
                .setDurable(false)
                .setTitle("newUserRequest")
                .setRequestIdPrefix("NUR")
                .setDescription("Used to create a new user request on registration page")
                .setParameters(NewUserRequestParams.getParamsDefinition())
                .setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)))
                .setSystemRequest(true)
                .setDisplayOptions(new HashMap<String, String>(){{
                    put("iconPack", "eva");
                    put("icon", "person-add-outline");
                }});

    }

    @Override
    public String getName() {
        return name;
    }


}
