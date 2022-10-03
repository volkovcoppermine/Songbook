package org.volkov.searchtest;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class SearchFragment extends Fragment {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    Context context;

    ListView suggestionsList;
    EditText searchFilter;

    interface OnFragmentSendDataListener {
        void onSendData(Bundle data);
    }

    private OnFragmentSendDataListener fragmentSendDataListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            Log.d("Search fragment", e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        dbHelper = new DBHelper(context);
        dbHelper.createDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchFilter = view.findViewById(R.id.searchFilter);
        suggestionsList = view.findViewById(R.id.suggestionsList);

        suggestionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor selectedItem = (Cursor) adapterView.getItemAtPosition(i);
                Bundle data = new Bundle();

                //TODO: заменить на getColumnIndex?
                data.putString("TEXT", selectedItem.getString(2));
                data.putByteArray("IMAGE", selectedItem.getBlob(3));
                fragmentSendDataListener.onSendData(data);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            db = dbHelper.open();
            cursor = db.rawQuery(String.format("select %1$s, %2$s, %3$s, %4$s from %5$s order by %1$s",
                    DBHelper.COLUMN_ID, DBHelper.COLUMN_TITLE, DBHelper.COLUMN_TEXT,
                    DBHelper.COLUMN_IMAGE, DBHelper.TABLE), null);

            String[] headers = new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_TITLE};
            adapter = new SimpleCursorAdapter(context, android.R.layout.two_line_list_item, cursor,
                    headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

            if (!searchFilter.getText().toString().isEmpty())
                adapter.getFilter().filter(searchFilter.getText().toString());

            searchFilter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.getFilter().filter(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {
                    if (constraint == null || constraint.length() == 0) {
                        return db.rawQuery(String.format("select %s, %s, %s, %s from %s",
                                DBHelper.COLUMN_ID, DBHelper.COLUMN_TITLE, DBHelper.COLUMN_TEXT,
                                DBHelper.COLUMN_IMAGE, DBHelper.TABLE), null);
                    } else {
                        return db.rawQuery(String.format("select %1$s, %2$s, %3$s, %4$s from %5$s where %2$s like ? or %1$s like ? order by %1$s",
                                        DBHelper.COLUMN_ID, DBHelper.COLUMN_TITLE, DBHelper.COLUMN_TEXT,
                                        DBHelper.COLUMN_IMAGE, DBHelper.TABLE),
                                new String[]{"%" + constraint.toString() + "%", "%" + constraint.toString() + "%"});
                    }
                }
            });

            suggestionsList.setAdapter(adapter);
        } catch (SQLException e) {
            Log.d("Search fragment", e.getMessage());
        }
    }
}