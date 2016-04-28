package soaress3.edu.lunchilicous;

public class OrderDetailsItem {

    private Integer purchaseOrderId;
    private String productName;
    private Integer lineNumber;
    private String description;
    private Integer quantity;
    private Double price;

    public OrderDetailsItem(Integer purchaseOrderId, String productName, Integer lineNumber,
                            String description, Integer quantity, Double price) {
        this.purchaseOrderId = purchaseOrderId;
        this.productName = productName;
        this.lineNumber = lineNumber;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
