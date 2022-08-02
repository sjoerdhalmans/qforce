package com.qforce.qforce_Sjoerd.Logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qforce.qforce_Sjoerd.interfaces.domain.Gender;
import com.qforce.qforce_Sjoerd.interfaces.domain.Movie;
import com.qforce.qforce_Sjoerd.interfaces.domain.Person;
import com.qforce.qforce_Sjoerd.models.MovieResource;
import com.qforce.qforce_Sjoerd.models.PersonResource;
import com.qforce.qforce_Sjoerd.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * I chose to write all logic in one class
 * as the application is not large enough
 * that it needs to be divided
 * <p>
 * I chose to manually create person resources instead of automatically
 * transforming them from JSON as there were a lot of irregularities
 * in the incoming data and the data types I needed them to be
 * (I would have done this differently if I were allowed to change
 * the provided interfaces)
 */

@Service
public class PersonServiceLogic implements com.qforce.qforce_Sjoerd.interfaces.service.PersonService {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final DatabaseConnector databaseConnector;

    @Autowired
    public PersonServiceLogic(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public List<Person> search(String query) throws JsonProcessingException {
        databaseConnector.logSearch(query);

        List<Person> populatedPeople = new ArrayList<>();
        String uri = "https://swapi.dev/api/people?search=" + query;
        RestTemplate template = new RestTemplate();
        String result = template.getForObject(uri, String.class);
        JsonNode resource = mapper.readValue(result, JsonNode.class);
        String json = resource.get("results").toString();

        List<JsonNode> people = mapper.readValue(json, new TypeReference<List<JsonNode>>(){});

        if (resource.get("next").textValue() != null) {
            people = iteratePeople(people, resource.get("next").textValue());
        }

        for (JsonNode node : people) {
            Integer id = getId(node);
            populatedPeople.add(populatePerson(node, id));
        }

        return populatedPeople;
    }

    private Integer getId(JsonNode node) {
        String uri = node.get("url").textValue();
        uri = uri.substring(0, uri.length() - 1);

        uri = uri.substring(uri.lastIndexOf("/")+1, uri.length());

        return Integer.parseInt(uri);
    }

    private List<JsonNode> iteratePeople(List<JsonNode> people, String nextUri) throws JsonProcessingException {
        boolean done = false;
        RestTemplate template = new RestTemplate();

        while (!done) {
            String result = template.getForObject(nextUri, String.class);
            JsonNode resource = mapper.readValue(result, JsonNode.class);
            String json = resource.get("results").toString();

            List<JsonNode> newPeople = mapper.readValue(json, new TypeReference<List<JsonNode>>(){});
            people.addAll(newPeople);

            if (resource.get("next").textValue() != null) {
                nextUri = resource.get("next").textValue();
            }
            else {
                done = true;
            }
        }

        return people;
    }

    @Override
    public Optional<Person> get(long id) throws JsonProcessingException {
        databaseConnector.logGet(String.valueOf(id));

        String uri = "https://swapi.dev/api/people/" + id;
        RestTemplate template = new RestTemplate();
        String result = "";
        try {
            result = template.getForObject(uri, String.class);
        } catch (final HttpClientErrorException error) {
            return Optional.empty();
        }

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
        if (weight.equals("unknown")) {
            return 0;
        }

        if (weight.contains(",")) {
            weight = weight.replace(",", "");
        }

        if (weight.contains(".")) {
            Integer dotLocation = weight.indexOf(".");

            weight = weight.substring(0,dotLocation);
        }

        return Integer.parseInt(weight);
    }

    private Integer getHeight(JsonNode node) {
        if (node.get("height").textValue().equals("unknown")) {
            return 0;
        }
        else {
            return Integer.parseInt(node.get("height").textValue());
        }
    }

    private PersonResource populatePerson(JsonNode resource, long id) throws JsonProcessingException {
        String genderString = resource.get("gender").textValue().toUpperCase();
        genderString = adjustGenderText(genderString);
        Gender gender = Gender.valueOf(genderString);
        List<Movie> movies = populateMovies(resource.get("films").toString());
        String weightString = resource.get("mass").textValue();
        Integer height = getHeight(resource);

        return new PersonResource(
                id,
                resource.get("name").textValue(),
                resource.get("birth_year").textValue(),
                gender,
                height,
                cleanInteger(weightString),
                movies
        );
    }
}
