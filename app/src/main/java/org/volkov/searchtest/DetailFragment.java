package org.volkov.searchtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

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

        ByteArrayInputStream imageStream = new ByteArrayInputStream(data.getByteArray("IMAGE"));
        Bitmap image = BitmapFactory.decodeStream(imageStream);
        scoreView.setImageBitmap(image);
        songTextView.setText(data.getString("TEXT"));
    }
}