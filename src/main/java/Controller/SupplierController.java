package Controller;

import Entity.Supplier;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.SupplierTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class SupplierController implements Initializable {

    public BorderPane supplierForm;
    public JFXTreeTableView<SupplierTm> tblSupplier;
    public TreeTableColumn colId;
    public TreeTableColumn colName;
    public TreeTableColumn colCompany;
    public TreeTableColumn colContact;
    public JFXTextField txtSearch;
    @FXML
    private Label lblCode;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtSupName;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierName"));
        colContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("contact"));
        colCompany.setCellValueFactory(new TreeItemPropertyValueFactory<>("company"));


        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if(newValue != null){
                setData(newValue);
            }
        });

        generateId();
        loadTable();


        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                tblSupplier.setPredicate(new Predicate<TreeItem<SupplierTm>>() {
                    @Override
                    public boolean test(TreeItem<SupplierTm> supplierTmTreeItem) {
                        return supplierTmTreeItem.getValue().getSupplierId().contains(newVal) ||
                                supplierTmTreeItem.getValue().getSupplierName().contains(newVal);

                    }
                });
            }
        });

    }


    private void setData(TreeItem<SupplierTm> value) {
        lblCode.setText(value.getValue().getSupplierId());
        txtSupName.setText(value.getValue().getSupplierName());
        txtContact.setText(value.getValue().getContact());
        txtCompany.setText(value.getValue().getCompany());
    }



    private void loadTable() {
        ObservableList<SupplierTm> tblList = FXCollections.observableArrayList();

        Session session = HibernateUtil.getSession();
        Query<Supplier> query = session.createQuery("FROM Supplier");
        List<Supplier> list = query.list();


        for (Supplier supplier:list) {

            tblList.add(new SupplierTm(
                    supplier.getSupplierId(),
                    supplier.getSupplierName(),
                    supplier.getContact(),
                    supplier.getComapny()
            ));
        }

        TreeItem<SupplierTm> treeItem = new RecursiveTreeItem<>(tblList, RecursiveTreeObject::getChildren);
        tblSupplier.setRoot(treeItem);
        tblSupplier.setShowRoot(false);
    }



    @FXML
    void btnAddOnAction(ActionEvent event) {

        if(!checkValidity()){
            new Alert(Alert.AlertType.INFORMATION,"Invalid Contact..").show();

        }else{
            Supplier supplier = new Supplier(
                    lblCode.getText(),
                    txtSupName.getText(),
                    txtContact.getText(),
                    txtCompany.getText()
            );

            Session session = HibernateUtil.getSession();

            session.beginTransaction();
            session.save(supplier);
            session.getTransaction().commit();
            session.close();

            loadTable();
            new Alert(Alert.AlertType.INFORMATION,"Supplier Saved Successfully..").show();
        }
    }

    boolean checkValidity(){
        if(txtContact.getText().length() != 10 || txtContact.getText().charAt(0) != '0'){
            return false;
        }
        return true;
    }



    @FXML
    void btnDeleteOnAction(ActionEvent event) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(Supplier.class, lblCode.getText()));
        transaction.commit();
        session.close();

        new Alert(Alert.AlertType.INFORMATION,"Supplier Deleted Successfully..").show();
        loadTable();
        clearFields();

    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();
        Supplier supplier = session.find(Supplier.class, lblCode.getText());

        supplier.setSupplierId(lblCode.getText());
        supplier.setSupplierName(txtSupName.getText());
        supplier.setContact(txtContact.getText());
        supplier.setComapny(txtCompany.getText());

        Transaction transaction = session.beginTransaction();
        session.save(supplier);
        transaction.commit();
        session.close();

        loadTable();
        clearFields();
    }

    private void generateId() {
        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("FROM Supplier ORDER BY supplierId DESC");
        query.setMaxResults(1);

        List<Supplier> supplierList = query.list();

        if(!supplierList.isEmpty()){
            Supplier lastSupplier = supplierList.get(0);
            String lastId = lastSupplier.getSupplierId();

            if(lastId != null && !lastId.isEmpty()){
                int num = Integer.parseInt(lastId.split("[S]")[1]);
                num++;
                lblCode.setText(String.format("S%04d",num));

            }
        }else{
            lblCode.setText("S0001");
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        generateId();
        txtSupName.clear();
        txtCompany.clear();
        txtContact.clear();
        txtSearch.clear();
    }

    //------------------------- form controlling --------------------------

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        Stage stage = (Stage)supplierForm .getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/ItemForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) supplierForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
    //---------------------------------------------------------------------

}

