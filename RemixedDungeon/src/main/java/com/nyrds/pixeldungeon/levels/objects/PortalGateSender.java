package com.nyrds.pixeldungeon.levels.objects;

import com.nyrds.pixeldungeon.ml.R;
import com.nyrds.pixeldungeon.utils.Position;
import com.nyrds.pixeldungeon.windows.WndPortal;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Amulet;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class PortalGateSender extends PortalGate {

	private static final String TARGET = "target";

	protected Position target;

	public boolean portalInteract(Hero hero) {
		if(!used && hero.getBelongings().getItem(Amulet.class) == null){
			if(!animationRunning){
				if (!activated){
					playStartUpAnim();
				} else {
					GameScene.show(new WndPortal(this, hero, target));
				}
			}
		} else{
			GLog.w( Game.getVar(R.string.PortalGate_Used) );
		}
		return false;
	}

	@Override
	void setupFromJson(Level level, JSONObject obj) throws JSONException {
		super.setupFromJson(level, obj);

		if(obj.has("target")){
			JSONObject portalDesc = obj.getJSONObject("target");
			String levelId = portalDesc.optString("levelId" ,"1");
			target = new Position(levelId, portalDesc.optInt("x" ,1), portalDesc.optInt("y" ,1));
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		target = (Position) bundle.get(TARGET);
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(TARGET, target);
	}
}
