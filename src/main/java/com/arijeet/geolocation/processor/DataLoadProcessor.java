package com.arijeet.geolocation.processor;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import lombok.Setter;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped //Globally available in the application
public class DataLoadProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoadProcessor.class);

    @Setter
    @Getter
    FeatureCollection featureCollection;

    //Loads the Geometries data
    void onStart(@Observes StartupEvent startupEvent) {
        LOGGER.info("Starting application");
        long startTime = System.currentTimeMillis();
        FeatureJSON featureJSON = new FeatureJSON();
        InputStream locationFileInputStream = getClass().getClassLoader()
                .getResourceAsStream("countries-data.geojson");
        try {
            featureCollection = featureJSON.readFeatureCollection(locationFileInputStream);
            int size = featureCollection.size();
            LOGGER.info("Size of data loaded : {}", size);
        } catch (IOException e) {
            LOGGER.error("Exception occurred while reading location data during startup", e);
        }
        LOGGER.info("Time taken to load data : {}", System.currentTimeMillis() - startTime);
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        LOGGER.info("Shutting down application");
    }

}
