package ru.focus.zavalishina.rssreader.view.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.focus.zavalishina.rssreader.model.ChannelInfo;
import ru.focus.zavalishina.rssreader.view.DataAdapter;
import ru.focus.zavalishina.rssreader.view.NewsLoaderService;
import ru.focus.zavalishina.rssreader.R;

public final class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.CHANNEL_INFO_INTENT";
    private static final String CHANNEL_INFO_BROADCAST = "ru.focus.zavalishina.rssreader.CHANNEL_INFO_BROADCAST";
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 223313232;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                ChannelInfo channelInfo = getChannelInfo(intent);
                if (channelInfo == null) {
                    return;
                }
                RecyclerView recyclerView = findViewById(R.id.news_list);
                recyclerView.setAdapter(new DataAdapter(context, channelInfo));
            }
        };

        IntentFilter intentFilter = new IntentFilter(CHANNEL_INFO_BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    static ChannelInfo getChannelInfo(Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(CHANNEL_INFO_INTENT);
    }

    public static Intent createChannelInfoIntent(ChannelInfo channelInfo) {
        Intent intent = new Intent(CHANNEL_INFO_BROADCAST);
        intent.putExtra(CHANNEL_INFO_INTENT, channelInfo);
        return intent;
    }

    public final void addChannel(View view) {
        EditText editText = findViewById(R.id.edit_message);
        String url = editText.getText().toString();
        Intent intent = NewsLoaderService.createIntent(this, url);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (!(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET))) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);
            }
        } else {
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_INTERNET: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        addChannel(null);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.permission_message, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
    }

    public final boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_settings :
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.menu_channels:
                startActivity(new Intent(this, ChannelsActivity.class));
                return true;
            case R.id.menu_favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

