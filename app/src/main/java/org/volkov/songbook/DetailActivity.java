package org.volkov.songbook;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends BaseActivity {
    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    Bundle selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            selectedItem = extras.getBundle(SELECTED_ITEM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Detail activity", "onResume called");
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null)
            fragment.setSelectedItem(selectedItem);
    }
}