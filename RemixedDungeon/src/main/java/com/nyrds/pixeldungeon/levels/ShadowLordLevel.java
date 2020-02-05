package com.nyrds.pixeldungeon.levels;

import com.nyrds.pixeldungeon.mobs.common.ShadowLord;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.levels.BossLevel;

public class ShadowLordLevel extends BossLevel {

	public ShadowLordLevel() {
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = 3;
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_SHADOW_LORD;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	protected boolean build() {
		Tools.makeShadowLordLevel(this);
		return true;
	}

	@Override
	protected void decorate() {
	}

	@Override
	protected void createMobs() {
		ShadowLord lord = new ShadowLord();
		lord.setPos(cell(width/2,height / 2));
		mobs.add(lord);
	}

	@Override
	protected void createItems() {
	}
}
