// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.samples.ads.debugsettings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.ads.AdSettings;

import java.util.ArrayList;

import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;
import static com.facebook.ads.AdSettings.MultiprocessSupportMode.MULTIPROCESS_SUPPORT_MODE_AUTO;

public class DebugSettings {
    public static final String  PREFERENCES_FILE = "debug_preferences.xml";

    public static final String  SDK_VERSION_KEY = "sdk_version_key";
    public static final String  TEST_MODE_KEY = "test_mode_key";
    public static final String  URL_PREFIX_KEY = "url_prefix_key";
    public static final String  VIDEO_AUTOPLAY_KEY = "video_autoplay_setting_key";
    public static final String  VISIBLE_ANIMATION_KEY = "visible_animation_key";
    public static final String  DEMO_AD_TEST_TYPE = "demo_ad_test_type";
    public static final String INTEGRATION_ERROR_MODE_KEY = "integration_error_mode_key";

    public static final boolean TEST_MODE_DEFAULT = false;
    public static final boolean VISIBLE_ANIMATION_DEFAULT = false;
    public static final String  URL_PREFIX_DEFAULT = "";
    public static final String  VIDEO_AUTOPLAY_DEFAULT = "AUTOPLAY";

    static final String  MULTIPROCESS_SUPPORT_KEY = "multiprocess_support_key";
    static final MultiprocessSupport MULTIPROCESS_SUPPORT_DEFAULT =
        MultiprocessSupport.MULTIPROCESS_SUPPORT_AUTO;

    enum MultiprocessSupport {
        MULTIPROCESS_SUPPORT_AUTO,
        MULTIPROCESS_SUPPORT_ON,
        MULTIPROCESS_SUPPORT_OFF,
        MULTIPROCESS_SUPPORT_FORCE,
    }

    private static ArrayList<ExtraSettings> sExtraSettings = new ArrayList<>();

    public enum AutoplaySetting {
        AUTOPLAY,
        AUTOPLAY_ON_MOBILE,
        NO_AUTOPLAY;
    }

    public static void initialize(Application application) {
        SharedPreferences preferences =
                application.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        setTestMode(preferences);
        setUrlPrefix(preferences);
        setVideoAutoplay(preferences);
        setVisibleAnimation(preferences);
        setIntegrationErrorMode(preferences);
        setMultiprocessSupportMode(preferences);

        for (ExtraSettings extraSetting: sExtraSettings) {
            extraSetting.initialize(preferences);
        }
    }

    public static void setTestMode(SharedPreferences preferences) {
        AdSettings.setTestMode(preferences.getBoolean(TEST_MODE_KEY, TEST_MODE_DEFAULT));
    }

    public static void setUrlPrefix(SharedPreferences preferences) {
        AdSettings.setUrlPrefix(preferences.getString(URL_PREFIX_KEY, URL_PREFIX_DEFAULT));
    }

    public static void setVisibleAnimation(SharedPreferences preferences) {
        AdSettings.setVisibleAnimation(
                preferences.getBoolean(VISIBLE_ANIMATION_KEY, VISIBLE_ANIMATION_DEFAULT));
    }

    public static void setVideoAutoplay(SharedPreferences preferences) {
        AutoplaySetting setting = AutoplaySetting.valueOf(
                preferences.getString(VIDEO_AUTOPLAY_KEY,VIDEO_AUTOPLAY_DEFAULT));
        AdSettings.setVideoAutoplay(setting != AutoplaySetting.NO_AUTOPLAY);
        AdSettings.setVideoAutoplayOnMobile(setting == AutoplaySetting.AUTOPLAY_ON_MOBILE);
    }

    public static void setDemoAdTestType(SharedPreferences preferences) {
        AdSettings.TestAdType testAdType = AdSettings.TestAdType.valueOf(
                preferences.getString(
                        DEMO_AD_TEST_TYPE,
                        AdSettings.TestAdType.DEFAULT.getAdTypeString()));
        AdSettings.setTestAdType(testAdType);
    }

    public static void setIntegrationErrorMode(SharedPreferences preferences) {
        AdSettings.IntegrationErrorMode integrationErrorMode =
            getIntegrationErrorMode(preferences);
        AdSettings.setIntegrationErrorMode(integrationErrorMode);
    }

    static void setMultiprocessSupportMode(SharedPreferences preferences) {

        MultiprocessSupport mode = getCurrentMultiprocessValue(preferences);
        switch (mode) {
            case MULTIPROCESS_SUPPORT_ON:
                AdSettings.setMultiprocessSupportMode(AdSettings.MultiprocessSupportMode.
                        MULTIPROCESS_SUPPORT_MODE_ON);
                break;
            case MULTIPROCESS_SUPPORT_AUTO:
                AdSettings.setMultiprocessSupportMode(MULTIPROCESS_SUPPORT_MODE_AUTO);
                break;
            case MULTIPROCESS_SUPPORT_OFF:
                AdSettings.setMultiprocessSupportMode(AdSettings.MultiprocessSupportMode.
                        MULTIPROCESS_SUPPORT_MODE_OFF);
                break;
        }
    }

    static AdSettings.IntegrationErrorMode getIntegrationErrorMode(SharedPreferences preferences) {
        String mode = preferences.getString(INTEGRATION_ERROR_MODE_KEY,
            INTEGRATION_ERROR_CRASH_DEBUG_MODE.name());
        AdSettings.IntegrationErrorMode integrationErrorMode;
        try {
            integrationErrorMode = AdSettings.IntegrationErrorMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            integrationErrorMode = INTEGRATION_ERROR_CRASH_DEBUG_MODE;
        }
        return integrationErrorMode;
    }

    static MultiprocessSupport getCurrentMultiprocessValue(SharedPreferences preferences) {
        String multiprocessMode = preferences.
            getString(MULTIPROCESS_SUPPORT_KEY, MULTIPROCESS_SUPPORT_DEFAULT.name());
        try {
            return MultiprocessSupport.valueOf(multiprocessMode);
        } catch (IllegalArgumentException e) {
            return MULTIPROCESS_SUPPORT_DEFAULT;
        }
    }

    public static ArrayList<ExtraSettings> getExtraSettings() {
        return sExtraSettings;
    }
}
