package org.volkov.songbook;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "songs.db";
    private static final int SCHEMA = 1;
    static String TABLE = "songs";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NUM = "num";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_TEXT = "text";
    static final String COLUMN_IMAGE = "image";
    private Context myContext;

    DBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    void createDB() {
        File file = new File(DB_PATH);
        if (!file.exists()) {
            try (InputStream in = myContext.getAssets().open(DB_NAME)) {
                OutputStream out = new FileOutputStream(DB_PATH);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                out.flush();
            } catch (IOException e) {
                Log.d("DB_Helper", e.getMessage());
            }
        }
    }

    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
