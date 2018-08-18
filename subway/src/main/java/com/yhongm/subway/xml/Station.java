package com.yhongm.subway.xml;

import java.util.ArrayList;

public class Station {
    public String name;
    public ArrayList<String> linenames = new ArrayList<>();
    public String firstend;

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", linenames=" + linenames +
                ", firstend='" + firstend + '\'' +
                '}';
    }
}
