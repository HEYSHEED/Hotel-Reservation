package model;
/**********************************************
 Workshop #
 Course:<ADP> - Semester 5th
 Last Name:<Ebrahim Shirazi>
 First Name:<Mahshid>
 ID:<168024222>
 Section:<NCC>
 This assignment represents my own work in accordance with Seneca Academic Policy.
 Signature
 Date:<2025-08-08>
 **********************************************/

import javafx.beans.property.*;

public class BillingItem {
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty unitPrice = new SimpleDoubleProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();

    public BillingItem(int quantity, String description, double unitPrice) {
        this.quantity.set(quantity);
        this.description.set(description);
        this.unitPrice.set(unitPrice);
        this.amount.set(quantity * unitPrice);
    }

    public int getQuantity() { return quantity.get(); }
    public String getDescription() { return description.get(); }
    public double getUnitPrice() { return unitPrice.get(); }
    public double getAmount() { return amount.get(); }

    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty unitPriceProperty() { return unitPrice; }
    public DoubleProperty amountProperty() { return amount; }
}
