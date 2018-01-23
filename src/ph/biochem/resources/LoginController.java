package ph.biochem.resources;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ph.biochem.modules.AlertDialog;
import ph.biochem.modules.DataHolder;

import java.io.File;
import java.util.Scanner;

public class LoginController extends AlertDialog{
    @FXML
    private Button btnLogin;
    @FXML
    private JFXTextField inputUserName;
    @FXML
    private JFXPasswordField inputPassword;

    public void onClickBtnLogin(){
        try{
            Scanner accountsConfig = new Scanner(new File("accounts.biochem"));
            accountsConfig.nextLine();
            String adminUserName = accountsConfig.nextLine();
            String adminPassword = accountsConfig.nextLine();
            accountsConfig.nextLine();
            accountsConfig.nextLine();
            String normalAccUser = accountsConfig.nextLine();
            String normalAccPassword = accountsConfig.nextLine();

            String userName = isNull(inputUserName.getText());
            String password = isNull(inputPassword.getText());

            if(userName.equals(adminUserName)){
                if(password.equals(adminPassword)){
                    DataHolder.isAdmin = true;
                    showMainPage();
                }
                else{
                    showSuccess("Error!", "Invalid Credentials.");
                }
            }
            else if(userName.equals(normalAccUser)){
                if(password.equals(normalAccPassword)){
                    DataHolder.isAdmin = false;
                    showMainPage();
                }
                else{
                    showSuccess("Error!", "Invalid Credentials.");
                }
            }
            else{
                showSuccess("Error!", "Invalid Credentials.");
            }
        }
        catch(Exception e){
            showSuccess("Error!", "Accounts config file not found.");
            e.printStackTrace();
        }
    }

    public void showMainPage(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
            Stage loginForm = (Stage) btnLogin.getScene().getWindow();
            loginForm.close();
        }
        catch(Exception e){
            showSuccess("Error", "Cannot find main page resources.");
        }
    }
}
