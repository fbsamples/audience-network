/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity
import com.facebook.samples.adunitssamplekotlin.fragments.SampleListFragment

class SampleListActivity : AppCompatActivity() {

  companion object {
    val TAG = SampleListActivity::class.java.simpleName
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_sample_list)

    supportFragmentManager
        .beginTransaction()
        .add(R.id.list_fragment_container, SampleListFragment())
        .commit()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.ad_units_sample_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.debug_settings) {
      // Start debug settings
      startActivity(Intent(this, DebugSettingsActivity::class.java))
      return true
    }

    return super.onOptionsItemSelected(item)
  }
}
