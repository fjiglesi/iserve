package com.epimorphics.lda.sources;

import com.hp.hpl.jena.util.FileManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// EXPLORATORY.
public class AuthMap {

    public interface NamesAndValues {
        public String getParameter(String name);

        public List<String> getParameterNames();
    }

    public static final String AUTH_NAME_PREFIX = "com.epimorphics.api.authKey";

    Map<String, AuthInfo> map = new HashMap<String, AuthInfo>();

    public static AuthMap loadAuthMap(FileManager fm, NamesAndValues map) {
        AuthMap am = new AuthMap();
        for (String name : map.getParameterNames()) {
            if (name.startsWith(AuthMap.AUTH_NAME_PREFIX)) {
                String restName = name.substring(AuthMap.AUTH_NAME_PREFIX.length() + 1);
                am.put(restName, readAuthFile(fm, map.getParameter(name)));
            }
        }
        return am;
    }

    private void put(String name, AuthInfo ai) {
        map.put(name, ai);
    }

    private static AuthInfo readAuthFile(FileManager fm, String fileName) {
        AuthInfo ai = new AuthInfo();
        String wholeFile = fm.readWholeFileAsUTF8(fileName);
        String[] lines = wholeFile.split("\n");
        for (String line : lines) {
            if (!line.equals("") && !line.startsWith("#")) {
                String[] parts = line.split(" *= *");
                ai.put(parts[0], parts[1]);
            }
        }
        return ai;
    }

    public AuthInfo get(String key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}