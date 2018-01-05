package ph.biochem.resources;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import ph.biochem.modules.AlertDialog;
import ph.biochem.modules.DataHolder;

import java.time.LocalDate;

public class FamilyHealthController extends AlertDialog{
    public String hospitalizations, surgery, presentMed, remarks, smoker, alcohol, mensDate,
            gravida, para, t, p, a, l, m, sticksPerDay, smokerYrs, bottlesPerSession, drinkerYrs;
    @FXML
    private JFXComboBox comboHospitalizations, comboSurgery, comboMedications, comboSmoker, comboDrinker;
    @FXML
    private JFXTextField inputRemarks, inputSticksPerDay, inputSmokerYrs, inputBottlesPerSession, inputDrinkerYrs,
    inputGravida, inputPara, inputT, inputP, inputA, inputL, inputM;
    @FXML
    private DatePicker inputMensDate;
    @FXML
    private Button btnSave, btnClose;

    public void onClose(){
        //resetValues();
        retainValues();
        Stage familyForm = (Stage) btnClose.getScene().getWindow();
        familyForm.close();
    }

    public void onClickBtnSave(){
        if(showDialogBox("Confirmation", "Are you sure you want to save these information? " +
                "Further editing would be disabled after this window is closed.\n\nYou can still edit the patient's" +
                " information after saving the patient.")){
            if(comboHospitalizations.getValue() != null){
                hospitalizations = comboHospitalizations.getValue().toString();
            }
            if(comboSurgery.getValue() != null){
                surgery = comboSurgery.getValue().toString();
            }
            if(comboMedications.getValue() != null){
                presentMed = comboMedications.getValue().toString();
            }
            remarks = inputRemarks.getText();

            if(comboSmoker.getValue() != null){
                smoker = comboSmoker.getValue().toString();
            }
            sticksPerDay = inputSticksPerDay.getText();
            smokerYrs = inputSmokerYrs.getText();

            if(comboDrinker.getValue() != null){
                alcohol = comboDrinker.getValue().toString();
            }
            bottlesPerSession = inputBottlesPerSession.getText();
            drinkerYrs = inputDrinkerYrs.getText();

            if(inputMensDate.getValue() != null){
                mensDate = inputMensDate.getValue().toString();
            }
            gravida = inputGravida.getText();
            para = inputPara.getText();
            t = inputT.getText();
            p = inputP.getText();
            a = inputA.getText();
            l = inputL.getText();
            m = inputM.getText();

            onClose();
        }
    }

    public void initialize(){
        initComboBoxes();
        setValues();
    }

    public void setValues(){
        comboHospitalizations.setValue(DataHolder.hospitalizations);
        comboSurgery.setValue(DataHolder.surgery);
        comboMedications.setValue(DataHolder.presentMed);
        inputRemarks.setText(DataHolder.remarks);
        comboSmoker.setValue(DataHolder.smoker);
        comboDrinker.setValue(DataHolder.alcohol);
        if(DataHolder.mensDate != null){
            if(!DataHolder.mensDate.isEmpty()){
                inputMensDate.setValue(LocalDate.parse(DataHolder.mensDate));
            }
        }
        inputGravida.setText(DataHolder.gravida);
        inputPara.setText(DataHolder.para);
        inputT.setText(DataHolder.t);
        inputP.setText(DataHolder.p);
        inputA.setText(DataHolder.a);
        inputL.setText(DataHolder.l);
        inputM.setText(DataHolder.m);
        inputSticksPerDay.setText(DataHolder.sticksPerDay);
        inputSmokerYrs.setText(DataHolder.smokerYrs);
        inputBottlesPerSession.setText(DataHolder.bottlesPerSession);
        inputDrinkerYrs.setText(DataHolder.drinkerYrs);
    }

    public void retainValues(){
        if(comboHospitalizations.getValue() != null){
            DataHolder.hospitalizations=comboHospitalizations.getValue().toString();
        }
        if(comboSurgery.getValue() != null){
            DataHolder.surgery=comboSurgery.getValue().toString();
        }
        if(comboMedications.getValue() != null){
            DataHolder.presentMed=comboMedications.getValue().toString();
        }
        if(comboSmoker.getValue() != null){
            DataHolder.smoker=comboSmoker.getValue().toString();
        }
        if(comboDrinker.getValue() != null){
            DataHolder.alcohol=comboDrinker.getValue().toString();
        }
        if(inputMensDate.getValue() != null){
            DataHolder.mensDate=inputMensDate.getValue().toString();
        }

        DataHolder.remarks=inputRemarks.getText();
        DataHolder.gravida=inputGravida.getText();
        DataHolder.para=inputPara.getText();
        DataHolder.t=inputT.getText();
        DataHolder.p=inputP.getText();
        DataHolder.a=inputA.getText();
        DataHolder.l=inputL.getText();
        DataHolder.m=inputM.getText();
        DataHolder.sticksPerDay=inputSticksPerDay.getText();
        DataHolder.smokerYrs=inputSmokerYrs.getText();
        DataHolder.bottlesPerSession=inputBottlesPerSession.getText();
        DataHolder.drinkerYrs=inputDrinkerYrs.getText();
    }

    public void resetValues(){
        DataHolder.hospitalizations=DataHolder.surgery=DataHolder.presentMed=DataHolder.remarks=DataHolder.smoker=
                DataHolder.alcohol=DataHolder.mensDate=DataHolder.gravida=DataHolder.para=DataHolder.t=DataHolder.p=DataHolder.a=
                        DataHolder.l=DataHolder.m= DataHolder.sticksPerDay=DataHolder.smokerYrs=DataHolder.bottlesPerSession=
                                DataHolder.drinkerYrs="";
    }

    private void initComboBoxes(){
        initComboBox(comboHospitalizations);
        initComboBox(comboSurgery);
        initComboBox(comboMedications);
        initComboBox(comboSmoker);
        initComboBox(comboDrinker);
    }

    private void initComboBox(JFXComboBox combo){
        combo.getItems().addAll(new String[]{"Yes", "No"});
    }

    public void onOptionChangedSmoker(){
        if(comboSmoker.getValue().toString().equals("Yes")){
            inputSticksPerDay.setDisable(false);
            inputSmokerYrs.setDisable(false);
        }
        else{
            inputSticksPerDay.setDisable(true);
            inputSmokerYrs.setDisable(true);
        }
    }

    public void onOptionChangedDrinker(){
        if(comboDrinker.getValue().toString().equals("Yes")){
            inputBottlesPerSession.setDisable(false);
            inputDrinkerYrs.setDisable(false);
        }
        else{
            inputBottlesPerSession.setDisable(true);
            inputDrinkerYrs.setDisable(true);
        }
    }

}
