package com.qforce.qforce_Sjoerd.models;

import com.qforce.qforce_Sjoerd.interfaces.domain.Gender;
import com.qforce.qforce_Sjoerd.interfaces.domain.Movie;
import com.qforce.qforce_Sjoerd.interfaces.domain.Person;

import java.util.List;

public class PersonResource implements Person {
    long id; // As defined in the SWAPI database
    String name;
    String birth_year; // BBY = Before Battle of Yavin | ABY = After Battle of Yavin
    Gender gender;
    Integer height; // In centimeters
    Integer weight; // In kilograms
    public List<Movie> movies; // Movies the character stars in

    public PersonResource() {

    }

    public PersonResource(long Id, String Name, String Birth_Year, Gender Gender,
                          Integer Height, Integer Weight, List<Movie> Movies) {
        id = Id;
        name = Name;
        birth_year = Birth_Year;
        gender = Gender;
        height = Height;
        weight = Weight;
        movies = Movies;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBirthYear() {
        return birth_year;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Integer getHeight() {
        return height;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public List<Movie> getMovies() {
        return movies;
    }
}
