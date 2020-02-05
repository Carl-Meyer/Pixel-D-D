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
package com.watabou.pixeldungeon.items.wands;

import com.nyrds.pixeldungeon.items.ItemUtils;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.ui.QuickSlot;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfMagicMissile extends SimpleWand  {

	public static final String AC_DISENCHANT    = "WandOfMagicMissile_ACDisenchant";
	
	private static final float TIME_TO_DISENCHANT	= 2f;
	
	private boolean disenchantEquipped;
	
	{
		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (effectiveLevel() > 0) {
			actions.add( AC_DISENCHANT );
		}
		return actions;
	}

	@Override
	protected void onZap( int cell ) {
		
		Char ch = Actor.findChar( cell );
		if (ch != null) {
			
			int level = effectiveLevel();
			
			ch.damage( Random.Int( 1, 6 + level * 2 ), this );
			
			ch.getSprite().burst( 0xFF99CCFF, level / 2 + 2 );
			
			if (ch == getUser() && !ch.isAlive()) {
				Dungeon.fail( Utils.format( ResultDescriptions.getDescription(ResultDescriptions.Reason.WAND), name, Dungeon.depth ) );
				GLog.n(Game.getVar(R.string.WandOfMagicMissile_Info1));
			}
		}
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_DISENCHANT )) {
			
			if (hero.getBelongings().weapon == this) {
				disenchantEquipped = true;
				hero.getBelongings().weapon = null;
                QuickSlot.refresh();
            } else {
				disenchantEquipped = false;
				detach( hero.getBelongings().backpack );
			}
			
			setUser(hero);
			GameScene.selectItem( itemSelector, WndBag.Mode.WAND, Game.getVar(R.string.WandOfMagicMissile_SelectWand) );
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean isKnown() {
		return true;
	}
	
	@Override
	public void setKnown() {
	}
	
	protected int initialCharges() {
		return 3;
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.WandOfMagicMissile_Info);
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				
				Sample.INSTANCE.play( Assets.SND_EVOKE );
				ScrollOfUpgrade.upgrade( getUser() );
				ItemUtils.evoke( getUser() );
				
				GLog.w( Game.getVar(R.string.WandOfMagicMissile_Desinchanted), item.name() );
				
				item.upgrade();
				getUser().spendAndNext( TIME_TO_DISENCHANT );
				
				Badges.validateItemLevelAcquired( item );
				
			} else {
				if (disenchantEquipped) {
					getUser().getBelongings().weapon = WandOfMagicMissile.this;
                    QuickSlot.refresh();
                } else {
					collect( getUser().getBelongings().backpack );
				}
			}
		}
	};
}
