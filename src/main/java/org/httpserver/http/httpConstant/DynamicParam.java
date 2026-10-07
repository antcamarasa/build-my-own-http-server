package org.httpserver.http.httpConstant;

public enum DynamicParam {
    CATEGORY("category");

    String value;
    DynamicParam(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static DynamicParam getEnumFromValue(String value){
        for(DynamicParam dp : DynamicParam.values()){
            if(dp.value.equals(value)){
                return dp;
            }
        }
        return null;
    }
}
