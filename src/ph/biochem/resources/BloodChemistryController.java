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

    public String fastingSI, bloodUreaSI, creatinineSI, bloodUricSI, totalCholesterolSI, triglyceridesSI,
            HDLSI, LDLSI;

    @FXML
    private JFXTextField inputFasting, inputBloodUrea, inputCreatinine, inputBloodUric, inputTotalCholesterol, inputTriglycerides,
    inputHDL, inputLDL, inputSGPT, inputSGOT;

    @FXML
    private JFXTextField inputFastingSI, inputBloodUreaSI, inputCreatinineSI, inputBloodUricSI, inputTotalCholesterolSI,
    inputTriglyceridesSI, inputHDLSI, inputLDLSI;

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
        inputFastingSI.setText(DataHolder.fastingSI);
        inputBloodUreaSI.setText(DataHolder.bloodUreaSI);
        inputCreatinineSI.setText(DataHolder.creatinineSI);
        inputBloodUricSI.setText(DataHolder.bloodUricSI);
        inputTotalCholesterolSI.setText(DataHolder.totalCholesterolSI);
        inputTriglyceridesSI.setText(DataHolder.triglyceridesSI);
        inputHDLSI.setText(DataHolder.HDLSI);
        inputLDLSI.setText(DataHolder.LDLSI);
    }

    public void resetValues(){
        DataHolder.fasting = DataHolder.bloodUrea = DataHolder.creatinine = DataHolder.bloodUric = DataHolder.totalCholesterol
                = DataHolder.triglycerides = DataHolder.HDL = DataHolder.LDL = DataHolder.SGPT = DataHolder.SGOT =
                DataHolder.bloodChemistryRemarks = DataHolder.fastingSI = DataHolder.bloodUreaSI = DataHolder.creatinineSI =
                DataHolder.bloodUricSI = DataHolder.totalCholesterolSI = DataHolder.triglyceridesSI = DataHolder.HDLSI =
                        DataHolder.LDLSI = "";
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
        //
        fastingSI = inputFastingSI.getText();
        bloodUreaSI = inputBloodUreaSI.getText();
        creatinineSI = inputCreatinineSI.getText();
        bloodUricSI = inputBloodUricSI.getText();
        totalCholesterolSI = inputTotalCholesterolSI.getText();
        triglyceridesSI = inputTriglyceridesSI.getText();
        HDLSI = inputHDLSI.getText();
        LDLSI = inputLDLSI.getText();
        //
        SGPT = inputSGPT.getText();
        SGOT = inputSGOT.getText();
        bloodChemistryRemarks = inputBloodChemistryRemarks.getText();
        String updateBC = "UPDATE SecondaryInfo SET BCFastingBloodSugar = ?, BCBlood = ?, BCCreatinine = ?, BCUricAcid = ?, " +
                "BCCholesterol = ?, BCTriglycerides = ?, BCHDL = ?, BCLDL = ?, BCSGPT = ?, BCSGOT = ?, BCRemarks = ?," +
                " BCBloodSugarSI = ?, BCBloodUreaSI = ?, BCCreatinineSI = ?, BCUricSI = ?, BCCholesterolSI = ?, " +
                "BCTriglyceridesSI = ?, BCHDLSI = ?, BCLDLSI = ? WHERE" +
                " MRNID = ?";
        DBHelper.executeQuery(updateBC, new String[]{fasting, bloodUrea, creatinine, bloodUric, totalCholesterol, triglycerides,
        HDL, LDL, SGPT, SGOT, bloodChemistryRemarks, fastingSI, bloodUreaSI, creatinineSI, bloodUricSI, totalCholesterolSI, triglyceridesSI,
                HDLSI, LDLSI, DataHolder.selectedMRNID}, StatementType.UPDATE);
        DataHolder.config.createConfigTest("BloodChemistry.biochem", new String[]{fasting, bloodUrea, creatinine, bloodUric, totalCholesterol,
                triglycerides, HDL, LDL, SGPT, SGOT, fastingSI, bloodUreaSI, creatinineSI, bloodUricSI, totalCholesterolSI, triglyceridesSI,
                HDLSI, LDLSI, bloodChemistryRemarks});
        onClose();
    }

    public void onClose(){
        resetValues();
        Stage bloodChemistryForm = (Stage) btnSave.getScene().getWindow();
        bloodChemistryForm.close();
    }
}
