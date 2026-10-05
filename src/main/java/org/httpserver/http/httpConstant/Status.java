package org.httpserver.http.httpConstant;

public enum Status {
    OK("200"),
    CREATED("201"),
    NO_CONTENT("204"),
    BAD_REQUEST("400"),
    NOT_FOUND("404"),
    INTERNAL_SERVER_ERROR("500");

    final String code;

    Status(String code){
        this.code = code;
    }

    public String getStatusCode(){
        return this.code;
    }
}
