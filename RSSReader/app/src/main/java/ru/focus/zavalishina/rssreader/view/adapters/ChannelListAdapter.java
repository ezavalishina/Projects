package ru.focus.zavalishina.rssreader.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ru.focus.zavalishina.rssreader.R;
import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.view.activities.NewsListActivity;
import ru.focus.zavalishina.rssreader.services.ChannelDeleteService;

public final class ChannelListAdapter extends RecyclerView.Adapter {
    private final ArrayList<ChannelInfo> channelInfos;
    private final LayoutInflater inflater;

    public ChannelListAdapter(final Context context, final ArrayList<ChannelInfo> channelInfos) {
        this.channelInfos = channelInfos;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChannelListAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        final View view = inflater.inflate(R.layout.channel, viewGroup, false);
        return new ChannelListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        final ChannelInfo channel = channelInfos.get(i);
        final ChannelListAdapter.ViewHolder holder = (ChannelListAdapter.ViewHolder) viewHolder;
        holder.bind(channel);
    }

    @Override
    public int getItemCount() {
        return channelInfos.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView channelNameTextView;
        private final ImageButton deleteButton;

        private ViewHolder(final View view) {
            super(view);
            channelNameTextView = view.findViewById(R.id.channel_name_text_view);
            deleteButton = view.findViewById(R.id.delete_channel_button);
        }


        private void bind(final ChannelInfo channelInfo) {
            channelNameTextView.setText(channelInfo.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsListActivity.startWithChannelInfo(v.getContext(), channelInfo);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = ChannelDeleteService.createIntent(v.getContext(), channelInfo);
                    v.getContext().startService(intent);
                }
            });
        }
    }
}
