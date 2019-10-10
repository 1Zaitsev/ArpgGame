package com.arpg.game;

import com.badlogic.gdx.math.MathUtils;
import javafx.fxml.Initializable;

public class Weapon implements Item {
    private String title;
    private float attackPeriod;
    private int minDamage;
    private int maxDamage;
    private float critChance;
    private float stunChance;
    private float bleedChance;

    @Override
    public Type getItemType() {
        return Type.WEAPON;
    }

    @Override
    public String getTitle() {
        return title + " [" + minDamage + "-" + maxDamage + "]";
    }

    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public boolean isWearable() {
        return true;
    }

    public float getAttackPeriod() {
        return attackPeriod;
    }

    public int getDamage() {
        return MathUtils.random(minDamage, maxDamage);
    }


    public float getCritChance() {
        return critChance;
    }

    public float getStanChance() {
        return stunChance;
    }

    public float getBleedChance() {
        return bleedChance;
    }

    public Weapon(String title, float attackPeriod, int minDamage, int maxDamage, float critChance, float stunChance, float bleedChance) {
        this.title = title;
        this.attackPeriod = attackPeriod;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.critChance = critChance;
        this.stunChance = stunChance;
        this.bleedChance = bleedChance;
    }
// __title__,__att_period__,__min_dmg__,__max_dmg__,__crit_chance__,__stan_chance__,__bleed_chance__
    public Weapon(String line){
        String[] tokens = line.split(",");
        this.title = tokens[0].trim();
        this.attackPeriod = Float.parseFloat(tokens[1].trim());
        this.minDamage = Integer.parseInt(tokens[2].trim());
        this.maxDamage = Integer.parseInt(tokens[3].trim());
        this.critChance = Float.parseFloat(tokens[4].trim());
        this.stunChance = Float.parseFloat(tokens[5].trim());
        this.bleedChance = Float.parseFloat(tokens[6].trim());
    }
}
