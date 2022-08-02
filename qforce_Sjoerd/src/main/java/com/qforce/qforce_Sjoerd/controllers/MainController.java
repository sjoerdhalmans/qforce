package com.qforce.qforce_Sjoerd.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qforce.qforce_Sjoerd.Logic.PersonServiceLogic;
import com.qforce.qforce_Sjoerd.interfaces.domain.Person;
import com.qforce.qforce_Sjoerd.models.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {
    private final PersonServiceLogic personServiceLogic;

    @Autowired
    public MainController(PersonServiceLogic personServiceLogic) {
        this.personServiceLogic = personServiceLogic;
    };

    @GetMapping("/persons")
    List<Person> search(@RequestParam String q) throws JsonProcessingException {
        return personServiceLogic.search(q);
    }

    @RequestMapping("/persons/{id}")
    PersonResource get(@PathVariable long id) throws JsonProcessingException {
        Optional<Person> person = personServiceLogic.get(id);
        if (person.isPresent()) {
            PersonResource resource = (PersonResource) person.get();
            return resource;
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }
}
