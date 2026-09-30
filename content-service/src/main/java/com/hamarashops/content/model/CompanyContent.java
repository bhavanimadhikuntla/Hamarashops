package com.hamarashops.content.model;

import java.util.List;

public class CompanyContent {

    private String name;
    private String headline;
    private String ceo;
    private String designation;
    private String mission;
    private String vision;
    private String approach;
    private String journey;
    private List<LocationContent> locations;

    public CompanyContent() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getJourney() {
        return journey;
    }

    public void setJourney(String journey) {
        this.journey = journey;
    }

    public List<LocationContent> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationContent> locations) {
        this.locations = locations;
    }
}