package ph.biochem.resources;

import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ph.biochem.modules.DataHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatesController {
    @FXML
    private JFXDatePicker dateRequest, datePerformed, dateReleased;
    @FXML
    private Button btnDateGenerate;

    public void onClickDateGenerate() throws ParseException{
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("M/d/yyyy");
        toFormat.setLenient(false);
        if(dateRequest.getValue() != null){
            DataHolder.dateRequest = toFormat.format(fromFormat.parse(dateRequest.getValue().toString()));
        }
        if(datePerformed.getValue() != null){
            DataHolder.datePerformed = toFormat.format(fromFormat.parse(datePerformed.getValue().toString()));
        }
        if(dateReleased.getValue() != null){
            DataHolder.dateReleased = toFormat.format(fromFormat.parse(dateReleased.getValue().toString()));
        }
        DataHolder.config.createConfigTest("Dates.biochem", new String[]{DataHolder.dateRequest,
                DataHolder.datePerformed, DataHolder.dateReleased});
        Stage DatesForm = (Stage) btnDateGenerate.getScene().getWindow();
        DatesForm.close();
    }
}
