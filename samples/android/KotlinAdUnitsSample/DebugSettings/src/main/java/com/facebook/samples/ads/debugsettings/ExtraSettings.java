// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.samples.ads.debugsettings;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;

public interface ExtraSettings {
    void initialize(SharedPreferences preferences);
    void addExtraPreferences(PreferenceFragment preferenceFragment);
    void onCreatePreferencesView(PreferenceFragment preferenceFragment);
    boolean onSharedPreferenceChanged(PreferenceFragment preferenceFragment,
                                      SharedPreferences sharedPreferences,
                                      String key);
}
