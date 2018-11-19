package ru.focus.zavalishina.rssreader.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
        TextView textView = findViewById(R.id.description);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(itemInfo.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(itemInfo.getDescription()));
        }
    }

    public static void startWithItemInfo(Context context, ItemInfo itemInfo) {
        Intent intent = new Intent(context, NewsDescriptionActivity.class);
        intent.putExtra(ITEM_INFO_INTENT, itemInfo);
        context.startActivity(intent);
    }

    static ItemInfo getItemInfo(Intent intent) {
        return (ItemInfo) intent.getSerializableExtra(ITEM_INFO_INTENT);
    }
}
