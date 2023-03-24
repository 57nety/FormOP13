package lr1.form_op13;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.converter.DateTimeStringConverter;
import lr1.form_op13.tabControlCalcSpent.ControlCalcData;
import lr1.form_op13.tabControlCalcSpent.SpiceSpentData;
import lr1.form_op13.tabControlCalcSpent.TotalSpentData;
import lr1.form_op13.tabCostReport.CostReportData;
import lr1.form_op13.tabCostReport.DishesSoldData;
import lr1.form_op13.tabCostReport.TotalCostCalkData;
import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Pane paneMain;

    // шапка
    @FXML
    private TextField numControlCalc;
    @FXML
    private DatePicker nowDate;
    @FXML
    private DatePicker withDate;
    @FXML
    private DatePicker beforeDate;
    @FXML
    private ComboBox<String> cmbBoxOrg;
    @FXML
    private ComboBox<String> cmbBoxStructSubdiv;
    @FXML
    private TextField OKDP;
    @FXML
    private TextField OKPO;

    // таблица "Контрольный расчёт расхода"
    private ObservableList<ControlCalcData> listSpices;
    @FXML
    private TextField nameSpice;
    @FXML
    private TableView<ControlCalcData> tblControlCalc;
    @FXML
    private TableColumn<ControlCalcData, String> colNumber;
    @FXML
    private TableColumn<ControlCalcData, String> colName;
    @FXML
    private TableColumn<ControlCalcData, String> colCode;
    @FXML
    private TableColumn<ControlCalcData, ?> colBalanceStart;
    @FXML
    private TableColumn<ControlCalcData, ?> colReceive;
    @FXML
    private TableColumn<ControlCalcData, ?> colBalanceEnd;
    @FXML
    private TableColumn<ControlCalcData, String> colSpent;

    // таблица "Справка о стоимости, включённой в калькуляцию блюд"
    @FXML
    private TextField costSaltRuble;
    @FXML
    private TextField costSaltKopeck;
    @FXML
    private TextField costSpiceRuble;
    @FXML
    private TextField costSpiceKopeck;

    @FXML
    private TableView<CostReportData> tblCostReport;
    @FXML
    private TableColumn<CostReportData, String> colNameField;
    @FXML
    private TableColumn<CostReportData, ?> colNumberDishes;
    @FXML
    private TableColumn<CostReportData, String> colSumCost;

    // pane "Расшифровка подписей"
    @FXML
    private TitledPane paneSign;
    @FXML
    private ComboBox<String> cmbBoxCalcAndRef;
    @FXML
    private ComboBox<String> cmbBoxApprove;
    @FXML
    private ComboBox<String> cmbBoxAccountant;

    @FXML
    private TextField calcAndRefSign;

    @FXML
    private TextField approveSign;

    @FXML
    private TextField accountantSign;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initHeader();
        initTableControlCalc();
        initTableCostReport();
        initPaneSign();
    }

    @FXML
    public void selectOrganization() {
        String number = switch (cmbBoxOrg.getValue()) {
            case "Организация 1" -> "1";
            case "Организация 2" -> "2";
            case "Организация 3" -> "3";
            default -> "";
        };
        OKDP.setText(number);
        updateOKPO();
    }

    void updateOKPO() {
        if (cmbBoxStructSubdiv.isDisable()) cmbBoxStructSubdiv.setDisable(false);
        if (cmbBoxStructSubdiv.getValue() != null) selectStructuralSubdivision();
    }

    @FXML
    public void selectStructuralSubdivision() {
        String number = switch (cmbBoxStructSubdiv.getValue()) {
            case "Подразделение 1" -> "1";
            case "Подразделение 2" -> "2";
            case "Подразделение 3" -> "3";
            default -> "";
        };
        OKPO.setText(OKDP.getText() + '.' + number);
    }

    private void initHeader() {
        ObservableList<String> observableList;

        observableList = FXCollections.observableArrayList("Организация 1", "Организация 2", "Организация 3");
        cmbBoxOrg.setItems(observableList);

        observableList = FXCollections.observableArrayList("Подразделение 1", "Подразделение 2", "Подразделение 3");
        cmbBoxStructSubdiv.setItems(observableList);

        nowDate.setValue(LocalDate.now());
        withDate.setValue(LocalDate.now());
        beforeDate.setValue(LocalDate.now());
    }

    private void initTableControlCalc() {
        listSpices = FXCollections.observableArrayList();

        // перенос слов в названии всех колонок
        ObservableList<TableColumn<ControlCalcData, ?>> columns = FXCollections.observableArrayList(tblControlCalc.getColumns());
        for (var column : columns) {
            Text text = new Text(column.getText());
            text.wrappingWidthProperty().bind(column.widthProperty());
            column.setGraphic(text);
        }

        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colBalanceStart.setCellValueFactory(new PropertyValueFactory<>("balanceStart"));
        colReceive.setCellValueFactory(new PropertyValueFactory<>("receive"));
        colBalanceEnd.setCellValueFactory(new PropertyValueFactory<>("balanceEnd"));
        colSpent.setCellValueFactory(new PropertyValueFactory<>("spent"));
    }

    @FXML
    public void addSpiceInTable() {
        if (nameSpice.getText().equals(""))
            return;

        if (!listSpices.isEmpty()) {
            for (var spice : listSpices)
                if (spice.getName().equalsIgnoreCase(nameSpice.getText()))
                    return;
            listSpices.remove(TotalSpentData.TOTAL);
        }

        listSpices.addAll(new SpiceSpentData(nameSpice.getText()), TotalSpentData.TOTAL);
        nameSpice.setText("");

        tblControlCalc.setItems(listSpices);
    }


    private void initTableCostReport() {
        ObservableList<CostReportData> listCostReport = FXCollections.observableArrayList(
                DishesSoldData.SOLD_DISHES_WITH_SPICE,
                DishesSoldData.SOlD_DISHES_WITH_SALT,
                TotalCostCalkData.TOTAL,
                TotalCostCalkData.SPENT_ACCORDING_CONTROL_CALC,
                TotalCostCalkData.SUM_NOT_SPENT
        );

        colNameField.setCellValueFactory(new PropertyValueFactory<>("nameField"));
        colNumberDishes.setCellValueFactory(new PropertyValueFactory<>("numberDishes"));
        colSumCost.setCellValueFactory(new PropertyValueFactory<>("sumCost"));

        tblCostReport.setItems(listCostReport);
    }

    private Double getFormattedNumber(Double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    private Double getAmountMoney(TextField rubles, TextField kopecks) {
        if (kopecks.getText().equals("")) {
            kopecks.setText("0");
            return Double.parseDouble(rubles.getText());
        }
        return Double.parseDouble(rubles.getText()) + Double.parseDouble(kopecks.getText()) / 100;
    }

    private void calculationTableCostReport() {
        Double costSpice = getAmountMoney(costSpiceRuble, costSpiceKopeck);
        Double numDishesWithSpice = Double.parseDouble(DishesSoldData.SOLD_DISHES_WITH_SPICE.getNumberDishes().getText());
        double allCostSpice = costSpice * numDishesWithSpice;

        Double costSalt = getAmountMoney(costSaltRuble, costSaltKopeck);
        Double numDishesWithSalt = Double.parseDouble(DishesSoldData.SOlD_DISHES_WITH_SALT.getNumberDishes().getText());
        double allCostSalt = costSalt * numDishesWithSalt;

        double totalNumDishes = numDishesWithSpice + numDishesWithSalt;
        double totalCostAllDishes = allCostSpice + allCostSalt;

        double spentAccordingControlCalc = Double.parseDouble(TotalSpentData.TOTAL.getSpent());
        double sumNotSpent = totalCostAllDishes - spentAccordingControlCalc;

        DishesSoldData.SOLD_DISHES_WITH_SPICE.setSumCost(Double.toString(getFormattedNumber(allCostSpice)));
        DishesSoldData.SOlD_DISHES_WITH_SALT.setSumCost(Double.toString(getFormattedNumber(allCostSalt)));
        TotalCostCalkData.TOTAL.setNumberDishes(Double.toString(getFormattedNumber(totalNumDishes)));
        TotalCostCalkData.TOTAL.setSumCost(Double.toString(getFormattedNumber(totalCostAllDishes)));
        TotalCostCalkData.SPENT_ACCORDING_CONTROL_CALC.setSumCost(Double.toString(getFormattedNumber(spentAccordingControlCalc)));
        TotalCostCalkData.SUM_NOT_SPENT.setSumCost(Double.toString(getFormattedNumber(sumNotSpent)));
    }

    private void calculationTableControlCalc() {
        double sumBalanceStart = 0;
        double sumReceive = 0;
        double sumBalanceEnd = 0;
        double sumSpent = 0;

        for (int i = 0; i < listSpices.size() - 1; i++) {
            SpiceSpentData spice = (SpiceSpentData) listSpices.get(i);

            double balanceStart = Double.parseDouble(spice.getBalanceStart().getText());
            sumBalanceStart += balanceStart;

            double receive = Double.parseDouble(spice.getReceive().getText());
            sumReceive += receive;

            double balanceEnd = Double.parseDouble(spice.getBalanceEnd().getText());
            sumBalanceEnd += balanceEnd;

            double spent = balanceStart + receive - balanceEnd;
            sumSpent += spent;

            spice.setSpent(Double.toString(getFormattedNumber(spent)));
            listSpices.set(i, spice);
        }

        TotalSpentData.TOTAL.setBalanceStart(Double.toString(getFormattedNumber(sumBalanceStart)));
        TotalSpentData.TOTAL.setReceive(Double.toString(getFormattedNumber(sumReceive)));
        TotalSpentData.TOTAL.setBalanceEnd(Double.toString(getFormattedNumber(sumBalanceEnd)));
        TotalSpentData.TOTAL.setSpent(Double.toString(getFormattedNumber(sumSpent)));
    }

    private boolean haveEmptyFieldInTabPane() {
        if (listSpices.isEmpty())
            return true;

        for (int i = 0; i < listSpices.size() - 1; i++) {
            SpiceSpentData data = (SpiceSpentData) listSpices.get(i);
            if (data.getBalanceStart().getText().equals(""))
                return true;
            if (data.getReceive().getText().equals(""))
                return true;
            if (data.getBalanceEnd().getText().equals(""))
                return true;
        }

        if (costSpiceRuble.getText().equals(""))
            return true;
        if (costSaltRuble.getText().equals(""))
            return true;
        if (DishesSoldData.SOLD_DISHES_WITH_SPICE.getNumberDishes().getText().equals(""))
            return true;
        if (DishesSoldData.SOlD_DISHES_WITH_SALT.getNumberDishes().getText().equals(""))
            return true;

        return false;
    }

    @FXML
    public void clickBtnCalcTables() {
        if (haveEmptyFieldInTabPane())
            return;

        calculationTableControlCalc();
        tblControlCalc.refresh();

        calculationTableCostReport();
        tblCostReport.refresh();
    }

    private boolean haveEmptyFieldInHeader() {
        if (numControlCalc.getText().equals(""))
            return true;
        if (cmbBoxOrg.getValue() == null)
            return true;
        if (cmbBoxStructSubdiv == null)
            return true;
        return false;
    }

    private boolean calculationNotBeenMade() {
        if (TotalCostCalkData.TOTAL.getSumCost().equals(""))
            return true;
        return false;
    }

    @FXML
    public void clickBtnSaveDoc() throws IOException {
        if (haveEmptyFieldInHeader() || haveEmptyFieldInTabPane() || haveEmptyFieldInSignPane())
            return;
        if (calculationNotBeenMade())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение документа");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.XLS)", "*.XLS");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileNewExcel = fileChooser.showSaveDialog(Window.getStage());
        if (fileNewExcel == null)
            return;

        File fileExcel = new File("src/main/excel_files/form_op13.XLS");
        FileInputStream inputStream = new FileInputStream(fileExcel);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFCell cell;

        cell = sheet.getRow(5).getCell(42);
        cell.setCellValue(OKPO.getText());

        cell = sheet.getRow(5).getCell(0);
        cell.setCellValue(cmbBoxOrg.getValue());

        cell = sheet.getRow(7).getCell(0);
        cell.setCellValue(cmbBoxStructSubdiv.getValue());

        cell = sheet.getRow(8).getCell(42);
        cell.setCellValue(OKDP.getText());

        cell = sheet.getRow(15).getCell(16);
        cell.setCellValue(numControlCalc.getText());

        cell = sheet.getRow(15).getCell(22);
        int day = nowDate.getValue().getDayOfMonth();
        int month = nowDate.getValue().getMonthValue();
        int year = nowDate.getValue().getYear();
        String date = day + "." + month + "." + year;
        cell.setCellValue(date);

        cell = sheet.getRow(15).getCell(28);
        day = withDate.getValue().getDayOfMonth();
        month = withDate.getValue().getMonthValue();
        year = withDate.getValue().getYear();
        date = day + "." + month + "." + year;
        cell.setCellValue(date);

        cell = sheet.getRow(15).getCell(32);
        day = beforeDate.getValue().getDayOfMonth();
        month = beforeDate.getValue().getMonthValue();
        year = beforeDate.getValue().getYear();
        date = day + "." + month + "." + year;
        cell.setCellValue(date);

        cell = sheet.getRow(12).getCell(43);
        cell.setCellValue(cmbBoxApprove.getValue());

        cell = sheet.getRow(14).getCell(44);
        cell.setCellValue(approveSign.getText());

        int len = 7;
        if (listSpices.size() < len)
            len = listSpices.size() - 1;

        for (int i = 0; i < len; i++) {
            SpiceSpentData spice = (SpiceSpentData) listSpices.get(i);

            cell = sheet.getRow(24 + i).getCell(0);
            cell.setCellValue(spice.getNumber());

            cell = sheet.getRow(24 + i).getCell(4);
            cell.setCellValue(spice.getName());

            cell = sheet.getRow(24 + i).getCell(18);
            cell.setCellValue(spice.getCode());

            cell = sheet.getRow(24 + i).getCell(22);
            cell.setCellValue(spice.getBalanceStart().getText());

            cell = sheet.getRow(24 + i).getCell(29);
            cell.setCellValue(spice.getReceive().getText());

            cell = sheet.getRow(24 + i).getCell(36);
            cell.setCellValue(spice.getBalanceEnd().getText());

            cell = sheet.getRow(24 + i).getCell(44);
            cell.setCellValue(spice.getSpent());
        }

        cell = sheet.getRow(31).getCell(22);
        cell.setCellValue(TotalSpentData.TOTAL.getBalanceStart());

        cell = sheet.getRow(31).getCell(29);
        cell.setCellValue(TotalSpentData.TOTAL.getReceive());

        cell = sheet.getRow(31).getCell(36);
        cell.setCellValue(TotalSpentData.TOTAL.getBalanceEnd());

        cell = sheet.getRow(31).getCell(44);
        cell.setCellValue(TotalSpentData.TOTAL.getSpent());

        cell = sheet.getRow(39).getCell(2);
        cell.setCellValue(costSpiceRuble.getText());

        cell = sheet.getRow(39).getCell(19);
        cell.setCellValue(costSpiceKopeck.getText());

        cell = sheet.getRow(39).getCell(30);
        cell.setCellValue(DishesSoldData.SOLD_DISHES_WITH_SPICE.getNumberDishes().getText());

        cell = sheet.getRow(39).getCell(37);
        cell.setCellValue(DishesSoldData.SOLD_DISHES_WITH_SPICE.getSumCost());

        cell = sheet.getRow(42).getCell(2);
        cell.setCellValue(costSaltRuble.getText());

        cell = sheet.getRow(42).getCell(19);
        cell.setCellValue(costSaltKopeck.getText());

        cell = sheet.getRow(41).getCell(30);
        cell.setCellValue(DishesSoldData.SOlD_DISHES_WITH_SALT.getNumberDishes().getText());

        cell = sheet.getRow(41).getCell(37);
        cell.setCellValue(DishesSoldData.SOlD_DISHES_WITH_SALT.getSumCost());

        cell = sheet.getRow(44).getCell(30);
        cell.setCellValue(TotalCostCalkData.TOTAL.getNumberDishes());

        cell = sheet.getRow(44).getCell(37);
        cell.setCellValue(TotalCostCalkData.TOTAL.getSumCost());

        cell = sheet.getRow(45).getCell(37);
        cell.setCellValue(TotalCostCalkData.SPENT_ACCORDING_CONTROL_CALC.getSumCost());

        cell = sheet.getRow(46).getCell(37);
        cell.setCellValue(TotalCostCalkData.SUM_NOT_SPENT.getSumCost());

        cell = sheet.getRow(48).getCell(15);
        cell.setCellValue(cmbBoxCalcAndRef.getValue());

        cell = sheet.getRow(48).getCell(33);
        cell.setCellValue(calcAndRefSign.getText());

        cell = sheet.getRow(50).getCell(15);
        cell.setCellValue(accountantSign.getText());

        workbook.write(new FileOutputStream(fileNewExcel));
        workbook.close();

    }

    @FXML
    public void clickBtnCloseDoc() {
        Window.close();
    }

    private void initPaneSign() {
        ObservableList<String> observableList;

        observableList = FXCollections.observableArrayList("Бухгалтер", "Директор", "Заведующий", "Повар");
        cmbBoxCalcAndRef.setItems(observableList);

        observableList = FXCollections.observableArrayList("Ген. директор", "Зам. директора");
        cmbBoxApprove.setItems(observableList);

        observableList = FXCollections.observableArrayList("Бухгалтер");
        cmbBoxAccountant.setItems(observableList);
    }

    @FXML
    public void clickBtnSign() {
        paneMain.setDisable(true);
        paneSign.setVisible(true);
    }

    private boolean haveEmptyFieldInSignPane() {
        if (calcAndRefSign.getText().equals(""))
            return true;
        if (approveSign.getText().equals(""))
            return true;
        if (accountantSign.getText().equals(""))
            return true;
        if (cmbBoxCalcAndRef.getValue() == null)
            return true;
        if (cmbBoxApprove.getValue() == null)
            return true;

        return false;
    }

    @FXML
    public void clickBtnSignSave() {
        if (haveEmptyFieldInSignPane())
            return;
        clickBtnSignCancel();
    }

    @FXML
    public void clickBtnSignCancel() {
        paneMain.setDisable(false);
        paneSign.setVisible(false);
    }
}
