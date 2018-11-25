package ru.focus.zavalishina.rssreader.view.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import ru.focus.zavalishina.rssreader.R;

public final class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
