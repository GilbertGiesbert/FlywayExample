package com.joern.flywayexample;


public class Dog {

    private String id, name, color, race;

    public Dog(String id, String name, String color, String race) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.race = race;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getRace() {
        return race;
    }
}