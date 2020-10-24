package com.paxovision.raptor.comparator;

import java.util.HashMap;
import java.util.Map;

public class DataBuilder {

    protected String employeeId;
    protected String startDate;
    protected int age;
    public DataBuilder employeeId(String value) {
        this.employeeId = value;
        return this;
    }

    public DataBuilder startDate(String value) {
        this.startDate = value;
        return this;
    }

    public DataBuilder age(int value) {
        this.age = value;
        return this;
    }

    public Map build() {
        Map<String, Object> data = new HashMap<>();
        data.put("employeeId", employeeId);
        data.put("startdate", startDate);
        data.put("age", age);
        return data;
    }
}
