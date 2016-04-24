package soaress3.edu.lunchilicous;

public class ConfirmationItem {
    private String mItemName;
    private Integer mItemLineNumber;
    private Integer mItemQuantity;

    public ConfirmationItem(String mItemName, Integer mItemLineNumber, Integer mItemQuantity) {
        this.mItemName = mItemName;
        this.mItemLineNumber = mItemLineNumber;
        this.mItemQuantity = mItemQuantity;
    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public Integer getmItemLineNumber() {
        return mItemLineNumber;
    }

    public void setmItemLineNumber(Integer mItemLineNumber) {
        this.mItemLineNumber = mItemLineNumber;
    }

    public Integer getmItemQuantity() {
        return mItemQuantity;
    }

    public void setmItemQuantity(Integer mItemQuantity) {
        this.mItemQuantity = mItemQuantity;
    }
}
