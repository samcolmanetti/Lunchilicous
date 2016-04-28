package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class FoodMenuFragment extends ListFragment {

    private Activity mActivity = null;
    private OnFoodMenuItemSelectedListener mListener;
    private DbProvider mDbProvider;
    private LoadMenuTask mLoadMenuTask;

    private List<FoodMenuItem> mMenuItems;

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
        mLoadMenuTask = new LoadMenuTask(this, mDbProvider.getReadableDb());
        mLoadMenuTask.execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            mListener = (OnFoodMenuItemSelectedListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString()
                    + " must implement OnFoodMenuItemSelectedListener");
        }
        mListener.onFoodMenuItemSelected(mMenuItems.get(position).getmFoodId());
    }

    public void setFoodMenuItems(List<FoodMenuItem> menuItems) {
        this.mMenuItems = menuItems;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbProvider = null;
        if (mLoadMenuTask != null) mLoadMenuTask.cancel(true);
    }
}
