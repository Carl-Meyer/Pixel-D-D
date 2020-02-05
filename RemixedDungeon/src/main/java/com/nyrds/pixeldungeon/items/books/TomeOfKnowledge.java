package com.nyrds.pixeldungeon.items.books;

import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.effects.SpellSprite;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.CharSprite;

public class TomeOfKnowledge extends Book {

	{
		image = 1;
	}

	@Override
	protected void doRead(Hero hero) {
		curItem = detach( hero.getBelongings().backpack );

		SpellSprite.show( getUser(), SpellSprite.MASTERY );
		getUser().getSprite().emitter().burst( Speck.factory( Speck.MAGIC ), 8 );
		hero.getSprite().showStatus( CharSprite.BLUE, "+ 1");
		Sample.INSTANCE.play( Assets.SND_READ );

		getUser().spendAndNext( TIME_TO_READ );
		getUser().busy();
		hero.skillLevelUp();
	}

	@Override
	public int price() {
		return 100 * quantity();
	}

	@Override
	public Item burn(int cell){
		return null;
	}
}
