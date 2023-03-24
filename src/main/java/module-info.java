module lr1.form.op13.form_op13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires java.sql;


    opens lr1.form_op13 to javafx.fxml;
    exports lr1.form_op13;
    exports lr1.form_op13.tabControlCalcSpent;
    opens lr1.form_op13.tabControlCalcSpent to javafx.fxml;
    exports lr1.form_op13.tabCostReport;
    opens lr1.form_op13.tabCostReport to javafx.fxml;
}