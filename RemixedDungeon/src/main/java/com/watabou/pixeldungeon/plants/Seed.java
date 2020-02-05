package com.watabou.pixeldungeon.plants;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.CommonActions;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.food.Food;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.utils.Utils;

import java.util.ArrayList;

import lombok.SneakyThrows;

public class Seed extends Item {

    public static final String AC_PLANT = "Plant_ACPlant";

    private static final float TIME_TO_PLANT = 1f;

    {
        stackable = true;
        setDefaultAction(AC_THROW);
    }

    protected Class<? extends Plant> plantClass;
    protected String plantName;

    public Class<? extends Item> alchemyClass;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PLANT);
        actions.add(CommonActions.AC_EAT);
        return actions;
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.map[cell] == Terrain.ALCHEMY || Dungeon.level.pit[cell]) {
            super.onThrow(cell);
        } else {
            Dungeon.level.plant(this, cell);
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_PLANT)) {

            hero.spend(TIME_TO_PLANT);
            hero.busy();
            ((Seed) detach(hero.getBelongings().backpack)).onThrow(hero.getPos());

            hero.getSprite().operate(hero.getPos());

        } else if (action.equals(CommonActions.AC_EAT)) {
            detach(hero.getBelongings().backpack);

            hero.getSprite().operate(hero.getPos());
            hero.busy();

            SpellSprite.show(hero, SpellSprite.FOOD);
            Sample.INSTANCE.play(Assets.SND_EAT);

            hero.spend(Food.TIME_TO_EAT);
        }

        super.execute(hero, action);
    }


    @Override
    public Item burn(int cell){
        return null;
    }

    @SneakyThrows
    public Plant couch(int pos) {
        Sample.INSTANCE.play(Assets.SND_PLANT);
        Plant plant = plantClass.newInstance();
        plant.setPos(pos);
        return plant;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int price() {
        return 10 * quantity();
    }

    @Override
    public String info() {
        return Utils.format(Game.getVar(R.string.Plant_Info), Utils.indefinite(plantName), desc());
    }
}
