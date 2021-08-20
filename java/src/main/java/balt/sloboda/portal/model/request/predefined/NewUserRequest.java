package balt.sloboda.portal.model.request.predefined;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestParamType;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;

import java.util.*;

public class NewUserRequest implements PredefinedRequestType{

    private final String name = "NewUserRequest";

    @Override
    public RequestType getRequestType(){
        return new RequestType()
                .setName(name)
                .setDurable(true)
                .setTitle("newUserRequest")
                .setParameters(Arrays.asList(
                        new RequestParam().setName("userName").setOptional(false).setType(RequestParamType.STRING).setComment("userName"),
                        new RequestParam().setName("firstName").setOptional(false).setType(RequestParamType.STRING).setComment("firstName"),
                        new RequestParam().setName("lastName").setOptional(false).setType(RequestParamType.STRING).setComment("lastName"),
                        new RequestParam().setName("street").setOptional(false).setType(RequestParamType.STRING).setComment("street"),
                        new RequestParam().setName("houseNumber").setOptional(false).setType(RequestParamType.INTEGER).setComment("houseNumber"),
                        new RequestParam().setName("plotNumber").setOptional(false).setType(RequestParamType.INTEGER).setComment("plotNumber")
                ))
                .setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)));
    }

    @Override
    public String getName() {
        return name;
    }


}
