package com.watabou.pixeldungeon.items.armor;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.utils.GLog;

public class FreeRunnerArmor extends RogueArmor {

	{
		name = Game.getVar(R.string.RogueArmor_Name);
		image = 9;
		hasHelmet = true;
	}
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.getSubClass() == HeroSubClass.FREERUNNER) {
			return super.doEquip( hero );
		} else {
			GLog.w( Game.getVar(R.string.RogueArmor_NotRogue) );
			return false;
		}
	}
}