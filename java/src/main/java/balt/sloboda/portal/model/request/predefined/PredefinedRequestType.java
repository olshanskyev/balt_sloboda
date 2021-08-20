package balt.sloboda.portal.model.request.predefined;

import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.type.RequestTypeParams;

public interface PredefinedRequestType {
    RequestType getRequestType();
    String getName();
}
