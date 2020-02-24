package com.lrose07.concat;

/**
 * @author Lauren Rose, Elizabeth Jolly
 * @version 23 Feb 2020
 *
 * An event object
 */
public class ConCatEvent {

    // these are public due to a Serializable issue
    private String name;
    private String code;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
