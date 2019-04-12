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

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.samples.ads.debugsettings.DebugSettings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.facebook.ads.BuildConfig.DEBUG;

/**
 * Alternative version of Application class that shows how to enable multiprocess support
 * for Audience Network SDK without calling AudienceNetworkAds.isInAdsProcess(Context) method
 * inside Application.onCreate().
 */
public class AdUnitsSampleMultiprocessApplication extends Application
        implements AudienceNetworkAds.InitListener {

    private static final String TAG = AdUnitsSampleMultiprocessApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        String currentProcessName = getCurrentProcessName(this);
        if (TextUtils.isEmpty(currentProcessName) ||
            currentProcessName.equals(getDefaultApplicationProcessName(this))) {
            defaultProcessInitialization(this);
        } else if (currentProcessName.endsWith(AudienceNetworkAds.getAdsProcessName(this))) {
            // This is Audience Network Ads process. We should not do anything here.
            //
            // Note!!! You should be careful with calls to other SDKs and access to any files
            // including SharedPreferences as they might get corrupted if accessed from two processes
            // concurrently without synchronization at the same time.
        } // else code specific for other processes

    }

    private static void defaultProcessInitialization(AdUnitsSampleMultiprocessApplication app) {
        // Here you should place default initialization of your application
        // and init calls to other SDKs that require it
        if (DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        }

        DebugSettings.initialize(app);

        // Let Audience Network SDK know that you want to use Multiprocess Support
        AdSettings.setMultiprocessSupportMode(
            AdSettings.MultiprocessSupportMode.MULTIPROCESS_SUPPORT_MODE_ON);

        AudienceNetworkAds
            .buildInitSettings(app)
            .withInitListener(app)
            .initialize();
    }

    private static String getDefaultApplicationProcessName(Application app) {
        return app.getApplicationInfo().processName;
    }

    /**
     * This method uses reflection to fetch process name. For API 28 it uses public method
     * Application.getProcessName(). For older versions it uses private method
     * ActivityThread.getProcessName() that is the same method as API 28 uses internally.
     *
     * @param app current {@link Application}.
     * @return current process name.
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    @SuppressLint("CatchGeneralException")
    @Nullable
    private static String getCurrentProcessName(Application app) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                Method getProcessName = Application.class.
                    getMethod("getProcessName", (Class[]) null);
                return (String) getProcessName.invoke(null, (Object[]) null);
            } else {
                Field loadedApkField = app.getClass().getField("mLoadedApk");
                loadedApkField.setAccessible(true);
                Object loadedApk = loadedApkField.get(app);

                Field activityThreadField = loadedApk.getClass().
                    getDeclaredField("mActivityThread");
                activityThreadField.setAccessible(true);
                Object activityThread = activityThreadField.get(loadedApk);

                Method getProcessName = activityThread.getClass().
                    getDeclaredMethod("getProcessName", (Class[]) null);
                return (String) getProcessName.invoke(activityThread, (Object[]) null);
            }
        } catch (Throwable t) {
            Log.e(TAG, "Can't get process name.", t);
            return null;
        }
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        Log.d(AudienceNetworkAds.TAG, result.getMessage());
    }

}
