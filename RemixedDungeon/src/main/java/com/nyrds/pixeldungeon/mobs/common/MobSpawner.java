package com.nyrds.pixeldungeon.mobs.common;

import com.nyrds.pixeldungeon.ai.MobAi;
import com.nyrds.pixeldungeon.ai.Wandering;
import com.nyrds.pixeldungeon.mobs.necropolis.JarOfSouls;
import com.watabou.pixeldungeon.actors.mobs.Bestiary;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.levels.Level;

import org.jetbrains.annotations.NotNull;

public class MobSpawner {

	@NotNull
	static public Mob spawnRandomMob(Level level, int position) {
		Mob mob = Bestiary.mob(level);
		mob.setPos(position);
		mob.setState(MobAi.getStateByClass(Wandering.class));
		level.spawnMob(mob);
		return mob;
	}

	static public void spawnJarOfSouls(@NotNull Level level, int position) {
		Mob mob = new JarOfSouls();
		mob.setPos(position);
		mob.setState(MobAi.getStateByClass(Wandering.class));
		level.spawnMob(mob);
	}

}
