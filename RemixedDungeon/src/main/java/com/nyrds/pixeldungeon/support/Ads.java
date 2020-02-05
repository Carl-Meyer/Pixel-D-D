package com.nyrds.pixeldungeon.support;

import com.watabou.noosa.Game;
import com.watabou.noosa.InterstitialPoint;
import com.watabou.pixeldungeon.RemixedDungeon;

/**
 * Created by mike on 24.05.2016.
 */
public class Ads {

    private static boolean isSmallScreen() {
        return (Game.width() < 400 || Game.height() < 400);
    }

    public static void displayEasyModeBanner() {
        if (!isSmallScreen()) {
            AdsUtilsCommon.displayTopBanner();
        }
    }

    public static boolean isRewardVideoReady(){
        return AdsUtilsCommon.isRewardVideoReady();
    }

    public static void showRewardVideo(final InterstitialPoint work) {
        AdsUtilsCommon.showRewardVideo(work);
    }

    public static void displaySaveAndLoadAd(final InterstitialPoint work) {
        AdsUtilsCommon.showInterstitial(work);
    }
}
