/**
 * Copyright (c) 2004-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook.samples.AdUnitsSample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.facebook.samples.AdUnitsSample.adapters.SampleAdapter;
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleListActivity extends ListActivity {

    private static final String TAG = SampleListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.select_sample);

        final SampleAdapter adapter = new SampleAdapter(SampleListActivity.this);

        // Bind to our new adapter.
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e(TAG, "List item clicked: " + position);
                SampleAdapter.Item item = (SampleAdapter.Item) adapter.getItem(position);

                String sampleName = item.getTitle();
                AdUnitsSampleType type = AdUnitsSampleType.getSampleTypeFromName(sampleName);
                if (type != null) {
                    Intent intent = new Intent(SampleListActivity.this,
                            AdUnitsSampleActivity.class);
                    intent.putExtra(AdUnitsSampleActivity.SAMPLE_TYPE,
                            type.getName());
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
