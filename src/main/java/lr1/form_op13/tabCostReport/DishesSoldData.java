package lr1.form_op13.tabCostReport;

import javafx.scene.control.TextField;

public final class DishesSoldData extends CostReportData {
    private TextField numberDishes;
    public static final DishesSoldData SOLD_DISHES_WITH_SPICE;
    public static final DishesSoldData SOlD_DISHES_WITH_SALT;

    static {
        SOLD_DISHES_WITH_SPICE = new DishesSoldData("Продано блюд, в которые включена стоимость специй на блюдо");
        SOlD_DISHES_WITH_SALT = new DishesSoldData("Продано блюд, в которые включена стоимость соли на блюдо");
    }

    private DishesSoldData(String nameField) {
        super(nameField);
        this.numberDishes = new TextField("");
    }

    public TextField getNumberDishes() {
        return numberDishes;
    }

    public void setNumberDishes(String numberDishes) {
        this.numberDishes = new TextField(numberDishes);
    }
}
