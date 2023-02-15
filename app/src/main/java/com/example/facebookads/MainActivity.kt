package com.example.facebookads


import android.R.bool
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*


class MainActivity : AppCompatActivity() {
    private var adView2: LinearLayout? = null
    private var adView1: AdView? = null
    private var nativeBannerAd1: NativeBannerAd? = null
    private var interstitialAd: InterstitialAd? = null
    private val isLoaded: bool? = null
    private val TAG = "nativeAdrequest"
    private var nativeAd: NativeAd? = null
    private var nativeAdLayout1: NativeAdLayout? = null
    private var nativeAdLayout: NativeAdLayout? = null
    private var adView: LinearLayout? = null
    private var nativeBannerAd: NativeBannerAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AudienceNetworkAds.initialize(this)
        loadNativeAd()
        adView1 = AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50)
        val adContainer = findViewById<View>(R.id.banner_container) as LinearLayout
        adContainer.addView(adView1)
        adView1?.loadAd()
        findViewById<Button>(R.id.nativeLayout).setOnClickListener {
            startActivity(Intent(this, NativeAdActivity::class.java))
        }
    }

    private fun loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = NativeBannerAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
        val nativeAdListener: NativeAdListener = object : NativeAdListener {
            override fun onMediaDownloaded(ad: Ad?) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.")
            }

            override fun onError(ad: Ad?, adError: AdError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage())
            }

            override fun onAdLoaded(ad: Ad?) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!")
                inflateAdBanner(nativeBannerAd!!)
            }

            override fun onAdClicked(ad: Ad?) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad?) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!")
            }
        }

        // Request an ad
        nativeBannerAd!!.loadAd(
            nativeBannerAd!!.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build()
        )
    }

    private fun inflateAd(nativeAd: NativeAd) {
        nativeAd.unregisterView()

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById<NativeAdLayout>(R.id.native_ad_container)
        val inflater = LayoutInflater.from(this)
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView =
            inflater.inflate(R.layout.nativeadlayoutbanner, nativeAdLayout, false) as LinearLayout
        nativeAdLayout!!.addView(adView)

        // Add the AdOptionsView
        val adChoicesContainer = findViewById<LinearLayout>(R.id.ad_choices_container)
        val adOptionsView = AdOptionsView(this, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdIcon: MediaView = adView!!.findViewById(R.id.native_ad_icon)
        val nativeAdTitle = adView!!.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdMedia: MediaView = adView!!.findViewById(R.id.native_ad_media)
        val nativeAdSocialContext = adView!!.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdBody = adView!!.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adView!!.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction = adView!!.findViewById<Button>(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation

        // Create a list of clickable views
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
            adView, nativeAdMedia, nativeAdIcon, clickableViews
        )
    }

    private fun inflateAdBanner(nativeBannerAd: NativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView()

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_banner_ad_container)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = inflater.inflate(
            R.layout.nativeadlayoutbanner,
            nativeAdLayout,
            false
        ) as LinearLayout
        nativeAdLayout!!.addView(adView)

        // Add the AdChoices icon
        val adChoicesContainer : RelativeLayout=adView!!.findViewById(R.id.ad_choices_container)
        val adOptionsView =
            AdOptionsView(this, nativeBannerAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)

        // Create native UI using the ad metadata.
        val nativeAdTitle: TextView = adView!!.findViewById(R.id.native_ad_title)
        val nativeAdSocialContext: TextView = adView!!.findViewById(R.id.native_ad_social_context)
        val sponsoredLabel: TextView = adView!!.findViewById(R.id.native_ad_sponsored_label)
        val nativeAdIconView: MediaView = adView!!.findViewById(R.id.native_icon_view)
        val nativeAdCallToAction: Button = adView!!.findViewById(R.id.native_ad_call_to_action)

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction())
        nativeAdCallToAction.setVisibility(
            if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        )
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName())
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext())
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation())

        // Register the Title and CTA button to listen for clicks.
        val clickableViews: MutableList<View> = java.util.ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews)
    }
}
