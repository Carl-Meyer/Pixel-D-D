/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.items.armor;

import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Invisibility;
import com.watabou.pixeldungeon.actors.buffs.Paralysis;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.mechanics.Ballistica;
import com.watabou.pixeldungeon.scenes.CellSelector;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.HeroSpriteDef;
import com.watabou.pixeldungeon.utils.GLog;

public class WarriorArmor extends ClassArmor {
	
	private static int LEAP_TIME	= 1;
	private static int SHOCK_TIME	= 3;

	{
		image = 5;
	}
	
	@Override
	public String special() {
		return "WarriorArmor_ACSpecial";
	}
	
	@Override
	public void doSpecial() {
		GameScene.selectCell( leaper );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.getHeroClass() == HeroClass.WARRIOR) {
			return super.doEquip( hero );
		} else {
			GLog.w( Game.getVar(R.string.WarriorArmor_NotWarrior) );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.WarriorArmor_Desc);
	}

	protected static CellSelector.Listener leaper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null && target != getUser().getPos()) {
				
				int cell = Ballistica.cast( getUser().getPos(), target, false, true );
				if (Actor.findChar( cell ) != null && cell != getUser().getPos()) {
					cell = Ballistica.trace[Ballistica.distance - 2];
				}
				
				getUser().checkIfFurious();
				
				Invisibility.dispel(getUser());
				
				final int dest = cell;
				getUser().busy();
				((HeroSpriteDef) getUser().getSprite()).jump( getUser().getPos(), cell, () -> {
					getUser().placeTo( dest );
					Dungeon.level.press( dest, getUser() );
					Dungeon.observe();

					for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
						Char mob = Actor.findChar( getUser().getPos() + Level.NEIGHBOURS8[i] );
						if (mob != null && mob != getUser()) {
							Buff.prolong( mob, Paralysis.class, SHOCK_TIME );
						}
					}

					CellEmitter.center( dest ).burst( Speck.factory( Speck.DUST ), 10 );
					Camera.main.shake( 2, 0.5f );

					getUser().spendAndNext( LEAP_TIME );
				});
			}
		}
		
		@Override
		public String prompt() {
			return Game.getVar(R.string.WarriorArmor_Prompt);
		}
	};
}