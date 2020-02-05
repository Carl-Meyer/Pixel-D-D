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
package com.watabou.pixeldungeon.actors.mobs;

import com.nyrds.pixeldungeon.ai.MobAi;
import com.nyrds.pixeldungeon.ai.Passive;
import com.nyrds.pixeldungeon.items.Treasury;
import com.nyrds.pixeldungeon.mechanics.NamedEntityKind;
import com.nyrds.pixeldungeon.ml.EventCollector;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.CharUtils;
import com.watabou.pixeldungeon.actors.blobs.ToxicGas;
import com.watabou.pixeldungeon.actors.buffs.Bleeding;
import com.watabou.pixeldungeon.actors.buffs.Poison;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.items.weapon.Weapon.Enchantment;
import com.watabou.pixeldungeon.items.weapon.enchantments.Death;
import com.watabou.pixeldungeon.items.weapon.enchantments.Leech;
import com.watabou.pixeldungeon.items.weapon.melee.Dagger;
import com.watabou.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.sprites.HeroSpriteDef;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class Statue extends Mob {
	
	protected Weapon weapon;
	
	public Statue() {
		exp = 0;
		setState(MobAi.getStateByClass(Passive.class));

		hp(ht(15 + Dungeon.depth * 5));
		defenseSkill = 4 + Dungeon.depth;

		addImmunity( ToxicGas.class );
		addImmunity( Poison.class );
		addResistance( Death.class );
		addResistance( ScrollOfPsionicBlast.class );
		addImmunity( Leech.class );
		addImmunity(Bleeding.class);
	}
	
	private static final String WEAPON	= "weapon";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WEAPON, getWeapon());
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		weapon = (Weapon)bundle.get( WEAPON );
	}
	
	@Override
    public boolean act() {
		if (!isPet() && CharUtils.isVisible(this)) {
			Journal.add( Journal.Feature.STATUE.desc() );
		}
		return super.act();
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( getWeapon().MIN, getWeapon().MAX );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return (int)((9 + Dungeon.depth) * getWeapon().ACU);
	}
	
	@Override
	protected float _attackDelay() {
		return getWeapon().DLY;
	}
	
	@Override
	public int dr() {
		return Dungeon.depth;
	}

	@Override
	public int attackProc(@NotNull Char enemy, int damage ) {
		getWeapon().attackProc( this, enemy, damage );
		return damage;
	}
	
	@Override
	public void beckon( int cell ) {
	}
	
	@Override
	public void die(NamedEntityKind cause) {
		Dungeon.level.drop(getWeapon(), getPos() ).sprite.drop();
		super.die( cause );
	}
	
	@Override
	public void destroy() {
		Journal.remove( Journal.Feature.STATUE.desc() );
		super.destroy();
	}
	
	@Override
	public boolean reset() {
		setState(MobAi.getStateByClass(Passive.class));
		return true;
	}

	@Override
	public String description() {
		return Utils.format(Game.getVar(R.string.Statue_Desc), getWeapon().name());
	}

	@Override
	public CharSprite sprite() {
		if(getWeapon() ==null) {
			weapon = new Dagger();
			EventCollector.logException("no weapon");
		}

		return HeroSpriteDef.createHeroSpriteDef(getWeapon());
	}

	public Weapon getWeapon() {
		if(weapon==null) {
			Item weaponCandidate;
			do {
				weaponCandidate = Treasury.getLevelTreasury().random(Treasury.Category.WEAPON );
			} while (!(weaponCandidate instanceof MeleeWeapon) || weaponCandidate.level() < 0);
			weapon = (Weapon) weaponCandidate;

			weapon.identify();
			weapon.enchant( Enchantment.random() );
		}
		return weapon;
	}
}
