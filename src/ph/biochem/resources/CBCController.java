package ph.biochem.resources;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ph.biochem.modules.AlertDialog;
import ph.biochem.modules.DBHelper;
import ph.biochem.modules.DataHolder;
import ph.biochem.modules.StatementType;

public class CBCController extends AlertDialog{
    public String wbc, lymphocyte, monocyte, granulocytes, MCV, MCH, MCHC, RBC, Hemoglobin, Hermatocrit, Platelet, cbcRemarks, CBCTestType;

    @FXML
    public JFXTextField inputWBC, inputLymphocyte, inputMonocyte, inputGranulocytes, inputMCV, inputMCH, inputMCHC, inputRBC, inputHemoglobin,
    inputHermatocrit, inputPlatelet;

    @FXML
    public TextArea inputCBCRemarks;

    @FXML
    public JFXComboBox comboTestType;

    @FXML
    public Button btnSave, btnClose;

    @FXML
    public void initialize(){
        setValues();
        initComboBox();
    }

    private void setValues(){
        inputWBC.setText(DataHolder.wbc);
        inputLymphocyte.setText(DataHolder.lymphocyte);
        inputMonocyte.setText(DataHolder.monocyte);
        inputGranulocytes.setText(DataHolder.granulocytes);
        inputMCV.setText(DataHolder.MCV);
        inputMCH.setText(DataHolder.MCH);
        inputMCHC.setText(DataHolder.MCHC);
        inputRBC.setText(DataHolder.CBCRBC);
        inputHemoglobin.setText(DataHolder.Hemoglobin);
        inputHermatocrit.setText(DataHolder.Hermatocrit);
        inputPlatelet.setText(DataHolder.Platelet);
        inputCBCRemarks.setText(DataHolder.cbcRemarks);
        comboTestType.setValue(DataHolder.testType);
    }

    private void initComboBox(){
        comboTestType.getItems().addAll(new String[]{"Adult", "Pediatric"});
    }

    private void resetValues(){
        DataHolder.wbc=DataHolder.lymphocyte=DataHolder.monocyte=DataHolder.granulocytes=DataHolder.MCV=
                DataHolder.MCH=DataHolder.MCHC= DataHolder.CBCRBC =DataHolder.Hemoglobin= DataHolder.Hermatocrit=
                        DataHolder.Platelet=DataHolder.cbcRemarks=DataHolder.testType=DataHolder.CBCRBC ="";
    }

    public void onClickBtnSave(){
        wbc = inputWBC.getText();
        lymphocyte = inputLymphocyte.getText();
        monocyte = inputMonocyte.getText();
        granulocytes = inputGranulocytes.getText();
        MCV = inputMCV.getText();
        MCH = inputMCH.getText();
        MCHC = inputMCHC.getText();
        RBC = inputRBC.getText();
        Hemoglobin = inputHemoglobin.getText();
        Platelet = inputPlatelet.getText();
        Hermatocrit = inputHermatocrit.getText();
        cbcRemarks = inputCBCRemarks.getText();
        if(comboTestType.getValue() != null){
            CBCTestType = comboTestType.getValue().toString();
        }
        String updateCBC = "UPDATE SecondaryInfo SET CBCWBC = ?, CBCLymphocyte = ?, CBCMonocyte = ?, CBCGranulocyte = ?, " +
                "CBCMCV = ?, CBCMCH = ?, CBCRBC = ?, CBCHemoglobin = ?, CBCHermatocrit = ?, CBCPlatelet = ?, CBCRemarks = ?," +
                "CBCTestType = ? WHERE MRNID = ?";
        DBHelper.executeQuery(updateCBC, new String[]{wbc, lymphocyte, monocyte, granulocytes, MCV, MCH, RBC, Hemoglobin,
        Hermatocrit, Platelet, cbcRemarks, isNull(comboTestType.getValue()), DataHolder.selectedMRNID}, StatementType.UPDATE);
        DataHolder.config.createConfigTest("CBC.biochem", new String[]{
                isNull(comboTestType.getValue()), wbc, lymphocyte, monocyte, granulocytes, MCV, MCH, MCHC, RBC, Hemoglobin, Hermatocrit, Platelet, cbcRemarks
        });
        onClose();
    }

    public void onClose(){
        resetValues();
        Stage CBCForm = (Stage) btnSave.getScene().getWindow();
        CBCForm.close();
    }
}
