package soaress3.edu.lunchilicous;

public class FoodMenuItem {
    public String mFoodName;
    public String mFoodDescription;
    public int mFoodPrice;
    public int mFoodCalories;
    public int mFoodQuantity;

    public FoodMenuItem(String mFoodName, String mFoodDescription, int mFoodCalories, int mFoodPrice, int mFoodQuantity) {
        this.mFoodName = mFoodName;
        this.mFoodDescription = mFoodDescription;
        this.mFoodCalories = mFoodCalories;
        this.mFoodPrice = mFoodPrice;
        this.mFoodQuantity = mFoodQuantity;
    }

    public String getmFoodName() {
        return mFoodName;
    }

    public String getmFoodDescription() {
        return mFoodDescription;
    }

    public int getmFoodPrice() {
        return mFoodPrice;
    }

    public int getmFoodCalories() {
        return mFoodCalories;
    }

    public int getmFoodQuantity() {
        return mFoodQuantity;
    }

}
