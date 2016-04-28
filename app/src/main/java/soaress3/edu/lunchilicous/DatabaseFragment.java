package soaress3.edu.lunchilicous;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class DatabaseFragment extends Fragment {
    private SQLiteDatabase mReadOnlyDb;
    private SQLiteDatabase mWritableOnlyDb;

    public DatabaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment
        setRetainInstance(true);
    }

    public void setDatabases (SQLiteDatabase readable, SQLiteDatabase writable){
        this.mReadOnlyDb = readable;
        this.mWritableOnlyDb = writable;
    }

    public SQLiteDatabase getmReadOnlyDb() {
        return mReadOnlyDb;
    }

    public void setmReadOnlyDb(SQLiteDatabase mReadOnlyDb) {
        this.mReadOnlyDb = mReadOnlyDb;
    }

    public SQLiteDatabase getmWritableOnlyDb() {
        return mWritableOnlyDb;
    }

    public void setmWritableOnlyDb(SQLiteDatabase mWritableOnlyDb) {
        this.mWritableOnlyDb = mWritableOnlyDb;
    }
}
