package com.qforce.qforce_Sjoerd.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.qforce.qforce_Sjoerd.interfaces.domain.Movie;

import java.time.LocalDate;

public class MovieResource implements Movie {
    @JsonProperty
    String title;
    @JsonProperty("episode_id")
    Integer episode;
    @JsonProperty
    String director;
    @JsonProperty("release_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate releaseDate;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Integer getEpisode() {
        return episode;
    }

    @Override
    public String getDirector() {
        return director;
    }

    @Override
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
}
