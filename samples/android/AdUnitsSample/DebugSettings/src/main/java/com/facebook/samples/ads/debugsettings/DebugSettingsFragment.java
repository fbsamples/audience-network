// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.samples.ads.debugsettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.AdSettings;
import com.facebook.ads.BuildConfig;

import static com.facebook.ads.BuildConfig.VERSION_NAME;
import static com.facebook.samples.ads.debugsettings.DebugSettings.MULTIPROCESS_SUPPORT_KEY;

public class DebugSettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(DebugSettings.PREFERENCES_FILE);
        getPreferenceManager().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.debug_preferences);
        if (showiInternalSettings()) {
            addPreferencesFromResource(R.xml.internal_preferences);
        }

        for (ExtraSettings extraSettings: DebugSettings.getExtraSettings()) {
            extraSettings.addExtraPreferences(this);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        findPreference(DebugSettings.SDK_VERSION_KEY).setSummary(VERSION_NAME);

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (showiInternalSettings()) {
            findPreference(DebugSettings.URL_PREFIX_KEY).setSummary(
                    sharedPreferences
                            .getString(DebugSettings.URL_PREFIX_KEY, ""));
        }
        for (ExtraSettings extraSettings: DebugSettings.getExtraSettings()) {
            extraSettings.onCreatePreferencesView(this);
        }
        setupDemoAdTestType();
        setupIntegrationErrorMode(sharedPreferences);
        setupMultiprocessMode(sharedPreferences, this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupDemoAdTestType() {
        final ListPreference preference =
                (ListPreference) findPreference(DebugSettings.DEMO_AD_TEST_TYPE);
        preference.setEntries(getDemoAdTestTypeEntries());
        preference.setEntryValues(getDemoAdTestTypeEntryValues());
        preference.setDefaultValue(AdSettings.TestAdType.DEFAULT.toString());
    }

    private void setupIntegrationErrorMode(SharedPreferences sharedPreferences) {
        final ListPreference preference =
                (ListPreference) findPreference(DebugSettings.INTEGRATION_ERROR_MODE_KEY);

        AdSettings.IntegrationErrorMode[] values = AdSettings.IntegrationErrorMode.values();
        CharSequence[] modes = new CharSequence[values.length];
        int i = 0;
        for (AdSettings.IntegrationErrorMode mode: values) {
            modes[i] = mode.toString();
            i++;
        }
        preference.setEntries(modes);
        preference.setEntryValues(modes);
        preference.setValue(DebugSettings.getIntegrationErrorMode(sharedPreferences).name());
    }

    private static void setupMultiprocessMode(
                SharedPreferences sharedPreferences,
                PreferenceFragment preferenceFragment) {

        final ListPreference preference =
            (ListPreference) preferenceFragment.findPreference(MULTIPROCESS_SUPPORT_KEY);

        DebugSettings.MultiprocessSupport[] values = DebugSettings.MultiprocessSupport.values();
        int length = values.length;
        CharSequence[] modes = new CharSequence[length];
        int i = 0;
        for (DebugSettings.MultiprocessSupport mode: values) {
            modes[i] = mode.toString();
            i++;
        }
        preference.setEntries(modes);
        preference.setEntryValues(modes);
        preference.setValue(DebugSettings.
            getCurrentMultiprocessValue(sharedPreferences).name());
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
        for (ExtraSettings extraSettings: DebugSettings.getExtraSettings()) {
            if (extraSettings.onSharedPreferenceChanged(this, sharedPreferences, key)) {
                return;
            }
        }
        switch (key) {
            case DebugSettings.TEST_MODE_KEY:
                DebugSettings.setTestMode(sharedPreferences);
                break;
            case DebugSettings.URL_PREFIX_KEY:
                DebugSettings.setUrlPrefix(sharedPreferences);
                findPreference(key).setSummary(AdSettings.getUrlPrefix());
                break;
            case DebugSettings.VIDEO_AUTOPLAY_KEY:
                DebugSettings.setVideoAutoplay(sharedPreferences);
                break;
            case DebugSettings.VISIBLE_ANIMATION_KEY:
                DebugSettings.setVisibleAnimation(sharedPreferences);
                break;
            case DebugSettings.DEMO_AD_TEST_TYPE:
                DebugSettings.setDemoAdTestType(sharedPreferences);
                break;
            case DebugSettings.INTEGRATION_ERROR_MODE_KEY:
                DebugSettings.setIntegrationErrorMode(sharedPreferences);
                break;
            case DebugSettings.MULTIPROCESS_SUPPORT_KEY:
                DebugSettings.setMultiprocessSupportMode(sharedPreferences);
                break;
            default:
                break;
        }
    }

    private static boolean showiInternalSettings() {
        return BuildConfig.DEBUG || BuildConfig.BUILD_TYPE.equals("releaseDL");
    }

}
