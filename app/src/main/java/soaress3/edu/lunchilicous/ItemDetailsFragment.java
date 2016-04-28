package soaress3.edu.lunchilicous;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.NumberFormat;

public class ItemDetailsFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = ItemDetailsFragment.class.getSimpleName();

    public interface OnItemAddedToCartListener {
        void itemAddedToCart (FoodMenuItem item, int quantity);
    }

    public  static final String ARG_ITEM_ID ="edu.soaress3.lunchilicious.ARG_ITEM_ID";
    private static final int QUANTITY_MIN = 1;
    private static final int QUANTITY_MAX = 100;

    private int mItemId = 0;
    private FoodMenuItem mFoodMenuItem;

    private OnItemAddedToCartListener mItemAddedToCartListener;
    private DbProvider mDbProvider;

    private TextView mFoodNameTextView;
    private TextView mFoodDescriptionTextView;
    private TextView mFoodUnitCostTextView;
    private TextView mFoodCaloriesTextView;
    private NumberPicker mFoodQuantityNumberPicker;
    private Button mAddToCartButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) mItemId = bundle.getInt(this.ARG_ITEM_ID, -1);

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mItemAddedToCartListener = (OnItemAddedToCartListener) activity;
        mDbProvider = (DbProvider) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details,  container, false);

        mFoodNameTextView = (TextView) view.findViewById(R.id.tv_food_name);
        mFoodDescriptionTextView = (TextView) view.findViewById(R.id.tv_food_description);
        mFoodUnitCostTextView = (TextView) view.findViewById(R.id.tv_food_unit_cost);
        mFoodCaloriesTextView = (TextView) view.findViewById(R.id.tv_food_calories);
        mFoodQuantityNumberPicker = (NumberPicker) view.findViewById(R.id.np_food_quantity);

        mAddToCartButton = (Button) view.findViewById(R.id.btn_add_to_cart);
        mAddToCartButton.setOnClickListener(this);

        mFoodQuantityNumberPicker = (NumberPicker) view.findViewById(R.id.np_food_quantity);
        mFoodQuantityNumberPicker.setMinValue(QUANTITY_MIN);
        mFoodQuantityNumberPicker.setMaxValue(QUANTITY_MAX);

        return view;
    }

    private void updateDisplay() {
        mFoodMenuItem = getFoodItem(mItemId);

        this.mFoodNameTextView.setText(mFoodMenuItem.getmFoodName());
        this.mFoodDescriptionTextView.setText(mFoodMenuItem.getmFoodDescription());
        this.mFoodUnitCostTextView.setText(NumberFormat.getCurrencyInstance().format(mFoodMenuItem.getmFoodPrice()));
        this.mFoodCaloriesTextView.setText("Calories: " + mFoodMenuItem.getmFoodCalories());
    }

    private int getItemQuantity (){
        return this.mFoodQuantityNumberPicker.getValue();
    }

    @Override
    public void onClick(View v) {
        mItemAddedToCartListener.itemAddedToCart(mFoodMenuItem, getItemQuantity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        updateDisplay();
        super.onActivityCreated(savedInstanceState);
    }

    private FoodMenuItem getFoodItem (int itemId){
        Cursor c = null;
        FoodMenuItem item = null;

        try {
            String whereClause = FoodOrderContract.Product._ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(itemId)};

            String[] columns = new String[] {
                    FoodOrderContract.Product.COLUMN_NAME_NAME,
                    FoodOrderContract.Product.COLUMN_NAME_DESCRIPTION,
                    FoodOrderContract.Product.COLUMN_NAME_CALORIES,
                    FoodOrderContract.Product.COLUMN_NAME_PRICE
            };
            c = mDbProvider.getReadableDb().query(
                    FoodOrderContract.Product.TABLE_NAME,
                    columns,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );

            int nameIndex = c.getColumnIndex(FoodOrderContract.Product.COLUMN_NAME_NAME);
            int descriptionIndex = c.getColumnIndex(FoodOrderContract.Product.COLUMN_NAME_DESCRIPTION);
            int calorieIndex = c.getColumnIndex(FoodOrderContract.Product.COLUMN_NAME_CALORIES);
            int priceIndex = c.getColumnIndex(FoodOrderContract.Product.COLUMN_NAME_PRICE);

            if (c != null && c.moveToFirst()) {
                item = new FoodMenuItem(c.getString(nameIndex), c.getString(descriptionIndex),
                        c.getInt(calorieIndex), c.getDouble(priceIndex));

            } else {
                Log.e (TAG, "Failed to get menu items");
            }
        }
        catch (Exception ex){
            Log.e(TAG, "getFoodItem failed: ", ex);
        }
        finally {
            if (c != null){
                c.close();
            }
        }
        return item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbProvider = null;
    }
}
