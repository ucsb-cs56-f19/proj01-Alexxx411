package earthquakes.controllers;

import earthquakes.services.LocationQueryService;
import earthquakes.searches.LocSearch;

import earthquakes.osm.Place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.nimbusds.oauth2.sdk.client.ClientReadRequest;

import earthquakes.entities.Location;
import earthquakes.repositories.LocationRepository;

@Controller
public class LocationsController {

    private LocationRepository locationRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public LocationsController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;   
    }

    @GetMapping("/locations/search")
    public String getlocationsSearch(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            LocSearch locSearch) {
        return "locations/search";
    }

    @GetMapping("/locations/results")
    public String getLocationsResults(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            LocSearch locSearch) {

        LocationQueryService e = new LocationQueryService();
        model.addAttribute("locSearch", locSearch);

        String json = e.getJSON(locSearch.getLocation());
        model.addAttribute("json", json);

        List<Place> place = Place.fromJSON(json);
        model.addAttribute("place", place);
        
        return "locations/results";
    }

    @GetMapping("/locations")
        public String index(Model model) {
        Iterable<Location> locations= locationRepository.findAll();
        model.addAttribute("locations", locations);
        return "locations/index";
    }

    @PostMapping("/locations/add")
        public String add(Location location, Model model) {
        locationRepository.save(location);
        model.addAttribute("locations", locationRepository.findAll());
        return "locations/index";
    }
}
