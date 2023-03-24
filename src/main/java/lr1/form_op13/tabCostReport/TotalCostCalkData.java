package lr1.form_op13.tabCostReport;

public final class TotalCostCalkData extends CostReportData {
    private String numberDishes;
    public static final TotalCostCalkData TOTAL;
    public static final TotalCostCalkData SPENT_ACCORDING_CONTROL_CALC;
    public static final TotalCostCalkData SUM_NOT_SPENT;

    static {
        TOTAL = new TotalCostCalkData("Итого");
        SPENT_ACCORDING_CONTROL_CALC = new TotalCostCalkData("Израсходовано согласно контрольному расчёту");
        SUM_NOT_SPENT = new TotalCostCalkData("Сумма недорасходов");
    }

    private TotalCostCalkData(String nameField) {
        super(nameField);
        this.numberDishes = "";
    }

    public String getNumberDishes() {
        return numberDishes;
    }

    public void setNumberDishes(String numberDishes) {
        this.numberDishes = numberDishes;
    }
}
