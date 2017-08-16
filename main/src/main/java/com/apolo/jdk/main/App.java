package com.apolo.jdk.main;

import com.apolo.jdk.services.SparkService;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class App {

    public static void main(String[] args) {
        List<String> services = new ArrayList<>();
        ServiceLoader<SparkService> loader = ServiceLoader.load(SparkService.class);
        if (!loader.iterator().hasNext()) {
            System.out.println("No se han encontrado modulos para cargar");
        }
        for (SparkService service : loader) {
            services.add(service.getClass().getName());
            service.initService();
        }
    }
}
