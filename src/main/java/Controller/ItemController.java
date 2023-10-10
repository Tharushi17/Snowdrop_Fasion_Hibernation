package Controller;
import Entity.Item;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ItemController implements Initializable {

    public JFXTextField txtCode;
    public Label lblDate;
    public BorderPane itemForm;
    @FXML
    private ComboBox<?> cmbCategory;

    @FXML
    private ComboBox<?> cmbSize;

    @FXML
    private ComboBox<?> cmbSupId;

    @FXML
    private Label lblProfit;

    @FXML
    private Label lblStock;

    @FXML
    private JFXTextField txtBuyingPrice;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtNewQty;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtSellingPrice;

    @FXML
    private JFXTextField txtSupName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    
    @FXML
    void btnAddOnAction(ActionEvent event) {

        Item item = new Item(
                txtCode.getText(),
                txtDescription.getText(),
                Integer.parseInt(lblStock.getText()),
                Double.parseDouble(txtBuyingPrice.getText()),
                Double.parseDouble(txtSellingPrice.getText()),
                Double.parseDouble(lblProfit.getText()),
                cmbSupId.getId(),
                cmbCategory.getId()
        );

        Session session = HibernateUtil.getSession();

        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }



    public void btnCategoryOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) itemForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/CategoryForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();

    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) itemForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }


}


