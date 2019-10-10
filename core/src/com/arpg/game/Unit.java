package com.arpg.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Unit implements MapElement {
    protected GameScreen gs;
    protected TextureRegion[][] texture;
    protected TextureRegion hpTexture;
    protected Vector2 position;
    protected Direction direction;
    protected Vector2 tmp;
    protected Circle area;
    protected Stats stats;
    protected State state;
    protected float damageTimer;
    protected Weapon weapon;
    protected float attackTime;
    protected float walkTimer;
    protected float timePerFrame;

    protected float regenTimer;
    protected float runTimer;

    protected float stunTimer;
    protected float bleedTimer;

    public Stats getStats() {
        return stats;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public int getCellX() {
        return (int) (position.x / 80);
    }

    @Override
    public int getCellY() {
        return (int) (position.y / 80);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Circle getArea() {
        return area;
    }

    public Unit(GameScreen gameScreen) {
        this.gs = gameScreen;
        this.hpTexture = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.position = new Vector2(0.0f, 0.0f);
        this.area = new Circle(0, 0, 32);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.timePerFrame = 0.1f;
        this.direction = Direction.DOWN;
    }

    public void takeDamage(Unit attacker, int amount, Color color) {
        int critMod = 1;
        if(this.state != State.BLEED) {
            if (MathUtils.random(1) < attacker.getWeapon().getCritChance())
                critMod = 2;

            if (MathUtils.random(1) < attacker.getWeapon().getStanChance())
                this.stateToStun();


            if (MathUtils.random(1) < attacker.getWeapon().getBleedChance())
                this.stateToBleed();
        }

        stats.decreaseHp(amount * critMod);
        damageTimer = 1.0f;
        gs.getInfoController().setup(position.x, position.y + 30, "-" + amount, color);

        if (stats.getHp() <= 0) {
            int exp = BattleCalc.calculateExp(attacker, this);
            int score = BattleCalc.calculateScore(attacker, this);
            gs.getHero().addExp(score);
            attacker.getStats().addExp(exp);
            gs.getInfoController().setup(attacker.getPosition().x, attacker.getPosition().y + 40, "exp +" + exp, Color.YELLOW);
            gs.getPowerUpsController().setup(position.x, position.y, 1.2f, 2, stats.getLevel());
        }
    }

    public TextureRegion getCurrentTexture() {
        return texture[direction.getImageIndex()][(int) (walkTimer / timePerFrame) % texture[direction.getImageIndex()].length];
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        if (damageTimer > 0.0f) {
            batch.setColor(1.0f, 1.0f - damageTimer, 1.0f - damageTimer, 1.0f);
        }
        batch.draw(getCurrentTexture(), position.x - 40, position.y - 20);
        if (stats.getHp() < stats.getHpMax()) {
            batch.setColor(1.0f, 1.0f, 1.0f, 0.9f);
            batch.draw(hpTexture, position.x - 40, position.y + 40, 80 * ((float) stats.getHp() / stats.getHpMax()), 12);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, "" + stats.getLevel(), position.x, position.y + 50);
    }

    public void stateToWalk(){
        this.state = State.WALK;
    }

    public void stateToSprint(){
        this.state = State.SPRINT;
    }

    public void stateToStun() {
        this.state = State.STUN;
        this.stunTimer = 3f;      //вычислять из стат
    }

    public void stateToBleed() {
        this.state = State.BLEED;
        this.bleedTimer = 5f;     //вычислять из стат
    }

    public abstract void update(float dt);
}