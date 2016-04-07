package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FoodMenuFragment extends ListFragment {
    private Activity mActivity = null;
    private OnFoodMenuItemSelectedListener mListener;
    private DbProvider mDbProvider;

    private FoodMenuItem[] mMenuItems;

    public interface OnFoodMenuItemSelectedListener {
        void onFoodMenuItemSelected(int position);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
        this.mDbProvider = (DbProvider) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMenuItems = getAllMenuItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getListOfMenuItems());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Context context = mActivity.getApplicationContext();
        try {
            mListener = (OnFoodMenuItemSelectedListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString()
                    + " must implement OnFoodMenuItemSelectedListener");
        }
        mListener.onFoodMenuItemSelected(mMenuItems[position].getmFoodId());
    }

    private FoodMenuItem[] getAllMenuItems (){
        Cursor c = null;
        FoodMenuItem[] items = null;
        try {
            c = mDbProvider.getReadableDb().query(
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

            items = new FoodMenuItem[c.getCount()];
            c.moveToFirst();
            for (int i = 0; i < items.length; i++) {
                items[i] = new FoodMenuItem(c.getString(nameIndex), c.getInt(idIndex));
                c.moveToNext();
            }
        }
        catch (Exception ex){
            Log.e("FoodMenuFragment", "getAllMenuItems query failed ", ex);
        }
        finally {
            if (c != null){
                c.close();
            }
        }
        return items;
    }

    private String[] getListOfMenuItems (){
        String[] names = new String[this.mMenuItems.length];

        for (int i = 0; i < mMenuItems.length; i++){
            names[i] = mMenuItems[i].getmFoodName();
        }

        return names;
    }
}
