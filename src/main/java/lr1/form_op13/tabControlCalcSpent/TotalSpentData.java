package lr1.form_op13.tabControlCalcSpent;

import javafx.scene.control.TextField;

public final class TotalSpentData extends ControlCalcData {

    private String balanceStart;
    private String receive;
    private String balanceEnd;

    public static final TotalSpentData TOTAL;

    static {
        TOTAL = new TotalSpentData();
    }

    private TotalSpentData() {
        this.number = "";
        this.name = "";
        this.code = "Итого:";
        this.balanceStart = "";
        this.receive = "";
        this.balanceEnd = "";
        this.spent = "";
    }

    public String getBalanceStart() {
        return balanceStart;
    }

    public void setBalanceStart(String balanceStart) {
        this.balanceStart = balanceStart;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getBalanceEnd() {
        return balanceEnd;
    }

    public void setBalanceEnd(String balanceEnd) {
        this.balanceEnd = balanceEnd;
    }
}
