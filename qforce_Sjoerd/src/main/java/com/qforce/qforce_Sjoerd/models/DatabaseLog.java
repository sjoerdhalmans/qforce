package com.qforce.qforce_Sjoerd.models;

import javax.persistence.*;

@Entity
@Table
public class DatabaseLog {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String searchRequest;

    @Column
    private String requestType;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
