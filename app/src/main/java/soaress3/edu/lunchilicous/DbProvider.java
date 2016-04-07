package soaress3.edu.lunchilicous;

import android.database.sqlite.SQLiteDatabase;

public interface DbProvider {
    SQLiteDatabase getReadableDb ();
    SQLiteDatabase getWritableDb();
}
