package com.fluidminds.android.studiosity.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * GUI-less Launcher Activity to determine which Activity to initially display (AppIntro to SubjectList)
 */
public class LauncherActivity extends Activity {
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.fluidminds.android.studiosity", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();

            Intent intent = new Intent(LauncherActivity.this , IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }
        else {
            startActivity(new Intent(LauncherActivity.this , SubjectListActivity.class));
            finish();
        }
    }
}
