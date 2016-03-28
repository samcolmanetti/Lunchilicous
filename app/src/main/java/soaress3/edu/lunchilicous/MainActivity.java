package soaress3.edu.lunchilicous;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        FoodMenuFragment.OnFoodMenuItemSelectedListener,
        ItemDetailsFragment.OnItemAddedToCartListener {

    private CartFragment mCartFragment;

    private int[] mFoodItemQuantities;
    private String[] mFoodNames;
    private String[] mFoodDescriptions;
    private int[] mFoodCalories;
    private int[] mFoodPrices;
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadDataFromResources();

        if (findViewById(R.id.food_menu_fragment) == null) {

            if (savedInstanceState != null) {
                return;
            }
            FoodMenuFragment foodMenu = new FoodMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, foodMenu);
            ft.commit();
        } else {
            // large device is being used
            ItemDetailsFragment foodItem = new ItemDetailsFragment();

            Bundle args = new Bundle();
            args.putInt(ItemDetailsFragment.ARG_POSITION, mPosition);
            foodItem.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, foodItem);
            transaction.addToBackStack("DETAILS");
            transaction.commit();
        }

        int numberOfItems = getResources().getStringArray(R.array.menu_items).length;
        this.mFoodItemQuantities = new int[numberOfItems];

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // shopping cart selected since it's the only item in the menu

        if (this.mCartFragment == null){
            this.mCartFragment = loadCartFragmentData();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, mCartFragment);
        ft.addToBackStack("CART");
        ft.commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFoodMenuItemSelected(int position) {
        ItemDetailsFragment foodItem = new ItemDetailsFragment();
        this.mPosition = position;

        Bundle args = new Bundle();
        args.putInt(ItemDetailsFragment.ARG_POSITION, position);
        foodItem.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, foodItem);
        transaction.addToBackStack("DETAILS");
        transaction.commit();
    }

    @Override
    public void itemAddedToCart(int itemId, int quantity) {
        if (mFoodItemQuantities != null) {
            mFoodItemQuantities[itemId] += quantity;
        } else {
            Toast.makeText(MainActivity.this, "NULL FoodQuantities", Toast.LENGTH_SHORT).show();
        }
        this.mPosition = 0;

        FragmentManager fm = getSupportFragmentManager();

        if (findViewById(R.id.food_menu_fragment) == null){
            fm.popBackStackImmediate();
        } else {
            fm.popBackStackImmediate("DETAILS",fm.POP_BACK_STACK_INCLUSIVE);

            // start new instance of ItemDetailsFragment
            this.onFoodMenuItemSelected(0);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle restoredInstanceState) {
        mFoodItemQuantities = restoredInstanceState.getIntArray(CartFragment.ARG_FOOD_QUANTITIES);
        super.onRestoreInstanceState(restoredInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putIntArray(CartFragment.ARG_FOOD_QUANTITIES, mFoodItemQuantities);
        super.onSaveInstanceState(savedInstanceState);
    }

    private CartFragment loadCartFragmentData () {
        CartFragment cartFrag = new CartFragment();
        cartFrag.setmFoodQuantities(this.mFoodItemQuantities);
        cartFrag.setmFoodPrices(this.mFoodPrices);
        cartFrag.setmFoodNames(this.mFoodNames);
        cartFrag.setmFoodDescriptions(this.mFoodDescriptions);
        cartFrag.setmFoodCalories(this.mFoodCalories);
        cartFrag.setmOrderTotal(this.orderTotal());

        return  cartFrag;
    }

    private void loadDataFromResources (){
        this.mFoodNames = getResources().getStringArray(R.array.menu_items);
        this.mFoodDescriptions = getResources().getStringArray(R.array.item_descriptions);
        this.mFoodCalories = getResources().getIntArray(R.array.item_calories);
        this.mFoodPrices = getResources().getIntArray(R.array.item_prices);
    }

    private int orderTotal (){
        int total = 0;

        for (int i = 0; i < mFoodItemQuantities.length; i++){
            if (mFoodItemQuantities[i] > 0){
                total += mFoodItemQuantities[i] * mFoodPrices[i];

            }
        }

        return total;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // save updated data in retainedFragment
        mCartFragment.setmFoodQuantities(this.mFoodItemQuantities);
        mCartFragment.setmOrderTotal(this.orderTotal());
    }



}