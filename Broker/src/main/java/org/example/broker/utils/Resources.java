package org.example.broker.utils;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class Resources {

    private Resources() {
    }

    public static String get(String resource) {

        URL url = com.google.common.io.Resources.getResource(resource);

        try {
            return com.google.common.io.Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getLines(String resource) {

        URL url = com.google.common.io.Resources.getResource(resource);

        try {
            return com.google.common.io.Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
