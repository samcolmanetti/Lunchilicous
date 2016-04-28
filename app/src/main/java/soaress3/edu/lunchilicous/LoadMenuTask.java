package soaress3.edu.lunchilicous;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class LoadMenuTask extends AsyncTask<Void, Void, List<FoodMenuItem>> {
    private WeakReference<FoodMenuFragment> mFoodMenuFragment;
    private WeakReference<SQLiteDatabase> mReadableDb;

    public LoadMenuTask (FoodMenuFragment foodMenuFragment, SQLiteDatabase readableDb){
        mFoodMenuFragment = new WeakReference<>(foodMenuFragment);
        mReadableDb = new WeakReference<>(readableDb);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<FoodMenuItem> doInBackground(Void... params) {
        Cursor c = null;
        List<FoodMenuItem> items = new LinkedList<>();
        try {
            SQLiteDatabase readableDb = mReadableDb.get();
            if (readableDb == null) return null;

            c = readableDb.query(
                    FoodOrderContract.Product.TABLE_NAME,
                    new String[]{FoodOrderContract.Product._ID, FoodOrderContract.Product.COLUMN_NAME_NAME},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            int nameIndex = c.getColumnIndex(FoodOrderContract.Product.COLUMN_NAME_NAME);
            int idIndex = c.getColumnIndex(FoodOrderContract.Product._ID);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                items.add(new FoodMenuItem(c.getString(nameIndex), c.getInt(idIndex)));
                c.moveToNext();
            }
        }
        catch (Exception ex){
            Log.e("LoadMenuTask", "Load menu items query failed ", ex);
        }
        finally {
            if (c != null) {
                c.close();
            }
        }

        return items;
    }

    private String[] getListOfMenuItems (List<FoodMenuItem> foodMenuItems){
        String[] names = new String[foodMenuItems.size()];

        for (int i = 0; i < foodMenuItems.size(); i++){
            names[i] = foodMenuItems.get(i).getmFoodName();
        }

        return names;
    }

    @Override
    protected void onPostExecute(List<FoodMenuItem> foodMenuItems) {
        super.onPostExecute(foodMenuItems);
        FoodMenuFragment fragment = mFoodMenuFragment.get();
        if (fragment == null) return;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getActivity(),
                android.R.layout.simple_list_item_1, getListOfMenuItems(foodMenuItems));

        fragment.setFoodMenuItems(foodMenuItems);
        fragment.setListAdapter(adapter);
    }


}
