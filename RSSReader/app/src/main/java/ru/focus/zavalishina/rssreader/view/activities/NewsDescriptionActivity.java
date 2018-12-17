package ru.focus.zavalishina.rssreader.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.focus.zavalishina.rssreader.R;
import ru.focus.zavalishina.rssreader.model.structures.ItemInfo;

public final class NewsDescriptionActivity extends AppCompatActivity {
    private static final String ITEM_INFO_INTENT = "ru.focus.zavalishina.rssreader.ITEM_INFO_INTENT";
    private static ItemInfo itemInfo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_description_activity);
        itemInfo = getItemInfo(getIntent());
        final TextView description = findViewById(R.id.description);
        final TextView title = findViewById(R.id.title);

        title.setText(itemInfo.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(itemInfo.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            description.setText(Html.fromHtml(itemInfo.getDescription()));
        }

        setTitle(R.string.news);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void openInBrowser(View view) {
        String link = itemInfo.getLink();
        if (!"".equals(link)) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemInfo.getLink()));
            startActivity(intent);
        }
    }

    public static void startWithItemInfo(final @NonNull Context context, final @NonNull ItemInfo itemInfo) {
        final Intent intent = new Intent(context, NewsDescriptionActivity.class);
        intent.putExtra(ITEM_INFO_INTENT, itemInfo);
        context.startActivity(intent);
    }

    static ItemInfo getItemInfo(final @NonNull Intent intent) {
        return (ItemInfo) intent.getSerializableExtra(ITEM_INFO_INTENT);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
