package lr1.form_op13.tabControlCalcSpent;

import javafx.scene.control.TextField;

public class SpiceSpentData extends ControlCalcData {
    private static int count = 0;

    private TextField balanceStart;
    private TextField receive;
    private TextField balanceEnd;

    public SpiceSpentData(String name) {
        count++;
        number = Integer.toString(count);
        this.name = name;
        this.code = Integer.toString(count * 100 + count * 10 + count);

        this.balanceStart = new TextField("");
        this.receive = new TextField("");
        this.balanceEnd = new TextField("");

        this.spent = "";
    }

    public TextField getBalanceStart() {
        return balanceStart;
    }

    public void setBalanceStart(String balanceStart) {
        this.balanceStart = new TextField(balanceStart);
    }

    public TextField getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = new TextField(receive);
    }

    public TextField getBalanceEnd() {
        return balanceEnd;
    }

    public void setBalanceEnd(String balanceEnd) {
        this.balanceEnd = new TextField(balanceEnd);
    }
}
