package balt.sloboda.portal.model.request.predefined;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestParamType;
import balt.sloboda.portal.model.request.RequestType;

import java.util.*;

public class NewUserRequest {

    public static RequestType getRequestType(){
        return new RequestType()
                .name("NewUserRequest")
                .durable(true)
                .title("New User Request")
                .parameters(Arrays.asList(
                        new RequestParam().name("user").optional(false).type(RequestParamType.STRING).comment("user"),
                        new RequestParam().name("firstName").optional(false).type(RequestParamType.STRING).comment("firstName"),
                        new RequestParam().name("lastName").optional(false).type(RequestParamType.STRING).comment("lastName"),
                        new RequestParam().name("street").optional(false).type(RequestParamType.STRING).comment("street"),
                        new RequestParam().name("houseNumber").optional(false).type(RequestParamType.INTEGER).comment("houseNumber"),
                        new RequestParam().name("plotNumber").optional(false).type(RequestParamType.INTEGER).comment("plotNumber")
                ))
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)));
    }
}
