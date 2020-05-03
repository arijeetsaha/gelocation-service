package com.arijeet.geolocation.service;

import com.arijeet.geolocation.model.Location;
import com.arijeet.geolocation.processor.DataLoadProcessor;
import io.quarkus.runtime.StartupEvent;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    @Inject
    DataLoadProcessor dataLoadProcessor;

    GeometryFactory geometryFactory;

    void init(@Observes StartupEvent startupEvent) {
        geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    }

    public Optional<Location> getLocation(double latitude, double longitude) {
        long currentMillis = System.currentTimeMillis();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Geometry point = geometryFactory.createPoint(coordinate);
        FeatureCollection featureCollection = dataLoadProcessor.getFeatureCollection();
        FeatureIterator featureIterator = featureCollection.features();
        Location location = null;
        while (featureIterator.hasNext()) {
            SimpleFeature feature = (SimpleFeature) featureIterator.next();
            boolean isContains = ((Geometry) feature.getDefaultGeometry()).contains(point);
            if (isContains) {
                location = new Location();
                location.setCountryName((String) feature.getAttribute("ADMIN"));
                location.setCountryCode((String) feature.getAttribute("ISO_A3"));
                break;
            }
        }
        LOGGER.info("Country Location found in : {}", System.currentTimeMillis() - currentMillis);
        return Optional.ofNullable(location);
    }
}
