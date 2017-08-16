module com.apolo.jdk.person.service {
    requires transitive com.apolo.jdk.services;

    provides com.apolo.jdk.services.SparkService
        with com.apolo.jdk.service.person.PersonService;

    requires spark.core;
}
