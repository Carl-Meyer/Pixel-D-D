package com.watabou.pixeldungeon.items.scrolls;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

public class ScrollOfSummoning extends Scroll {

	@Override
	protected void doRead() {
		Level level = Dungeon.level;

		if(level.isBossLevel() || !level.cellValid(level.randomRespawnCell())) {
			GLog.w( Utils.format(R.string.Using_Failed_Because_Magic, this.name()) );
			return;
		}

		int cell = level.getEmptyCellNextTo(getUser().getPos());

		if(level.cellValid(cell)){
			Mob mob = Bestiary.mob( level );
			GLog.i(Game.getVar(R.string.ScrollOfSummoning_Info_2));
			if(mob.canBePet()){
				Mob.makePet(mob, getUser().getId());
			} else {
				GLog.w( Utils.format(R.string.Mob_Cannot_Be_Pet, mob.getName()));
			}
			WandOfBlink.appear( mob, cell );
		} else {
			GLog.w(Game.getVar(R.string.No_Valid_Cell));
		}

		setKnown();

		SpellSprite.show( getUser(), SpellSprite.SUMMON );
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel(getUser());

		getUser().spendAndNext( TIME_TO_READ );
	}
}
