package com.arpg.game;

import com.arpg.game.utils.Poolable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Monster extends Unit implements Poolable {

    private Unit target;
    private String title;
    private float aiTimer;
    private float aiTimerTo;

    public String getTitle() {
        return title;
    }

    @Override
    public boolean isActive() {
        return stats.getHp() > 0;
    }

    public Monster(GameScreen gameScreen) {
        super(gameScreen);
        this.stats = new Stats();
        this.weapon = new Weapon("Bite", 0.8f, 2, 5,0,0,0);
    }

    // _title_,__base_att_,_base_def_,_base_hp_,_base_mana_,_base_end_,_att_pl_,_def_pl_,_hp_pl_,_mana_pl_,_end_pl_,_speed__
    public Monster(String line) {
        super(null);
        String[] tokens = line.split(",");
        this.title = tokens[0].trim();
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion(title)).split(80, 80);
        this.stats = new Stats(
                1,
                Integer.parseInt(tokens[1].trim()),
                Integer.parseInt(tokens[2].trim()),
                Integer.parseInt(tokens[3].trim()),
                Integer.parseInt(tokens[4].trim()),
                Integer.parseInt(tokens[5].trim()),
                Integer.parseInt(tokens[6].trim()),
                Integer.parseInt(tokens[7].trim()),
                Integer.parseInt(tokens[8].trim()),
                Integer.parseInt(tokens[9].trim()),
                Integer.parseInt(tokens[10].trim()),
                Float.parseFloat(tokens[11].trim()));
        this.weapon = new Weapon("Bite", 0.8f, 2, 5,0,0,0);
    }

    public void setup(int level, float x, float y, Monster pattern) {
        this.stats.set(level, pattern.stats);
        this.title = pattern.title;
        this.texture = pattern.texture;
        if (x < 0 && y < 0) {
            this.gs.getMap().setRefVectorToEmptyPoint(position);
        } else {
            this.position.set(x, y);
        }
        this.area.setPosition(position);
        this.bleedTimer = 0f;
        this.stunTimer = 0f;

    }

    @Override
    public void update(float dt) {
        aiTimer += dt;
        attackTime += dt;
        regenTimer += dt;


            if(this.stats.getEnd() < this.stats.getEndMax() && regenTimer > 1){
                int regenMod = 2;
                if(state == state.HUNT || state == state.WALK)
                    regenMod /= 2;
                this.stats.increaseEnd(regenMod * this.stats.getLevel());
                this.regenTimer = 0f;
            }

            if(this.state == State.BLEED){
                if(aiTimer > 1){
                    takeDamage(gs.getHero(), 1, Color.WHITE);
                    bleedTimer--;
                    aiTimer = 0f;
                }
                if(bleedTimer == 0){
                    state = State.values()[MathUtils.random(1, 2)];
                }
            }

            if(this.state == State.STUN) {
                if(aiTimer > 1){
                    stunTimer--;
                    aiTimer = 0f;
                }
                if (stunTimer == 0f) {
                    state = State.values()[MathUtils.random(1, 2)];
                }
            }

            else {

                if (damageTimer > 0.0f) {
                    damageTimer --;
                }

                if (aiTimer > aiTimerTo) {
                    state = State.values()[MathUtils.random(1, 2)]; // IDLE or WALK
                    aiTimer = 0.0f;
                    aiTimerTo = MathUtils.random(2.0f, 4.0f);
                    if (state == State.IDLE) {
                        aiTimerTo /= 4.0f;
                    }
                    direction = Direction.values()[MathUtils.random(0, 3)];
                }

                if (state == State.HUNT) {
                    if (Math.abs(target.getPosition().x - this.position.x) > 30.0f) {
                        if (target.getPosition().x > this.position.x) {
                            direction = Direction.RIGHT;
                        }
                        if (target.getPosition().x < this.position.x) {
                            direction = Direction.LEFT;
                        }
                    }
                    if (Math.abs(target.getPosition().y - this.position.y) > 30.0f) {
                        if (target.getPosition().y > this.position.y) {
                            direction = Direction.UP;
                        }
                        if (target.getPosition().y < this.position.y) {
                            direction = Direction.DOWN;
                        }
                    }
                }

                if (this.state != State.IDLE) {
                    tmp.set(position).add(direction.getX() * stats.getSpeed() * dt, direction.getY() * stats.getSpeed() * dt);
                    if (gs.getMap().isCellPassable(tmp)) {
                        position.set(tmp);
                        walkTimer += dt;
                        area.setPosition(position);
                    }

                    tryToAttack();
                }
            }
    }

    @Override
    public void takeDamage(Unit attacker, int amount, Color color) {
        super.takeDamage(attacker, amount, color);
        if (MathUtils.random(0, 100) < 20 && this.state != State.BLEED ) {
            stateToHunt(attacker);
        }
    }

    public void stateToHunt(Unit target) {
        this.state = State.HUNT;
        this.target = target;
        this.aiTimerTo = 15.0f;
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        super.render(batch, font);
        font.draw(batch, state.name(), position.x + 20, position.y + 60);
    }

    public void tryToAttack() {
        if (attackTime > weapon.getAttackPeriod()) {
            attackTime = 0.0f;
            tmp.set(position).add(direction.getX() * 60, direction.getY() * 60);
            if (gs.getHero().getArea().contains(tmp)) {
                gs.getEffectController().setup(tmp.x, tmp.y, 1);
                gs.getHero().takeDamage(this, BattleCalc.calculateDamage(this, gs.getHero()), Color.RED);
                this.stats.decreaseEnd(20);
            }
        }
    }
}
