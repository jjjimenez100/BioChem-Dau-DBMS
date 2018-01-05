package ph.biochem.resources;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ph.biochem.modules.DBHelper;
import ph.biochem.modules.DataHolder;
import ph.biochem.modules.StatementType;

public class BloodChemistryController {
    public String fasting, bloodUrea, creatinine, bloodUric, totalCholesterol, triglycerides, HDL, LDL, SGPT, SGOT, bloodChemistryRemarks;

    @FXML
    private JFXTextField inputFasting, inputBloodUrea, inputCreatinine, inputBloodUric, inputTotalCholesterol, inputTriglycerides,
    inputHDL, inputLDL, inputSGPT, inputSGOT;

    @FXML
    private TextArea inputBloodChemistryRemarks;

    @FXML
    private Button btnSave, btnClose;

    @FXML
    public void initialize(){
        setValues();
    }

    private void setValues(){
        inputFasting.setText(DataHolder.fasting);
        inputBloodUrea.setText(DataHolder.bloodUrea);
        inputCreatinine.setText(DataHolder.creatinine);
        inputBloodUric.setText(DataHolder.bloodUric);
        inputTotalCholesterol.setText(DataHolder.totalCholesterol);
        inputTriglycerides.setText(DataHolder.triglycerides);
        inputHDL.setText(DataHolder.HDL);
        inputLDL.setText(DataHolder.LDL);
        inputSGPT.setText(DataHolder.SGPT);
        inputSGOT.setText(DataHolder.SGOT);
        inputBloodChemistryRemarks.setText(DataHolder.bloodChemistryRemarks);
    }

    public void resetValues(){
        DataHolder.fasting = DataHolder.bloodUrea = DataHolder.creatinine = DataHolder.bloodUric = DataHolder.totalCholesterol
                = DataHolder.triglycerides = DataHolder.HDL = DataHolder.LDL = DataHolder.SGPT = DataHolder.SGOT =
                DataHolder.bloodChemistryRemarks = "";
    }

    public void onClickBtnSave(){
        fasting = inputFasting.getText();
        bloodUrea = inputBloodUrea.getText();
        creatinine = inputCreatinine.getText();
        bloodUric = inputBloodUric.getText();
        totalCholesterol = inputTotalCholesterol.getText();
        triglycerides = inputTriglycerides.getText();
        HDL = inputHDL.getText();
        LDL = inputLDL.getText();
        SGPT = inputSGPT.getText();
        SGOT = inputSGOT.getText();
        bloodChemistryRemarks = inputBloodChemistryRemarks.getText();
        String updateBC = "UPDATE SecondaryInfo SET BCFastingBloodSugar = ?, BCBlood = ?, BCCreatinine = ?, BCUricAcid = ?, " +
                "BCCholesterol = ?, BCTriglycerides = ?, BCHDL = ?, BCLDL = ?, BCSGPT = ?, BCSGOT = ?, BCRemarks = ? WHERE" +
                " MRNID = ?";
        DBHelper.executeQuery(updateBC, new String[]{fasting, bloodUrea, creatinine, bloodUric, totalCholesterol, triglycerides,
        HDL, LDL, SGPT, SGOT, bloodChemistryRemarks, Integer.toString(DataHolder.selectedMRNID)}, StatementType.UPDATE);
        onClose();
    }

    public void onClose(){
        resetValues();
        Stage bloodChemistryForm = (Stage) btnClose.getScene().getWindow();
        bloodChemistryForm.close();
    }
}
