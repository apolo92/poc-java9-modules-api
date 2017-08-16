package com.apolo.jdk.service.person;

import com.apolo.jdk.services.SparkService;
import spark.Spark;

public class PersonService implements SparkService {

    @Override
    public void initService() {
        System.out.print("Iniciando Persona");
        Spark.get("/Person", ((request, response) -> {
            System.out.print("Hola desde persona");
            return "Hola desde persona";
        }));
    }
}
