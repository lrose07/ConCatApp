package com.lrose07.concat;


public class ConCatEvent {

    private String name;
    private long start;
    private long end;
    private String code;

    ConCatEvent() {

    }

    ConCatEvent(String name, long start, long end, String code) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.code = code;
    }
}
