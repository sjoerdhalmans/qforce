package com.qforce.qforce_Sjoerd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qforce.qforce_Sjoerd.Logic.DatabaseConnector;
import com.qforce.qforce_Sjoerd.Logic.PersonServiceLogic;
import com.qforce.qforce_Sjoerd.controllers.MainController;
import com.qforce.qforce_Sjoerd.interfaces.domain.Gender;
import com.qforce.qforce_Sjoerd.interfaces.domain.Person;
import com.qforce.qforce_Sjoerd.models.PersonResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QforceSjoerdApplicationTests {

	@Autowired
	private MainController controller;
	@Autowired
	private PersonServiceLogic logic;
	@Autowired
	private DatabaseConnector connector;

	// Check if classes initialize properly
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(logic).isNotNull();
		assertThat(connector).isNotNull();
	}

	// test if weight is cleaned succesfully with jaba, and that gender is translated right
	@Test
	void testPersonServiceGet() throws JsonProcessingException {
		PersonResource person = (PersonResource) logic.get(1).get();

		assertThat(person.getGender()).isEqualTo(Gender.MALE);
		assertThat(person.getMovies().size()).isEqualTo(4);

		PersonResource person2 = (PersonResource) logic.get(16).get();

		assertThat(person2.getWeight()).isEqualTo(1358);
	}

	// Test if searching works and returns the desired amount of matches
	@Test
	void testPersonServiceSearch() throws JsonProcessingException {
		List<Person> people = logic.search("r");

		assertThat(people.size()).isEqualTo(46);
	}

	// Test if getting a resource sends a request to the logging module
	@Test
	void testLogging() throws JsonProcessingException {
		logic.get(5);

		long test = connector.counter;

		assertThat(test).isEqualTo(1);
	}

}
