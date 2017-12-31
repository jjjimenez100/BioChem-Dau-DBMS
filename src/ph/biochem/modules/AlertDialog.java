package ph.biochem.modules;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertDialog {
    private Alert alert;

    public boolean showDialogBox(String title, String msg){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(msg);

        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent()) && (result.get() == ButtonType.OK);
    }
}
