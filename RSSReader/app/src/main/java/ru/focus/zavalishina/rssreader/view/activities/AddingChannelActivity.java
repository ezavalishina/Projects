package ru.focus.zavalishina.rssreader.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import ru.focus.zavalishina.rssreader.R;

public final class AddingChannelActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String urlString = intent.getStringExtra(Intent.EXTRA_TEXT);

        setContentView(R.layout.activity_adding_channel);

        EditText editUrl = findViewById(R.id.edit_url);
        editUrl.setText(urlString);
    }


}
