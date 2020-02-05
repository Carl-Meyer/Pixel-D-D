package com.nyrds.pixeldungeon.items.common;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.hero.HeroSubClass;
import com.watabou.pixeldungeon.actors.mobs.Boss;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.actors.mobs.npcs.NPC;
import com.watabou.pixeldungeon.items.weapon.melee.SpecialWeapon;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;

public class SacrificialSword extends SpecialWeapon {
	{
		imageFile = "items/swords.png";
		image = 4;
		enchatable = false;
		animation_class = SWORD_ATTACK;
	}
	
	public SacrificialSword() {
		super( 2, 1f, 1f );
	}
	
	@Override
	public Glowing glowing() {
		
		float period = (float) Math.max(0.1, 0.1/baseChance(Dungeon.hero));
		//GLog.i("base chance: %.3f %.3f",baseChance(Dungeon.hero), period);
		
		return new Glowing(0xFF4466, period);
		
	}

	private double baseChance(Hero hero) {
		double armorPenalty = 1;
		
		if(hero.getBelongings().armor != null) {
			armorPenalty += hero.getBelongings().armor.tier;
		}
		
		double classBonus = 1;
		if(hero.getSubClass() == HeroSubClass.WARDEN ) {
			classBonus = 1.5;
		}
		
		if(hero.getSubClass() == HeroSubClass.SHAMAN) {
			classBonus = 2.0;
		}

		if(hero.getHeroClass() == HeroClass.NECROMANCER) {
			classBonus = 3.0;
		}

		return (0.25 + (hero.lvl() * 4 + Math.pow(level(),2)) * 0.01) * classBonus / armorPenalty;
	}
	
	public void postAttack(Hero user, Char tgt ) {
		
		if(tgt instanceof Boss) {
			return;
		}
		
		if(tgt instanceof NPC) {
			return;
		}
		
		if(! (tgt instanceof Mob) ) {
			return;
		}

		if(!(user instanceof Hero)) {
			return;
		}

		Hero hero = user;
		Mob mob = (Mob) tgt;

		double conversionChance =     baseChance(hero) + 
									- mob.defenseSkill(hero)*0.01*mob.speed()
									- tgt.hp()*0.01;

		double roll = Math.random();
		
		//GLog.i("chance %.3f roll %.3f\n", conversionChance, roll);

		if(roll < conversionChance ) {
			Mob.makePet(mob, hero.getId());
		}
	}
}
