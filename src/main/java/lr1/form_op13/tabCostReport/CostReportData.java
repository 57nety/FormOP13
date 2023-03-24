package lr1.form_op13.tabCostReport;

import javafx.beans.property.SimpleStringProperty;

public abstract class CostReportData {
    protected String nameField;
    protected String sumCost;

    protected CostReportData(String nameField) {
        this.nameField = nameField;
        this.sumCost = "";
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public String getSumCost() {
        return sumCost;
    }

    public void setSumCost(String sumCost) {
        this.sumCost = sumCost;
    }
}
