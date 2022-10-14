package org.volkov.songbook;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends BaseActivity implements SearchFragment.OnFragmentSendDataListener {
    DetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Явно удаляем второй фрагмент, если устройство в портретной ориентации.
         * Иначе перестаёт работать переход к тексту из поиска
         * после смены ориентации ландшафт -> портрет */
        fragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Main Activity", "Portrait; Recreated!");
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                Log.d("Main Activity", "Fragment removed");
            }
        }
    }

    @Override
    public void onSendData(Bundle data) {
        fragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        if (fragment != null)
            fragment.setSelectedItem(data);
        else {
            Log.d("Main Activity", "Fragment is null");
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.SELECTED_ITEM, data);
            startActivity(intent);
        }
    }
}