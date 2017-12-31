package ph.biochem.resources;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ph.biochem.modules.AlertDialog;
import ph.biochem.modules.DataHolder;

public class ClinicalController extends AlertDialog{
    public String temperature, pulseRate, respiratoryRate, bloodPressure, eyeGlasses, colorVision, right, left, bmiRemarks;
    public double weight, height, BMI;
    private final double CENTIMER_PER_METER = 0.01;

    @FXML
    public JFXTextField inputTemp, inputPulse, inputRespiratory, inputBlood, inputWeight, inputHeight, inputRight, inputLeft;

    @FXML
    public JFXComboBox comboEye, comboColor;

    @FXML
    public JFXTextField displayBMIScore;

    @FXML
    public JFXComboBox displayBMIRemarks;

    @FXML
    public Button btnSave, btnCompute, btnClose;

    @FXML
    private void initialize(){
        comboColor.getItems().addAll(new String[]{"adequate", "inadequate"});
        comboEye.getItems().addAll(new String[]{"with eyeglasses", "without eyeglasses"});
        setValues();
    }

    private void setValues(){
        inputTemp.setText(DataHolder.temperature);
        inputPulse.setText(DataHolder.pulseRate);
        inputRespiratory.setText(DataHolder.respiratoryRate);
        inputBlood.setText(DataHolder.bloodPressure);
        if(DataHolder.weight != 0){
            inputWeight.setText(Double.toString(DataHolder.weight));
        }
        if(DataHolder.height != 0){
            inputHeight.setText(Double.toString(DataHolder.height));
        }
        if(DataHolder.BMI != 0){
            displayBMIScore.setText(Double.toString(DataHolder.BMI));
        }
        if(!DataHolder.bmiRemarks.isEmpty()){
            displayBMIRemarks.setPromptText(DataHolder.bmiRemarks);
        }
        comboEye.setValue(DataHolder.eyeGlasses);
        comboColor.setValue(DataHolder.colorVision);
        inputRight.setText(DataHolder.right);
        inputLeft.setText(DataHolder.left);
    }

    private void resetValues(){
        DataHolder.temperature = DataHolder.pulseRate = DataHolder.respiratoryRate = DataHolder.bloodPressure
                = DataHolder.eyeGlasses = DataHolder.colorVision = DataHolder.right = DataHolder.left = DataHolder.bmiRemarks = "";
        DataHolder.weight = DataHolder.height = DataHolder.BMI=0;
    }

    public void onClose(){
        resetValues();
        Stage clinicalForm = (Stage) btnClose.getScene().getWindow();
        clinicalForm.close();
    }

    @FXML
    private void onClickComputeBtn(){
        weight = Double.parseDouble(inputWeight.getText());
        height = Double.parseDouble(inputHeight.getText());
        computeBMI();
        setBMIRemarks();
        btnSave.setDisable(false);
    }

    @FXML
    private void onClickSaveBtn(){
        if(showDialogBox("Confirmation", "Are you sure you want to save these information? Further editing would be disabled after this window is closed.\n\nYou can still edit the patient's information after saving the patient.")){
            temperature = inputTemp.getText();
            pulseRate = inputPulse.getText();
            respiratoryRate = inputRespiratory.getText();
            bloodPressure = inputBlood.getText();
            if(comboEye.getValue() != null){
                eyeGlasses = comboEye.getValue().toString();
            }
            if(comboColor.getValue() != null){
                colorVision = comboColor.getValue().toString();
            }
            right = inputRight.getText();
            left = inputLeft.getText();
            onClose();
        }
    }

    private void computeBMI(){
        BMI = weight/((height*CENTIMER_PER_METER)*(height*CENTIMER_PER_METER));
        displayBMIScore.setText(String.format("%.1f", BMI));
    }

    private void setBMIRemarks(){
        if(BMI < 18.5){
            bmiRemarks = "Underweight";
        }

        else if(BMI >= 18.5 && BMI <= 24.99){
            bmiRemarks = "Normal";
        }

        else if(BMI >= 25 && BMI < 30){
            bmiRemarks = "Overweight";
        }

        else if(BMI >= 30){
            bmiRemarks = "Obese";
        }

        else{
            bmiRemarks = "Unknown";
        }

        displayBMIRemarks.setPromptText(bmiRemarks);
    }
}
