package ru.focus.zavalishina.rssreader.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.focus.zavalishina.rssreader.R;
import ru.focus.zavalishina.rssreader.model.ChannelInfo;
import ru.focus.zavalishina.rssreader.view.ChannelListAdapter;
import ru.focus.zavalishina.rssreader.view.ChannelLoaderService;
import ru.focus.zavalishina.rssreader.view.DataAdapter;
import ru.focus.zavalishina.rssreader.view.NewsLoaderService;

public class NewsListActivity extends AppCompatActivity {
    private static final String NEWS_LIST_INTENT = "ru.focus.zavalishina.rssreader.NEWS_LIST_INTENT";
    private static final String UPDATE_CHANNEL_INFO_BROADCAST = "ru.focus.zavalishina.rssreader.CHANNEL_INFO_BROADCAST";
    private static final String UPDATE_CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.CHANNEL_INFO_INTENT";
    private RecyclerView recyclerView;
    private ChannelInfo channelInfo;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        channelInfo = getChannelInfo(getIntent());

        recyclerView = findViewById(R.id.news_list);
        recyclerView.setAdapter(new DataAdapter(this, channelInfo));
        setTitle(channelInfo.getTitle());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    return;
                }
                ChannelInfo newChannelInfo = getUpdatedChannelInfo(intent);

                if (newChannelInfo == null) {
                    return;
                }
                channelInfo = newChannelInfo;
                recyclerView.setAdapter(new DataAdapter(context, channelInfo));
            }
        };

        IntentFilter intentFilter = new IntentFilter(UPDATE_CHANNEL_INFO_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);


    }

    public void updateItems(View view) {
        Intent intent = NewsLoaderService.createIntent(this, channelInfo.getUrl());
        startService(intent);
    }

    public static Intent createUpdateChannelInfoIntent(ChannelInfo channelInfo) {
        Intent intent = new Intent(UPDATE_CHANNEL_INFO_BROADCAST);
        intent.putExtra(UPDATE_CHANNEL_INFO_INTENT, channelInfo);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    public static void startWithChannelInfo(Context context, ChannelInfo channelInfo) {
        Intent intent = new Intent(context, NewsListActivity.class);
        intent.putExtra(NEWS_LIST_INTENT, channelInfo);
        context.startActivity(intent);
    }

    static ChannelInfo getUpdatedChannelInfo(Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(UPDATE_CHANNEL_INFO_INTENT);
    }

    static ChannelInfo getChannelInfo(Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(NEWS_LIST_INTENT);
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
            case R.id.menu_favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
