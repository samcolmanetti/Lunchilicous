package soaress3.edu.lunchilicous;

import java.io.Serializable;

public class FoodMenuItem implements Serializable{
    private String mFoodName;
    private String mFoodDescription;
    private double mFoodPrice;
    private int mFoodCalories;
    private int mFoodQuantity;
    private int mFoodId;

    public FoodMenuItem () {}

    public FoodMenuItem(String mFoodName, String mFoodDescription, int mFoodCalories,
                        double mFoodPrice, int mFoodQuantity) {

        this.mFoodName = mFoodName;
        this.mFoodDescription = mFoodDescription;
        this.mFoodCalories = mFoodCalories;
        this.mFoodPrice = mFoodPrice;
        this.mFoodQuantity = mFoodQuantity;
    }
    public FoodMenuItem(String mFoodName, String mFoodDescription, int mFoodCalories,
                        double mFoodPrice) {

        this.mFoodName = mFoodName;
        this.mFoodDescription = mFoodDescription;
        this.mFoodCalories = mFoodCalories;
        this.mFoodPrice = mFoodPrice;
    }

    public FoodMenuItem (String mFoodName, int mFoodId) {
        this.mFoodName = mFoodName;
        this.mFoodId = mFoodId;
    }

    public String getmFoodName() {
        return mFoodName;
    }

    public String getmFoodDescription() {
        return mFoodDescription;
    }

    public double getmFoodPrice() {
        return mFoodPrice;
    }

    public int getmFoodCalories() {
        return mFoodCalories;
    }

    public int getmFoodQuantity() {
        return mFoodQuantity;
    }

    public int getmFoodId() {
        return mFoodId;
    }

    public void setmFoodId(int mFoodId) {
        this.mFoodId = mFoodId;
    }
}
