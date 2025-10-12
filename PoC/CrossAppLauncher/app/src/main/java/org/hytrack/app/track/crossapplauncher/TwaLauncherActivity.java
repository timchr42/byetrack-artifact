package org.hytrack.app.track.crossapplauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.browser.trusted.TrustedWebActivityIntent;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

public class TwaLauncherActivity extends Activity {

    private static final String LOGTAG = "TwaLauncher";
    private static final String FENIX_PACKAGE = "org.mozilla.fenix.debug";
    private static final Uri LAUNCH_URI = Uri.parse("http://10.0.2.2/?hide&demo&s=trackmeplslaunch&app=TrackingEventA");
    private static final List<String> TRUSTED_ORIGINS = Arrays.asList(
            "https://schnellnochraviolimachen.de/"
    );

    private CustomTabsClient customTabsClient;
    private CustomTabsSession customTabsSession;
    private CustomTabsServiceConnection connection;

    @Override
    protected void onStart() {
        super.onStart();

        if (connection == null) {
            connection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(@NonNull ComponentName name,
                                                         @NonNull CustomTabsClient client) {
                    Log.d(LOGTAG, "Custom Tabs service connected");

                    customTabsClient = client;
                    customTabsClient.warmup(0L);

                    customTabsSession = customTabsClient.newSession(null);
                    if (customTabsSession == null) {
                        Log.e(LOGTAG, "Failed to create CustomTabsSession");
                        finish();
                        return;
                    }

                    // Build TWA intent
                    TrustedWebActivityIntentBuilder builder =
                            new TrustedWebActivityIntentBuilder(LAUNCH_URI)
                                    .setAdditionalTrustedOrigins(TRUSTED_ORIGINS);

                    CustomTabColorSchemeParams colors = new CustomTabColorSchemeParams.Builder()
                            .setToolbarColor(ContextCompat.getColor(TwaLauncherActivity.this, R.color.purple_500))
                            .build();

                    builder.setDefaultColorSchemeParams(colors);

                    TrustedWebActivityIntent twaIntent = builder.build(customTabsSession);

                    // Launch the TWA
                    Log.d(LOGTAG, "Launching TWA: " + LAUNCH_URI);
                    twaIntent.launchTrustedWebActivity(TwaLauncherActivity.this);

                    // Close this launcher activity
                    finish();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.w(LOGTAG, "Custom Tabs service disconnected");
                    customTabsClient = null;
                    customTabsSession = null;
                }
            };
        }

        boolean ok = CustomTabsClient.bindCustomTabsService(
                this, FENIX_PACKAGE, connection
        );

        Log.d(LOGTAG, "bindCustomTabsService returned: " + ok);

        if (!ok) {
            Log.e(LOGTAG, "Could not bind to Fenix Custom Tabs service");
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (connection != null) {
            Log.d(LOGTAG, "Unbinding from Custom Tabs service");
            unbindService(connection);
            connection = null;
        }
    }
}
