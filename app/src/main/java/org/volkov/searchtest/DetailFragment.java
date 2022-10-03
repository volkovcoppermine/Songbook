package org.volkov.searchtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void setSelectedItem(Bundle data) {
        ImageView scoreView = getView().findViewById(R.id.scoreView);
        TextView songTextView = getView().findViewById(R.id.songTextView);

        String filename = "images/" + data.getString("IMAGE");
        try(InputStream inputStream = getActivity().getApplicationContext().getAssets()
                .open(filename)){
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            scoreView.setImageDrawable(drawable);
        } catch (IOException e) {
            Log.d("Detail fragment", e.getMessage());
        }
        songTextView.setText(data.getString("TEXT"));
    }
}