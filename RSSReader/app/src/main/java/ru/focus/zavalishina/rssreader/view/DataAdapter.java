package ru.focus.zavalishina.rssreader.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.focus.zavalishina.rssreader.R;
import ru.focus.zavalishina.rssreader.model.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.ItemInfo;

public final class DataAdapter extends RecyclerView.Adapter {
    private ChannelInfo channelInfo;
    private LayoutInflater inflater;

    public DataAdapter(final Context context, final ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        final View view = inflater.inflate(R.layout.news_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        final ItemInfo item = channelInfo.getItems().get(i);
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return channelInfo.getItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTextView, dataTextView, titleTextView;
        private ViewHolder(final View view) {
            super(view);
            authorTextView = view.findViewById(R.id.author_text_view);
            dataTextView = view.findViewById(R.id.data_text_view);
            titleTextView = view.findViewById(R.id.title_text_view);
        }

         private void bind(final ItemInfo itemInfo) {
            authorTextView.setText(itemInfo.getAuthor());
            dataTextView.setText(itemInfo.getPubDate());
            titleTextView.setText(itemInfo.getTitle());
        }
    }
}
