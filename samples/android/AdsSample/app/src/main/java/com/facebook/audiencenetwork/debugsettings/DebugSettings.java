// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.audiencenetwork.debugsettings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.ads.AdSettings;
import com.facebook.ads.internal.settings.AdInternalSettings;

public class DebugSettings {
    public static final String  PREFERENCES_FILE = "debug_preferences.xml";

    public static final String  SDK_VERSION_KEY = "sdk_version_key";
    public static final String  TEST_MODE_KEY = "test_mode_key";


    public static final String  DEMO_AD_TEST_TYPE = "demo_ad_test_type";
    public static final boolean TEST_MODE_DEFAULT = false;


    public static void initialize(Application application) {
        SharedPreferences preferences =
                application.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        setTestMode(preferences);
    }

    public static void setTestMode(SharedPreferences preferences) {
        AdInternalSettings.setTestMode(preferences.getBoolean(TEST_MODE_KEY, TEST_MODE_DEFAULT));
    }

    public static void setDemoAdTestType(SharedPreferences preferences) {
        AdSettings.TestAdType testAdType = AdSettings.TestAdType.valueOf(
                preferences.getString(
                        DEMO_AD_TEST_TYPE,
                        AdSettings.TestAdType.DEFAULT.getAdTypeString()));
        AdSettings.setTestAdType(testAdType);
    }
}