package com.lrose07.concat;


public class ConCatEvent {

    private String name;
    private String code;

    ConCatEvent() {

    }

    ConCatEvent(String name, String code) {
        this.name = name;
        this.code = code;
    }

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setCode(String code) {
        this.code = code;
    }

    String getCode() {
        return code;
    }
}
