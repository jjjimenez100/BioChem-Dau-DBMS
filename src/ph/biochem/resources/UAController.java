package ph.biochem.resources;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ph.biochem.modules.DBHelper;
import ph.biochem.modules.DataHolder;
import ph.biochem.modules.StatementType;


public class UAController {
    public String UAColor, transparency, protein, sugar, specgrav, phLevel, UAPus, UARBC,
            epithelial, mucus, bacteria, urates, phosphates, casts, crystals, UAOthers, UARemarks;

    @FXML
    public JFXTextField inputColor, inputTransparency, inputProtein, inputSugar, inputSpecGrav, inputPH, inputPus, inputRBC,
    inputEpithelial, inputMucus, inputBacteria, inputUrates, inputPhosphates, inputCasts, inputCrystals, inputOthers;

    @FXML
    public TextArea inputRemarks;

    @FXML
    public Button btnSave, btnClose;

    @FXML
    public void initialize(){
        setValues();
    }

    private void setValues(){
        inputColor.setText(DataHolder.UAColor);
        inputTransparency.setText(DataHolder.transparency);
        inputProtein.setText(DataHolder.protein);
        inputSugar.setText(DataHolder.sugar);
        inputSpecGrav.setText(DataHolder.specgrav);
        inputPH.setText(DataHolder.phLevel);
        inputPus.setText(DataHolder.UAPus);
        inputRBC.setText(DataHolder.UARBC);
        inputEpithelial.setText(DataHolder.epithelial);
        inputMucus.setText(DataHolder.mucus);
        inputBacteria.setText(DataHolder.bacteria);
        inputUrates.setText(DataHolder.urates);
        inputPhosphates.setText(DataHolder.phosphates);
        inputCasts.setText(DataHolder.casts);
        inputCrystals.setText(DataHolder.crystals);
        inputOthers.setText(DataHolder.UAOthers);
        inputRemarks.setText(DataHolder.UARemarks);
    }

    private void resetValues(){
        DataHolder.UAColor=DataHolder.transparency=DataHolder.protein=DataHolder.sugar=DataHolder.specgrav=DataHolder.phLevel=DataHolder.UAPus=
                DataHolder.UARBC=DataHolder.epithelial=DataHolder. mucus=DataHolder.bacteria=DataHolder.urates=DataHolder.phosphates=
                        DataHolder.casts=DataHolder.crystals=DataHolder.UAOthers=DataHolder.UARemarks="";
    }

    public void onClickBtnSave(){
        UAColor = inputColor.getText();
        transparency = inputTransparency.getText();
        protein = inputProtein.getText();
        sugar = inputSugar.getText();
        specgrav = inputSpecGrav.getText();
        phLevel = inputPH.getText();
        UAPus = inputPus.getText();
        UARBC = inputRBC.getText();
        epithelial = inputEpithelial.getText();
        mucus = inputMucus.getText();
        bacteria = inputBacteria.getText();
        urates = inputUrates.getText();
        phosphates = inputPhosphates.getText();
        casts = inputCasts.getText();
        crystals = inputCrystals.getText();
        UAOthers = inputOthers.getText();
        UARemarks = inputRemarks.getText();
        String updateUA = "UPDATE SecondaryInfo SET UAColor = ?, UATransparency = ?, UAProtein = ?, UASugar = ?, UASpecGrav = ?, " +
                "UApHLevel = ?, UAPusCells = ?, UARBC = ?, UAEpithelial = ?, UAMucus = ?, UABacteria = ?, UAUrates = ?, " +
                "UAPhosphates = ?, UACasts = ?, UACrystals = ?, UAOthers = ?, UARemarks = ? WHERE MRNID = ?";
        DBHelper.executeQuery(updateUA, new String[]{UAColor, transparency, protein, sugar, specgrav, phLevel, UAPus, UARBC,
        epithelial, mucus, bacteria, urates, phosphates, casts, crystals, UAOthers, UARemarks, Integer.toString(DataHolder.selectedMRNID)},
                StatementType.UPDATE);
        DataHolder.config.createConfigTest("UA.biochem", new String[]{UAColor, transparency, protein, sugar, specgrav, phLevel, UAPus, UARBC,
                epithelial, mucus, bacteria, urates, phosphates, casts, crystals, UAOthers, UARemarks});
        onClose();
    }

    public void onClose(){
        resetValues();
        Stage UAForm = (Stage) btnSave.getScene().getWindow();
        UAForm.close();
    }
}
