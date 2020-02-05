package com.watabou.pixeldungeon.windows;

import com.nyrds.pixeldungeon.ml.EventCollector;
import com.nyrds.pixeldungeon.ml.R;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.watabou.pixeldungeon.ui.IconButton;
import com.watabou.pixeldungeon.ui.Icons;

class WndDontLikeAds extends WndQuest {

    public WndDontLikeAds() {
        super(new Shopkeeper(), Game.getVar(R.string.WndSaveSlotSelect_dontLike));

        float y = height;

        IconButton btnDonate = new IconButton(R.string.DonateButton_pleaseDonate,Icons.SUPPORT.get() ){
            @Override
            protected void onClick() {
                WndDontLikeAds.this.add(new WndDonate());
                EventCollector.logEvent(EventCollector.SAVE_ADS_EXPERIMENT,"DonateButtonClicked");
            }
        };
        btnDonate.setSize(width-6*GAP, BUTTON_HEIGHT);
        btnDonate.setPos((width - btnDonate.width()) / 2, y + GAP*4);
        add(btnDonate);

        y=btnDonate.bottom();
/*
        RedButton btnNo = new RedButton(R.string.WndDontLikeAds_NotThisTime){
            @Override
            protected void onClick() {
                EventCollector.logEvent(EventCollector.SAVE_ADS_EXPERIMENT,"NotThisTimeClicked");
                hide();
            }
        };
        btnNo.setSize(width-6*GAP, BUTTON_HEIGHT);
        btnNo.setPos((width - btnNo.width()) / 2, y + GAP*2);

        add(btnNo);
*/
        resize(width, (int) btnDonate.bottom()+GAP);
    }

    @Override
    public void hide() {
        super.hide();
        EventCollector.logEvent(EventCollector.SAVE_ADS_EXPERIMENT,"DialogClosed");
    }
}
