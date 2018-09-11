// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.audiencenetwork.debugsettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.AdSettings;
import com.facebook.audiencenetwork.adssample.R;

import static com.facebook.ads.BuildConfig.DEBUG;
import static com.facebook.ads.BuildConfig.VERSION_NAME;

public class DebugSettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(DebugSettings.PREFERENCES_FILE);
        getPreferenceManager().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.debug_preferences);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        findPreference(DebugSettings.SDK_VERSION_KEY).setSummary(VERSION_NAME);

        setupDemoAdTestType();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupDemoAdTestType() {
        final ListPreference preference =
                (ListPreference) findPreference(DebugSettings.DEMO_AD_TEST_TYPE);
        preference.setEntries(getDemoAdTestTypeEntries());
        preference.setEntryValues(getDemoAdTestTypeEntryValues());
        preference.setDefaultValue(AdSettings.TestAdType.DEFAULT.toString());
    }

    private String[] getDemoAdTestTypeEntries() {
        final AdSettings.TestAdType[] values = AdSettings.TestAdType.values();
        final String[] entries = new String[AdSettings.TestAdType.values().length];
        for (int i = 0; i < values.length; i++) {
            entries[i] = values[i].getHumanReadable();
        }
        return entries;
    }

    private String[] getDemoAdTestTypeEntryValues() {
        final AdSettings.TestAdType[] values = AdSettings.TestAdType.values();
        final String[] entryValues = new String[AdSettings.TestAdType.values().length];
        for (int i = 0; i < values.length; i++) {
            entryValues[i] = values[i].toString();
        }
        return entryValues;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case DebugSettings.TEST_MODE_KEY:
                DebugSettings.setTestMode(sharedPreferences);
                break;
            case DebugSettings.DEMO_AD_TEST_TYPE:
                DebugSettings.setDemoAdTestType(sharedPreferences);
                break;
            default:
                break;
        }
    }

}
