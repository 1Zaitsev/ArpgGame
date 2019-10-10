package com.arpg.game;

public class BattleCalc {
    public static int calculateDamage(Unit attacker, Unit target) {
        int baseDamage = attacker.getWeapon().getDamage();
        int diffAD = attacker.getStats().getAtt() - target.getStats().getDef();
        int diffLevel = attacker.getStats().getLevel() - target.getStats().getLevel();
        int outDamage = (int) (baseDamage * (1 + diffAD * 0.05f) * (1 + diffLevel * 0.1f));
        if (outDamage < 1) {
            outDamage = 1;
        }
        return outDamage;
    }

    public static int calculateExp(Unit attacker, Unit target) {
        int exp = target.getStats().getHpMax() * 20;
        int diffLevel = attacker.getStats().getLevel() - target.getStats().getLevel();
        exp *= (1.0f + diffLevel * 0.2f);
        if (exp < 0) {
            exp = 0;
        }
        return exp;
    }

    public static int calculateScore(Unit attacker, Unit target){
        int att = target.getStats().getAtt() - attacker.getStats().getAtt();
        int def = target.getStats().getDef() - attacker.getStats().getDef();
        int score = att + def;
        if (score < 0){
            score = 0;
        }
        return score;
    }
}
