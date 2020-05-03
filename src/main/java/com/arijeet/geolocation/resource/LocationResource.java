package com.arijeet.geolocation.resource;

import com.arijeet.geolocation.interfce.Point;
import com.arijeet.geolocation.model.Location;
import com.arijeet.geolocation.service.LocationService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/location")
public class LocationResource {

    @Inject
    LocationService locationService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/country")
    public Location getCountry(Point point) {
        Optional<Location> locationOptional = locationService.getLocation(point.getLatitude(), point.getLongitude());
        return locationOptional.get();
    }
}
