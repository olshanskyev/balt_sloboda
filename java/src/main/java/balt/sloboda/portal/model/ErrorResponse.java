package balt.sloboda.portal.model;

import java.util.Map;

public class ErrorResponse {
    private String error;
    private Map<String, String> parameters; //parameter name, parameter value

    public String getError() {
        return error;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(String error, Map<String, String> parameters){
        this(error);
        this.parameters = parameters;
    }

}
