package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FoodMenuFragment extends ListFragment {
    private Activity mActivity = null;
    private OnFoodMenuItemSelectedListener mListener;

    public interface OnFoodMenuItemSelectedListener {
        void onFoodMenuItemSelected(int position);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] values = getResources().getStringArray(R.array.menu_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
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
        mListener.onFoodMenuItemSelected(position);
    }
}
