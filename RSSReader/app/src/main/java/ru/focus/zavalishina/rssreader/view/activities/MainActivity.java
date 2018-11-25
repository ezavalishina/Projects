package ru.focus.zavalishina.rssreader.view.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ru.focus.zavalishina.rssreader.R;
import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.services.ChannelLoadService;
import ru.focus.zavalishina.rssreader.services.NewsLoadService;
import ru.focus.zavalishina.rssreader.services.NewsUpdateService;
import ru.focus.zavalishina.rssreader.view.adapters.ChannelListAdapter;


public final class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.CHANNEL_INFO_INTENT";
    private static final String DELETE_CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.DELETE_CHANNEL_INFO_INTENT";
    private static final String ARRAY_CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.ARRAY_CHANNEL_INFO_INTENT";
    private static final String CHANNEL_INFO_BROADCAST = "ru.focus.zavalishina.rssreader.CHANNEL_INFO_BROADCAST";
    private static final String DELETE_CHANNEL_INFO_BROADCAST = "ru.focus.zavalishina.rssreader.DELETE_CHANNEL_INFO_BROADCAST";
    private static final String ARRAY_CHANNEL_INFO_BROADCAST = "ru.focus.zavalishina.rssreader.ARRAY_CHANNEL_INFO_BROADCAST";
    private static final String DIALOG_OPENED = "ru.focus.zavalishina.rssreader.DIALOG_OPENED";
    private static final String DIALOG_URL = "ru.focus.zavalishina.rssreader.DIALOG_URL";
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 223313232;
    private BroadcastReceiver internetBroadcastReceiver;
    private BroadcastReceiver dbBroadcastReceiver;
    private BroadcastReceiver deleteChannelBroadcastReceiver;
    private SharedPreferences sharedPreferences;
    private final ArrayList<ChannelInfo> channelInfos = new ArrayList<>();
    private String savedUrl = null;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = findViewById(R.id.news_list);

        internetBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (intent == null) {
                    return;
                }
                final ChannelInfo channelInfo = getChannelInfo(intent);

                if (channelInfo == null) {
                    return;
                }
                final int channelIndex = findChannelInfoIndex(channelInfo, channelInfos);
                if (channelIndex != -1) {
                    channelInfos.remove(channelIndex);
                    channelInfos.add(channelIndex, channelInfo);
                } else {
                    channelInfos.add(channelInfo);
                }
                recyclerView.setAdapter(new ChannelListAdapter(context, channelInfos));
            }
        };

        dbBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (intent == null) {
                    return;
                }
                channelInfos.addAll(getArrayChannelInfo(intent));

                recyclerView.setAdapter(new ChannelListAdapter(context, channelInfos));
            }
        };

        deleteChannelBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (intent == null) {
                    return;
                }

                final int channelIndex = findChannelInfoIndex(getDeleteChannelInfo(intent), channelInfos);
                if (channelIndex != -1) {
                    channelInfos.remove(channelIndex);

                    recyclerView.setAdapter(new ChannelListAdapter(context, channelInfos));
                }
            }
        };

        final IntentFilter dbIntentFilter = new IntentFilter(ARRAY_CHANNEL_INFO_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(dbBroadcastReceiver, dbIntentFilter);
        final Intent intent = ChannelLoadService.createIntent(this);
        startService(intent);

        final IntentFilter intentFilter = new IntentFilter(CHANNEL_INFO_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(internetBroadcastReceiver, intentFilter);

        final IntentFilter deleteChannelIntentFilter = new IntentFilter(DELETE_CHANNEL_INFO_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(deleteChannelBroadcastReceiver, deleteChannelIntentFilter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(DIALOG_OPENED, false);
            editor.putString(DIALOG_URL, "");
            editor.apply();
        }

        final boolean dialogOpened = sharedPreferences.getBoolean(DIALOG_OPENED, false);

        if (dialogOpened) {
            addChannel(null);
        }
    }

//    private void setAutoUpdate(final int updatePeriod) {
//        final AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//        if(manager == null){
//            return;
//        }
//        final Intent updateIntent = NewsUpdateService.createAutoUpdateIntent(this, channelInfos);
//        final long updateTime = updatePeriod + System.currentTimeMillis();
//        final PendingIntent pendingIntent = PendingIntent.getService(this,
//                0, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        manager.set(AlarmManager.RTC_WAKEUP, updateTime, pendingIntent);
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        boolean autoUpdate = sharedPreferences.getBoolean(getString(R.string.auto_update_key), false);
//        if (autoUpdate) {
//            String updatePeriodString = sharedPreferences.getString(getString(R.string.auto_update_list_key), getString(R.string.val_h24));
//            if (updatePeriodString != null) {
//                int updatePeriod = Integer.valueOf(updatePeriodString);
//                setAutoUpdate(updatePeriod);
//            }
//        }
    }

    private int findChannelInfoIndex(final ChannelInfo channelInfo, final ArrayList<ChannelInfo> channelInfos) {
        for (int i = 0; i < channelInfos.size(); i++) {
            if (channelInfos.get(i).getUrl().equals(channelInfo.getUrl())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (savedUrl != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(DIALOG_URL, savedUrl);
            editor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(internetBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dbBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(deleteChannelBroadcastReceiver);

    }

    static ChannelInfo getChannelInfo(final Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(CHANNEL_INFO_INTENT);
    }

    public static Intent createChannelInfoIntent(final ChannelInfo channelInfo) {
        Intent intent = new Intent(CHANNEL_INFO_BROADCAST);
        intent.putExtra(CHANNEL_INFO_INTENT, channelInfo);
        return intent;
    }

    public static Intent createArrayChannelInfoIntent(final ArrayList<ChannelInfo> channelInfos) {
        final Intent intent = new Intent(ARRAY_CHANNEL_INFO_BROADCAST);
        intent.putExtra(ARRAY_CHANNEL_INFO_INTENT, channelInfos);
        return intent;
    }

    static ArrayList<ChannelInfo> getArrayChannelInfo(final Intent intent) {
        return (ArrayList<ChannelInfo>) intent.getSerializableExtra(ARRAY_CHANNEL_INFO_INTENT);
    }

    static ChannelInfo getDeleteChannelInfo(final Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(DELETE_CHANNEL_INFO_INTENT);
    }

    public static Intent createDeleteChannelInfoIntent(final ChannelInfo channelInfo) {
        Intent intent = new Intent(DELETE_CHANNEL_INFO_BROADCAST);
        intent.putExtra(DELETE_CHANNEL_INFO_INTENT, channelInfo);
        return intent;
    }

    public final void addChannel(final View view) {
        final ViewGroup viewGroup = null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(this.getLayoutInflater().inflate(R.layout.activity_adding_channel, viewGroup));

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(createShowListener(this, alertDialog));
        alertDialog.show();
    }

    private DialogInterface.OnShowListener createShowListener(final Context context, final AlertDialog alertDialog) {
        return new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DIALOG_OPENED, true);
                editor.apply();
                final String editionUrl = sharedPreferences.getString(DIALOG_URL, "");
                final EditText editText = alertDialog.findViewById(R.id.edit_text_dialog_url);
                final TextInputLayout textInputLayout = alertDialog.findViewById(R.id.text_input_layout_adding_channel);
                editText.setText(editionUrl);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        savedUrl = editText.getText().toString();
                        textInputLayout.setError(null);
                    }
                });
                final Button positive = alertDialog.findViewById(R.id.positive_button);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = editText.getText().toString();

                        if (URLUtil.isValidUrl(url)) {
                            final Intent intent = NewsLoadService.createIntent(context, url);
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                                    != PackageManager.PERMISSION_GRANTED) {

                                if (!(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                        Manifest.permission.INTERNET))) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.INTERNET},
                                            MY_PERMISSIONS_REQUEST_INTERNET);
                                }
                            } else {
                                startService(intent);
                            }
                            savedUrl = null;
                            editor.putBoolean(DIALOG_OPENED, false);
                            editor.putString(DIALOG_URL, "");
                            editor.apply();
                            dialog.cancel();

                        } else {
                            textInputLayout.setError(context.getString(R.string.url_error));
                        }
                    }
                });

                final Button negative = alertDialog.findViewById(R.id.negative_button);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedUrl = null;
                        editor.putBoolean(DIALOG_OPENED, false);
                        editor.putString(DIALOG_URL, "");
                        editor.apply();
                        dialog.cancel();
                    }
                });
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
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

    public final boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}