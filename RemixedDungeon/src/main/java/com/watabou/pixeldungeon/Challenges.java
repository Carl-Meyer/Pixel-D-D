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
package com.watabou.pixeldungeon;

import android.annotation.SuppressLint;

import com.nyrds.pixeldungeon.items.Treasury;
import com.watabou.pixeldungeon.items.DewVial;
import com.watabou.pixeldungeon.items.Dewdrop;
import com.watabou.pixeldungeon.items.Stylus;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Challenges {

	public static final int NO_FOOD				= 1;
	public static final int NO_ARMOR			= 2;
	public static final int NO_HEALING			= 4;
	public static final int NO_HERBALISM		= 8;
	public static final int SWARM_INTELLIGENCE	= 16;
	public static final int DARKNESS			= 32;
	public static final int NO_SCROLLS          = 64;
	public static final int NO_WEAPON           = 128;


	public static final int[] MASKS = {
		NO_FOOD, NO_ARMOR, NO_HEALING, NO_HERBALISM, SWARM_INTELLIGENCE, DARKNESS, NO_SCROLLS, NO_WEAPON
	};

	@SuppressLint("UseSparseArrays")
	private static Map<Integer, ArrayList<String>> forbiddenCategories = new HashMap<>();

	static {
		for(Integer mask:MASKS) {
			forbiddenCategories.put(mask, new ArrayList<>());
		}

		Objects.requireNonNull(forbiddenCategories.get(NO_FOOD)).add(Treasury.Category.FOOD.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_ARMOR)).add(Treasury.Category.ARMOR.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_HEALING)).add(PotionOfHealing.class.getSimpleName());
		Objects.requireNonNull(forbiddenCategories.get(NO_HERBALISM)).add(Treasury.Category.SEED.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_HERBALISM)).add(Dewdrop.class.getSimpleName());
		Objects.requireNonNull(forbiddenCategories.get(NO_HERBALISM)).add(DewVial.class.getSimpleName());
		Objects.requireNonNull(forbiddenCategories.get(NO_SCROLLS)).add(Treasury.Category.SCROLL.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_SCROLLS)).add(Stylus.class.getSimpleName());
		Objects.requireNonNull(forbiddenCategories.get(NO_WEAPON)).add(Treasury.Category.WEAPON.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_WEAPON)).add(Treasury.Category.WAND.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_WEAPON)).add(Treasury.Category.THROWABLE.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_WEAPON)).add(Treasury.Category.BULLETS.name());
		Objects.requireNonNull(forbiddenCategories.get(NO_WEAPON)).add(Treasury.Category.RANGED.name());
	}

	public static void forbidCategories(int challenge,Treasury treasury) {
		for(int mask:MASKS) {
			if((mask & challenge) != 0 && forbiddenCategories.containsKey(mask)) {
				for(String catOrItem: Objects.requireNonNull(forbiddenCategories.get(mask))) {
					treasury.forbid(catOrItem);
				}
			}
		}
	}
}
