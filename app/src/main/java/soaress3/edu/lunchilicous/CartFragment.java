package soaress3.edu.lunchilicous;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CartFragment extends ListFragment {
    public interface OnReturnToFoodMenuListener {
        void returnToFoodMenu();
    }

    public static final String ARG_POSITION ="edu.soaress3.lunchilicious.ARG_POSITION";
    public static final String ARG_FOOD_NAMES = "edu.soaress3.lunchilicious.ARG_FOOD_NAME";
    public static final String ARG_FOOD_DESCRIPTION = "edu.soaress3.lunchilicious.ARG_FOOD_DESCRIPTION";
    public static final String ARG_FOOD_PRICES = "edu.soaress3.lunchilicious.ARG_FOOD_PRICES";
    public static final String ARG_FOOD_CALORIES = "edu.soaress3.lunchilicious.ARG_FOOD_CALORIES";
    public static final String ARG_FOOD_QUANTITIES = "edu.soaress3.lunchilicious.ARG_FOOD_QUANTITIES";

    private String[] mFoodNames;
    private String[] mFoodDescriptions;
    private int[] mFoodPrices;
    private int[] mFoodCalories;
    private int[] mFoodQuantities;
    private ListView mCartsListView;
    private OnReturnToFoodMenuListener mHostListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        if (bundle != null) {
            mFoodNames = bundle.getStringArray(ARG_FOOD_NAMES);
            mFoodDescriptions = bundle.getStringArray(ARG_FOOD_DESCRIPTION);
            mFoodPrices = bundle.getIntArray(ARG_FOOD_PRICES);
            mFoodCalories = bundle.getIntArray(ARG_FOOD_CALORIES);
            mFoodQuantities = bundle.getIntArray(ARG_FOOD_QUANTITIES);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mHostListener = (OnReturnToFoodMenuListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private FoodMenuItem[] getFoodMenuItems (){
        ArrayList<FoodMenuItem> items = new ArrayList<FoodMenuItem>();;

        for (int i = 0; i < mFoodQuantities.length; i++){
            if (mFoodQuantities[i] > 0){
                items.add(new FoodMenuItem(mFoodNames[i], mFoodDescriptions[i], mFoodCalories[i],
                        mFoodPrices[i], mFoodQuantities[i]));
            }
        }
        FoodMenuItem[] menuItems = new FoodMenuItem[items.size()];
        for (int i = 0; i < items.size(); i++){
            menuItems[i] = items.get(i);
        }

        return menuItems;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CartItemArrayAdapter adapter = new CartItemArrayAdapter(getActivity(), getFoodMenuItems());
        setListAdapter(adapter);
    }

}
