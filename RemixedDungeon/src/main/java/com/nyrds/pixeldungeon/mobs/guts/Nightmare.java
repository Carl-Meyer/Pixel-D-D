package com.nyrds.pixeldungeon.mobs.guts;


import com.nyrds.pixeldungeon.ai.Hunting;
import com.nyrds.pixeldungeon.ai.MobAi;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.buffs.Roots;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

/**
 * Created by DeadDie on 12.02.2016
 */
public class Nightmare extends Mob {

    {
        hp(ht(80));
        defenseSkill = 24;

        exp = 0;
    }

    @Override
    public int attackProc(@NotNull Char enemy, int damage ) {
        //Roots proc
        if (Random.Int(10) == 1){
            Buff.affect(enemy, Roots.class);
        }
        //Paralysis proc
        if (Random.Int(10) == 1){
            Buff.affect(enemy, Paralysis.class);
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(20, 25);
    }

    @Override
    public int attackSkill( Char target ) { return 26; }

    @Override
    public int dr() { return 10; }

    @Override
    public boolean act(){
        super.act();

        setState(MobAi.getStateByClass(Hunting.class));

        return true;
    }
}
