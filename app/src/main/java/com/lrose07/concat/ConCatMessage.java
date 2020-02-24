package com.lrose07.concat;

/**
 * @author Lauren Rose, Elizabeth Jolly
 * @version 23 Feb 2020
 *
 * A message object
 */
public class ConCatMessage {

    private String text;
    private ConCatEvent event;

    /**
     * no parameter constructor
     */
    public ConCatMessage() {}

    /**
     * creates a ConCatMessage
     * @param text message text
     * @param event event message belongs to
     */
    ConCatMessage(String text, ConCatEvent event) {
        this.text = text;
        this.event = event;
    }

    // Getters/Setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ConCatEvent getEvent() {
        return event;
    }

    public void setEvent(ConCatEvent event) {
        this.event = event;
    }
}
