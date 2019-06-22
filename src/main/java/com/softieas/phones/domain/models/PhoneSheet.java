package com.softieas.phones.domain.models;

import java.io.Serializable;

public class PhoneSheet implements Serializable {
    private long id;
    private String number;

    public PhoneSheet(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
