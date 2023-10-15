package Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    public BorderPane dashBoard;
    public JFXButton btnEmployee;
    public JFXButton btnReports;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String email = LoginController.getEmail();

        if(LoginController.getUserType(email).equals("Default")){
            btnEmployee.setDisable(true);
            btnReports.setDisable(true);
        }else{
            btnEmployee.setDisable(false);
            btnReports.setDisable(false);
        }
    }


    public void btnItemForm(ActionEvent actionEvent) {
        Stage stage = (Stage) dashBoard.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/ItemForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void btnOrderDetailsForm(ActionEvent actionEvent) {
        Stage stage = (Stage) dashBoard.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/OrderDetailForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void btnSupplierForm(ActionEvent actionEvent) {

        Stage stage = (Stage) dashBoard.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/SupplierForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void btnEmployeeForm(ActionEvent actionEvent) {
        Stage stage = (Stage) dashBoard.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/EmployeeForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void btnPlaceOrderForm(ActionEvent actionEvent) {
        Stage stage = (Stage) dashBoard.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/PlaceOrderForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void btnReportForm(ActionEvent actionEvent) {

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) dashBoard.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/LoginForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }


}
