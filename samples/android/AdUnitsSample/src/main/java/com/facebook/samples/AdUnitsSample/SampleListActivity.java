/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import com.facebook.samples.AdUnitsSample.adapters.SampleAdapter;
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity;

/** A simple {@link Fragment} subclass. */
public class SampleListActivity extends ListActivity {

  private static final String TAG = SampleListActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setTitle(R.string.select_sample);

    final SampleAdapter adapter = new SampleAdapter(SampleListActivity.this);

    // Bind to our new adapter.
    setListAdapter(adapter);

    getListView()
        .setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e(TAG, "List item clicked: " + position);
                SampleAdapter.Item item = (SampleAdapter.Item) adapter.getItem(position);

                String sampleName = item.getTitle();
                AdUnitsSampleType type = AdUnitsSampleType.getSampleTypeFromName(sampleName);
                if (type != null) {
                  Intent intent = new Intent(SampleListActivity.this, AdUnitsSampleActivity.class);
                  intent.putExtra(AdUnitsSampleActivity.SAMPLE_TYPE, type.getName());
                  startActivity(intent);
                }
              }
            });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.ad_units_sample_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int i = item.getItemId();
    if (i == R.id.debug_settings) {
      startActivity(new Intent(getApplicationContext(), DebugSettingsActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
