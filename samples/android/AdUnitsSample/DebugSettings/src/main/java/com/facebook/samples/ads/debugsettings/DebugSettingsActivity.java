// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.samples.ads.debugsettings;

import android.app.Activity;
import android.os.Bundle;

public class DebugSettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new DebugSettingsFragment())
                .commit();
    }
}
