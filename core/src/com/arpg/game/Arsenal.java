package com.arpg.game;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Arsenal {
    private Map<String, Weapon> map;

    private Arsenal(){
        this.map = new HashMap<>();
        BufferedReader reader = null;
        try{
            reader = Gdx.files.internal("data/arsenal.csv").reader(8192);
            reader.readLine();
            String str = null;
            while((str = reader.readLine()) != null){
                Weapon w = new Weapon(str);
                map.put(w.getTitle(), w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Weapon getPatternFromTitle(String title){
        return map.get(title);

    }
}
