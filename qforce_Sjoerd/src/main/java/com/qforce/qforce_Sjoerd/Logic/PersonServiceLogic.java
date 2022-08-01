package com.qforce.qforce_Sjoerd.Logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qforce.qforce_Sjoerd.interfaces.domain.Gender;
import com.qforce.qforce_Sjoerd.interfaces.domain.Movie;
import com.qforce.qforce_Sjoerd.interfaces.domain.Person;
import com.qforce.qforce_Sjoerd.models.MovieResource;
import com.qforce.qforce_Sjoerd.models.PersonResource;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class PersonServiceLogic implements com.qforce.qforce_Sjoerd.interfaces.service.PersonService {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Override
    public List<Person> search(String query) {
        return null;
    }

    @Override
    public Optional<Person> get(long id) throws JsonProcessingException {
        String uri = "https://swapi.dev/api/people/" + id;
        RestTemplate template = new RestTemplate();
        String result = template.getForObject(uri, String.class);
        JsonNode resource = mapper.readValue(result, JsonNode.class);
        PersonResource person = populatePerson(resource, id);

        Optional<Person> character = Optional.of(person);
        return character;
    }

    private List<String> getMovieUris(String json) {
        json = json.replace("[", "");
        json =json.replace("]", "");
        json =json.replace("\"", "");

        return new ArrayList<String>(Arrays.asList(json.split(",")));
    }

    private List<Movie> populateMovies(String json) throws JsonProcessingException {
        List<String> moviesUris = getMovieUris(json);
        List<Movie> movies = new ArrayList<>();

        for (String movie : moviesUris) {
            String uri = movie;
            RestTemplate template = new RestTemplate();
            String result = template.getForObject(uri, String.class);
            MovieResource resource = mapper.readValue(result, MovieResource.class);
            movies.add(resource);
        }

        return movies;
    }

    private String adjustGenderText(String gender) {
        if (Objects.equals(gender, "N/A")) {
            gender = "NOT_APPLICABLE";

            return gender;
        }
        else if (Objects.equals(gender, "MALE")) {
            return gender;
        }
        else if (Objects.equals(gender, "FEMALE")) {
            return gender;
        }
        else {
            gender = "UNKNOWN";

            return gender;
        }
    }

    private Integer cleanInteger(String weight) {
        if (weight.contains(",")) {
            weight = weight.replace(",", "");
        }

        if (weight.contains(".")) {
            Integer dotLocation = weight.indexOf(".");

            weight = weight.substring(0,dotLocation);
        }

        return Integer.parseInt(weight);
    }

    private PersonResource populatePerson(JsonNode resource, long id) throws JsonProcessingException {
        String genderString = resource.get("gender").textValue().toUpperCase();
        genderString = adjustGenderText(genderString);
        Gender gender = Gender.valueOf(genderString);
        List<Movie> movies = populateMovies(resource.get("films").toString());
        String weightString = resource.get("mass").textValue();

        PersonResource resources = new PersonResource();

        return new PersonResource(
                id,
                resource.get("name").textValue(),
                resource.get("birth_year").textValue(),
                gender,
                Integer.parseInt(resource.get("height").textValue()),
                cleanInteger(weightString),
                movies
                );
    }
}
