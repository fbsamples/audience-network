/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import com.facebook.infer.annotation.Nullsafe;

/**
 * Manages scheduling of reminder alarms that trigger the AdUnitsSampleActivity with a specific ad
 * format. Used for testing keyguard-blocked impression scenarios.
 */
@Nullsafe(Nullsafe.Mode.LOCAL)
public class ReminderAlarmManager {

  private static final String TAG = ReminderAlarmManager.class.getSimpleName();
  private static final int REMINDER_REQUEST_CODE = 42;

  public static final String EXTRA_SHOW_OVER_LOCK_SCREEN = "SHOW_OVER_LOCK_SCREEN";

  private final Context context;
  private final AlarmManager alarmManager;
  private final AdUnitsSampleType sampleType;

  public ReminderAlarmManager(Context context, AdUnitsSampleType sampleType) {
    this.context = context;
    this.sampleType = sampleType;
    this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }

  /**
   * Schedule a reminder to show in the specified number of minutes.
   *
   * @param delayMinutes Number of minutes until the reminder should show
   */
  public void scheduleReminder(long delayMinutes) {
    scheduleReminderInMillis(delayMinutes * 60 * 1000);
  }

  /**
   * Schedule a reminder to show in the specified number of seconds. Useful for quick testing.
   *
   * @param delaySeconds Number of seconds until the reminder should show
   */
  public void scheduleReminderInSeconds(long delaySeconds) {
    scheduleReminderInMillis(delaySeconds * 1000);
  }

  /**
   * Schedule a reminder to show after the specified delay in milliseconds.
   *
   * @param delayMillis Delay in milliseconds
   */
  private void scheduleReminderInMillis(long delayMillis) {
    if (alarmManager == null) {
      Log.e(TAG, "AlarmManager not available");
      return;
    }

    PendingIntent pendingIntent = createReminderPendingIntent();
    long triggerTime = SystemClock.elapsedRealtime() + delayMillis;

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Android 12+ requires checking if exact alarms are allowed
        if (alarmManager.canScheduleExactAlarms()) {
          alarmManager.setExactAndAllowWhileIdle(
              AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        } else {
          // Fall back to inexact alarm
          alarmManager.setAndAllowWhileIdle(
              AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
        }
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // Android 6.0+ with doze mode support
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
      } else {
        // Older devices
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
      }

      Log.d(TAG, "Reminder scheduled for " + delayMillis + "ms from now");
    } catch (SecurityException e) {
      Log.e(TAG, "Cannot schedule exact alarm, falling back to inexact", e);
      alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
    }
  }

  /** Cancel any scheduled reminder. */
  public void cancelReminder() {
    if (alarmManager != null) {
      alarmManager.cancel(createReminderPendingIntent());
      Log.d(TAG, "Reminder cancelled");
    }
  }

  /**
   * Create a PendingIntent that launches the AdUnitsSampleActivity with the specified ad format.
   *
   * @return PendingIntent for the reminder
   */
  private PendingIntent createReminderPendingIntent() {
    Intent intent = new Intent(context, AdUnitsSampleActivity.class);
    intent.putExtra(AdUnitsSampleActivity.SAMPLE_TYPE, sampleType.getName());
    intent.putExtra(EXTRA_SHOW_OVER_LOCK_SCREEN, true);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

    int flags = PendingIntent.FLAG_UPDATE_CURRENT;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      flags |= PendingIntent.FLAG_IMMUTABLE;
    }

    return PendingIntent.getActivity(context, REMINDER_REQUEST_CODE, intent, flags);
  }
}
