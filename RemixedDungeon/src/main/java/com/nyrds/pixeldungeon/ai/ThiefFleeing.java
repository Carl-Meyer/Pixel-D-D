package com.nyrds.pixeldungeon.ai;

import com.nyrds.pixeldungeon.mechanics.NamedEntityKind;
import com.nyrds.pixeldungeon.ml.R;
import com.nyrds.pixeldungeon.utils.CharsList;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.utils.Utils;

public class ThiefFleeing extends MobAi implements AiState {

    public ThiefFleeing() {
    }

    @Override
    public void act(Mob me) {
        me.enemySeen = me.isEnemyInFov();
        if (me.enemySeen) {
            me.target = me.getEnemy().getPos();
        }

        if(!me.doStepFrom(me.target)) {
            me.spend(Actor.TICK);
            me.getSprite().showStatus( CharSprite.NEGATIVE, Mob.TXT_RAGE );
            me.setState(MobAi.getStateByClass(Hunting.class));
        }
    }

    @Override
    public String status(Mob me) {
        Char enemy = me.getEnemy();
        if(enemy != CharsList.DUMMY) {
            return Utils.format(Game.getVar(R.string.Mob_StaFleeingStatus2),
                    me.getName(), enemy.getName_objective());
        }
        return Utils.format(Game.getVar(R.string.Mob_StaFleeingStatus),
                me.getName());
    }

    @Override
    public void gotDamage(Mob me, NamedEntityKind src, int dmg) {
        seekRevenge(me,src);
    }
}
