package com.lrose07.concat;

import java.sql.Timestamp;

public class ConCatEvent {

    private String name;
    private Timestamp start;
    private Timestamp end;
    private String code;

    ConCatEvent() {

    }

    ConCatEvent(String name, Timestamp start, Timestamp end, String code) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.code = code;
    }
}
