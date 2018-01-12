package ph.biochem.resources;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ph.biochem.modules.DBHelper;
import ph.biochem.modules.DataHolder;
import ph.biochem.modules.StatementType;


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
        String updateMisc = "UPDATE SecondaryInfo SET MiscTests = ?, MiscRemarks = ? WHERE MRNID = ?";
        DBHelper.executeQuery(updateMisc, new String[]{miscTests, miscRemarks, DataHolder.selectedMRNID}, StatementType.UPDATE);
        DataHolder.config.createConfigTest("Misc.biochem", new String[]{miscTests, miscRemarks});
        onClose();
    }

    public void onClose(){
        resetValues();
        Stage MISCForm = (Stage) btnSave.getScene().getWindow();
        MISCForm.close();
    }
}
