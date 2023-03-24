package lr1.form_op13.tabControlCalcSpent;

import javafx.scene.control.TextField;

public abstract class ControlCalcData{
    protected String number;
    protected String name;
    protected String code;
    protected String spent;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name =name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code =code;
    }

    public String getSpent() {
        return spent;
    }

    public void setSpent(String spent) {
        this.spent =spent;
    }
}
