package com.nyrds.pixeldungeon.support;

import androidx.annotation.MainThread;

import com.appodeal.ads.Appodeal;
import com.nyrds.pixeldungeon.ml.EventCollector;
import com.watabou.noosa.Game;
import com.watabou.noosa.InterstitialPoint;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

class AdsUtilsCommon {

    static void bannerFailed(IBannerProvider provider) {
        incFailCount(AdsUtils.bannerFails,provider);
        //tryNextBanner();
    }

    static void interstitialFailed(IInterstitialProvider provider, InterstitialPoint retTo) {
        incFailCount(AdsUtils.interstitialFails,provider);
        retTo.returnToWork(false);
        //tryNextInterstitial(retTo);
    }

    private static <T> void incFailCount(Map<T,Integer> map, T provider) {
        Integer failCount = map.get(provider);
        if(failCount!=null) {
            map.put(provider,failCount+1);
        } else {
            map.put(provider,1);
        }
    }

    @Nullable
    private static <T extends IProvider> T choseLessFailedFrom(Map<T,Integer> map, int maxFails) {
        int minima = Integer.MAX_VALUE;

        T chosenProvider = null;

        for (T provider: map.keySet()) {
            if(provider.isReady()) {
                Integer failRate = map.get(provider);
                if (failRate != null && failRate < minima) {
                    minima = failRate;
                    chosenProvider = provider;
                }
            }
        }

        if(minima<maxFails) {
            return chosenProvider;
        }
        return null;
    }

    private static void tryNextRewardVideo(final InterstitialPoint retTo) {
        final IRewardVideoProvider chosenProvider = choseLessFailedFrom(AdsUtils.rewardVideoFails, Integer.MAX_VALUE);

        AppodealAdapter.logEcpm(Appodeal.REWARDED_VIDEO, chosenProvider instanceof AppodealInterstitialProvider);

        if(chosenProvider!=null) {
            Game.instance().runOnUiThread(() -> chosenProvider.showRewardVideo(retTo));
        } else {
            retTo.returnToWork(false);
        }
    }

    private static void tryNextInterstitial(final InterstitialPoint retTo) {
        final IInterstitialProvider chosenProvider = choseLessFailedFrom(AdsUtils.interstitialFails, Integer.MAX_VALUE);

        AppodealAdapter.logEcpm(Appodeal.INTERSTITIAL, chosenProvider instanceof AppodealInterstitialProvider);

        if(chosenProvider!=null) {
            Game.instance().runOnUiThread(() -> chosenProvider.showInterstitial(retTo));
        } else {
            retTo.returnToWork(false);
        }
    }

    static private void tryNextBanner() {
        IBannerProvider chosenProvider = choseLessFailedFrom(AdsUtils.bannerFails, Integer.MAX_VALUE);

        double appodealEcmp = AppodealAdapter.logEcpm(Appodeal.BANNER, chosenProvider instanceof AppodealInterstitialProvider);
        if( appodealEcmp > 1 ) {
            chosenProvider = AppodealBannerProvider.getInstance();
            EventCollector.logEvent("Banner_override", appodealEcmp);
        }

        if(chosenProvider!=null) {
            Game.instance().runOnUiThread(chosenProvider::displayBanner);
        }
    }

    static void displayTopBanner() {
        if(AdsUtils.bannerIndex()<0) {
            tryNextBanner();
        }
    }

    static void showInterstitial(InterstitialPoint retTo) {
        tryNextInterstitial(retTo);
    }


    static void showRewardVideo(InterstitialPoint retTo) {
        tryNextRewardVideo(retTo);
    }

    @MainThread
    static boolean isRewardVideoReady() {
        for(IRewardVideoProvider provider: AdsUtils.rewardVideoFails.keySet()) {
            if(provider.isReady()) {
                return true;
            }
        }
        return false;
    }

    interface IProvider {
        boolean isReady();
    }

    interface IBannerProvider extends IProvider {
        void displayBanner();
    }

    interface IInterstitialProvider extends IProvider  {
        void showInterstitial(final InterstitialPoint ret);
    }

    interface IRewardVideoProvider extends IProvider {
        void showRewardVideo(final InterstitialPoint ret);
    }
}
