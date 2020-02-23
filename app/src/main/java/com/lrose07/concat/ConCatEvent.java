package com.lrose07.concat;

/**
 * @author Lauren Rose, Elizabeth Jolly
 * @version 23 Feb 2020
 *
 * An event object
 */
public class ConCatEvent {

    // these are public due to a Serializable issue
    public String name;
    public String code;

    /**
     * no-parameter constructor
     */
    ConCatEvent() {

    }

    /**
     * creates a new event
     * @param name name of event
     * @param code code for event
     */
    ConCatEvent(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // Getters/Setters

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
