package ru.focus.zavalishina.rssreader.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ru.focus.zavalishina.rssreader.R;
import ru.focus.zavalishina.rssreader.model.ItemInfo;

public class NewsDescriptionActivity extends AppCompatActivity {
    private static final String ITEM_INFO_INTENT = "ru.focus.zavalishina.rssreader.ITEM_INFO_INTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_description_activity);
        ItemInfo itemInfo = getItemInfo(getIntent());
        TextView description = findViewById(R.id.description);
        TextView title = findViewById(R.id.title);

        title.setText(itemInfo.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(itemInfo.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            description.setText(Html.fromHtml(itemInfo.getDescription()));
        }

        setTitle(R.string.news);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static void startWithItemInfo(Context context, ItemInfo itemInfo) {
        Intent intent = new Intent(context, NewsDescriptionActivity.class);
        intent.putExtra(ITEM_INFO_INTENT, itemInfo);
        context.startActivity(intent);
    }

    static ItemInfo getItemInfo(Intent intent) {
        return (ItemInfo) intent.getSerializableExtra(ITEM_INFO_INTENT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
