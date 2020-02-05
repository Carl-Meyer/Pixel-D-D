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
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private int specialCostModifier = 3;
	
	{
		setLevelKnown(true);
		cursedKnown = true;
		setDefaultAction(special());
	}
	
	public ClassArmor() {
		super( 6 );
	}
	
	public static Armor upgrade (Char owner, Armor armor ) {

		ClassArmor classArmor;
		if(owner.getSubClass() == HeroSubClass.NONE) {
			classArmor = owner.getHeroClass().classArmor();
		} else {
			classArmor = owner.getSubClass().classArmor();
		}

		classArmor.STR = armor.STR;
		classArmor.DR  = armor.DR;
		
		classArmor.inscribe( armor.glyph );
		
		return classArmor;
	}
	
	private static final String ARMOR_STR	= "STR";
	private static final String ARMOR_DR	= "DR";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_STR, STR );
		bundle.put( ARMOR_DR, DR );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		STR = bundle.getInt( ARMOR_STR );
		DR = bundle.getInt( ARMOR_DR );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.getSkillPoints() >= hero.getSkillPointsMax()/specialCostModifier + 1 && isEquipped( hero )) {
			actions.add( special() );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals(special())) {

			int cost = hero.getSkillPointsMax()/specialCostModifier;

			if (hero.getSkillPoints() < cost) {
				GLog.w( Game.getVar(R.string.ClassArmor_LowMana) );
				return;
			}
			if (!isEquipped( hero )) {
				GLog.w( Game.getVar(R.string.ClassArmor_NotEquipped) );
				return;
			}

			setUser(hero);
			doSpecial();
			hero.spendSkillPoints(cost);
			return;
		}

		super.execute( hero, action );
	}
	
	abstract public String special();
	abstract public void doSpecial();
	
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
		return 0;
	}
}
