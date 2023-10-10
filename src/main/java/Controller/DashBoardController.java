package Controller;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    }

    public void btnPlaceOrderForm(ActionEvent actionEvent) {

    }

    public void btnReportForm(ActionEvent actionEvent) {

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {

    }


}
