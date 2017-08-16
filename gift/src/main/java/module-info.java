module com.apolo.jdk.service.gift {
    requires transitive com.apolo.jdk.services;

    provides com.apolo.jdk.services.SparkService
        with com.apolo.jdk.service.gift.GiftService;

    requires spark.core;
}
