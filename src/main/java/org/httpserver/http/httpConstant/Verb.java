package org.httpserver.http.httpConstant;

public enum Verb {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    final String value;
    Verb(String value){
        this.value = value;
    }

    // Attention : le fait de mettre getVerb en static permet d'accéder a la méthode dans créer d'instances.
    //             ce qui est possible, par ce que tous les champs d'un enum sont static par defaut ?
    //             a vérifier.
    public static Verb getVerb(String verb){
        for(Verb v : Verb.values()){
            System.out.println(v);
            if(v.value.equals(verb)) return v;
        }
        return null;
    }
}
