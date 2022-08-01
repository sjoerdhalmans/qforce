package com.qforce.qforce_Sjoerd.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qforce.qforce_Sjoerd.Logic.PersonServiceLogic;
import com.qforce.qforce_Sjoerd.interfaces.domain.Person;
import com.qforce.qforce_Sjoerd.models.PersonResource;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {
    PersonServiceLogic personServiceLogic = new PersonServiceLogic();

    @GetMapping("/persons")
    String test(@RequestParam String q) {
        List<Person> people = personServiceLogic.search(q);
        return "Hello";
    }

    @RequestMapping("/persons/{id}")
    PersonResource testing(@PathVariable long id) throws JsonProcessingException {
        Optional<Person> person = personServiceLogic.get(id);
        PersonResource resource = (PersonResource) person.get();
        return resource;
    }
}
