package com.jukebox.swiss_tournament.data.pairing_engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javafo.api.JaVaFoApi;

public class JaVaFoHandler {

    private final File dir;

    public JaVaFoHandler(File dir) {
        this.dir = dir;
    }

    //Command Parameter for exec
    public static int PAIRING = 1000;
    public static int PAIRING_WITH_BAKU = 1001;
    public static int PRE_PAIRING_CHECKLIST = 1100;
    public static int POST_PAIRING_CHECKLIST = 1110;
    public static int POST_PAIRING_WITH_BAKU_CHECKLIST = 1111;
    public static int CHECK_TOURNAMENT = 1200;
    public static int CHECK_ONE_ROUND = 1210;
    public static int RANDOM_GENERATOR = 1300;
    public static int RANDOM_GENERATOR_WITH_BAKU = 1301;


    public Map<Integer, Integer> getPairings(String filename) {
        File input = new File(dir.getAbsolutePath()+"/"+filename);
        if (!input.exists()) {
            throw new IllegalArgumentException("file "+filename+" does not exist in "+dir.getAbsolutePath());
        }

        Map<Integer, Integer> pairings = new HashMap<>();
        try (
                InputStream inputStream = Files.newInputStream(input.toPath());
        ){
            String[] output = JaVaFoApi.exec(PAIRING, inputStream).split("\n");
            for (int i = 0; i < output.length; i++) {
                if (i==0) {continue;} //first row is the number of pairings
                String[] pair = output[i].split(" ");
                pairings.put(
                    Integer.parseInt(pair[0]),
                    Integer.parseInt(pair[1])
                );
            }
        } catch (IOException e) {
            pairings = new HashMap<>();
        }

        return pairings;
    }
}
