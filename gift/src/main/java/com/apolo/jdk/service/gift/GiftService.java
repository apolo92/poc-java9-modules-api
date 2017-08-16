package com.apolo.jdk.service.gift;

import com.apolo.jdk.services.SparkService;
import spark.Spark;

public class GiftService implements SparkService {
    @Override
    public void initService() {
        System.out.print("Iniciando Gift");
        Spark.get("/Gift", ((request, response) -> {
            System.out.println("Hola desde Gift");
            return "Hola desde Gift";
        }));
    }
}
