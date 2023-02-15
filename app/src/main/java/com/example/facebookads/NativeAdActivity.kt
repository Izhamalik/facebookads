package com.example.facebookads

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*


class NativeAdActivity : AppCompatActivity() {
    private var interstitialAd: InterstitialAd? = null
    private val TAG: String = "NativeBannerAdActivity::class.java.getSimpleName()"
    private val nativeBannerAd: NativeBannerAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)
        AudienceNetworkAds.initialize(this)

        interstitialAd = InterstitialAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
        // Create listeners for the Interstitial Ad
        // Create listeners for the Interstitial Ad
        @SuppressLint("LongLogTag")
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad?) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad?) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad?, adError: AdError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage())
            }

            override fun onAdLoaded(ad: Ad?) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!")
                // Show the ad
                interstitialAd!!.show()
            }

            override fun onAdClicked(ad: Ad?) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!")
            }


            override fun onLoggingImpression(ad: Ad?) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!")
            }
        }

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd!!.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )


    }
}