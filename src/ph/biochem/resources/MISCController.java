package ph.biochem.resources;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ph.biochem.modules.DataHolder;


public class MISCController {
    public String miscTests, miscRemarks;

    @FXML
    public TextArea inputMiscTests, inputMiscRemarks;

    @FXML
    public Button btnSave, btnClose;

    @FXML
    public void initialize(){
        setValues();
    }

    private void setValues(){
        inputMiscTests.setText(DataHolder.miscTests);
        inputMiscRemarks.setText(DataHolder.miscRemarks);
    }

    private void resetValues(){
        DataHolder.miscTests = DataHolder.miscRemarks = "";
    }

    public void onClickBtnSave(){
        miscRemarks = inputMiscRemarks.getText();
        miscTests = inputMiscTests.getText();
        onClose();
    }

    public void onClose(){
        resetValues();
        Stage MISCForm = (Stage) btnClose.getScene().getWindow();
        MISCForm.close();
    }
}