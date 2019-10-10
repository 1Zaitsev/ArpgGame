package com.arpg.game;
/*
1.обавить рестисты:   (это лучше в шмотки)
-стан;
-блид;

 */
public class Stats {
    private int level;

    private int att;
    private int def;
    private int hpMax;
    private int manaMax;
    private int endMax;

    private int attBase;
    private int defBase;
    private int hpMaxBase;
    private int manaMaxBase;
    private int endMaxBase;

    private int attPL;
    private int defPL;
    private int hpMaxPL;
    private int manaMaxPL;
    private int endMaxPL;

    private float speed;

    private int hp;
    private int mana;
    private int end;                // endurance

    private int exp;
    private int[] expTo = {1_000, 2_000, 4_000, 8_000, 16_000, 32_000, 80_000, 150_000, 200_000, 400_000};

    public int getAtt() {
        return att;
    }

    public int getDef() {
        return def;
    }

    public int getLevel() {
        return level;
    }

    public float getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getMana() {
        return mana;
    }
    public int getManaMax() {
        return manaMax;
    }

    public int getEnd() {
        return end;
    }

    public int getEndMax() {
        return endMax;
    }

    public Stats() {
    }

    public Stats(int level, int attBase, int defBase, int hpMaxBase,int manaMaxBase, int endMaxBase,
                            int attPL, int defPL, int hpMaxPL, int manaMaxPL, int endMaxPL, float speed) {

        this.level = level;
        this.attBase = attBase;
        this.defBase = defBase;
        this.hpMaxBase = hpMaxBase;
        this.manaMaxBase = manaMaxBase;
        this.endMaxBase = endMaxBase;
        this.attPL = attPL;
        this.defPL = defPL;
        this.hpMaxPL = hpMaxPL;
        this.manaMaxPL = manaMaxPL;
        this.endMaxPL = endMaxPL;
        this.speed = speed;

        this.calculate();
        this.fullStats();
    }

    public void set(int level, Stats stats) {
        this.level = level;
        this.attBase = stats.attBase;
        this.defBase = stats.defBase;
        this.hpMaxBase = stats.hpMaxBase;
        this.manaMaxBase = stats.manaMaxBase;
        this.endMaxBase = stats.endMaxBase;
        this.attPL = stats.attPL;
        this.defPL = stats.defPL;
        this.hpMaxPL = stats.hpMaxPL;
        this.manaMaxPL = stats.manaMaxPL;
        this.endMaxPL = stats.endMaxPL;
        this.speed = stats.speed;

        this.calculate();
        this.fullStats();
    }

    public void decreaseHp(int value) {
        hp -= value;
    }

    public void decreaseMana(int value){
        mana -= value;
    }

    public void decreaseEnd(int value){
        end -= value;
    }

    public void fullStats(){        //оставил отдельные методы, так как вероятно полное восстановление отдельного стата
        fullHp();
        fullMana();
        fullEnd();
    }

    public void fullHp() {
        hp = hpMax;
    }

    public void fullMana(){
        mana = manaMax;
    }

    public void fullEnd(){
        end = endMax;
    }

    public int increaseHp(int value) {
        int tempHp = hp;
        hp += value;
        if (hp > hpMax)
            hp = hpMax;
        return hp - tempHp;
    }

    public int increaseMana(int value) {
        int manaTemp = mana;
        mana += value;
        if(mana > manaMax)
            mana = manaMax;
        return mana - manaTemp;
    }

    public void increaseEnd(int value) {     //условие if(end != endMax) надо вынести выше
        if(end != endMax)
        end += value;
    }

    public void addExp(int amount) {
        exp += amount;
        if (exp >= expTo[level - 1]) {
            exp = 0;
            level++;
            calculate();
            fullStats();
        }
    }

    public void calculate() {
        att = attBase + level * attPL;
        def = defBase + level * defPL;
        hpMax = hpMaxBase + level * hpMaxPL;
        manaMax = manaMaxBase + level * manaMaxPL;
        endMax = endMaxBase + level * endMaxPL;
    }
}
