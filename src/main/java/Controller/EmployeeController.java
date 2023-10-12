package Controller;

import Entity.Employee;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.EmployeeTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployeeController implements Initializable {

    public BorderPane employeeForm;
    @FXML
    private TreeTableColumn<?, ?> colAddress;

    @FXML
    private TreeTableColumn<?, ?> colContact;

    @FXML
    private TreeTableColumn<?, ?> colDob;

    @FXML
    private TreeTableColumn<?, ?> colId;

    @FXML
    private TreeTableColumn<?, ?> colName;

    @FXML
    private TreeTableColumn<?, ?> colNic;

    @FXML
    private Label lblId;

    @FXML
    private JFXTreeTableView<EmployeeTm> tblEmployee;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtNic;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtContact;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new TreeItemPropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("contact"));
        colNic.setCellValueFactory(new TreeItemPropertyValueFactory<>("nic"));
        colDob.setCellValueFactory(new TreeItemPropertyValueFactory<>("dob"));


        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if(newValue != null){
                setData(newValue);
            }
        });

        generateId();
        loadTable();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                tblEmployee.setPredicate(new Predicate<TreeItem<EmployeeTm>>() {
                    @Override
                    public boolean test(TreeItem<EmployeeTm> employeeTmTreeItem) {
                        return employeeTmTreeItem.getValue().getEmpId().contains(newVal)||
                                employeeTmTreeItem.getValue().getName().contains(newVal);
                    }
                });
            }
        });
    }



//----------------------- ADD --------------------
    @FXML
    void btnAddOnAction(ActionEvent event) {
        if(!validContact()){
            new Alert(Alert.AlertType.ERROR,"Invalid Contact..").show();

             //new Alert(Alert.AlertType.ERROR,"Invalid Date of birth..").show();

        }else{
            Employee employee = new Employee(
                    lblId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContact.getText(),
                    txtNic.getText(),
                    txtDob.getText()
            );

            Session session = HibernateUtil.getSession();

            session.beginTransaction();
            session.save(employee);
            session.getTransaction().commit();
            session.close();

            loadTable();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION,"Employee added Successfully..").show();

        }
    }


//-------------- validity -------------------------
    boolean validContact(){
        if(txtContact.getText().length() != 10 || txtContact.getText().charAt(0) != '0'){
            return false;
        }
        return true;
    }

    boolean validDate(){
        try{
            DateTimeFormatter DataTimeFormatter = null;
            DateTimeFormatter formatter = DataTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(txtDob.getText(), formatter);

            return true;
        }catch (Exception e){

            return false;
        }

    }

//----------------------- DELETE --------------------
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Optional<ButtonType> btnType = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this Employee and ASSOCIATED DATA ?", ButtonType.YES, ButtonType.NO).showAndWait();

        if(btnType.get() == ButtonType.YES) {
            session.delete(session.find(Employee.class, lblId.getText()));
            transaction.commit();
            session.close();

            loadTable();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully..").show();

        }else{
            new Alert(Alert.AlertType.ERROR,"Something Went Wrong..").show();
        }

    }


//----------------------- UPDATE --------------------
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();
        Employee employee = session.find(Employee.class, lblId.getText());

        employee.setEmpId(lblId.getText());
        employee.setName(txtName.getText());
        employee.setAddress(txtAddress.getText());
        employee.setContact(txtContact.getText());
        employee.setNic(txtNic.getText());
        employee.setDob(txtDob.getText());

        Transaction transaction = session.beginTransaction();
        session.save(employee);
        transaction.commit();
        session.close();

        loadTable();
        clearFields();
        new Alert(Alert.AlertType.INFORMATION,"Employee updated Successfully..").show();

    }


//----------------------- Clear --------------------
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        generateId();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtDob.clear();
        txtNic.clear();
        txtSearch.clear();

    }


//-------------- load data to table -------------
    private void loadTable(){
        ObservableList<EmployeeTm> tblList = FXCollections.observableArrayList();

        Session session = HibernateUtil.getSession();
        Query<Employee> query = session.createQuery("FROM Employee");
        List<Employee> list = query.list();

        for (Employee employee:list) {

            tblList.add(new EmployeeTm(
                    employee.getEmpId(),
                    employee.getName(),
                    employee.getAddress(),
                    employee.getContact(),
                    employee.getNic(),
                    employee.getDob()
            ));
        }

        TreeItem<EmployeeTm> treeItem = new RecursiveTreeItem<>(tblList, RecursiveTreeObject::getChildren);
        tblEmployee.setRoot(treeItem);
        tblEmployee.setShowRoot(false);
    }


    private void setData(TreeItem<EmployeeTm> value){
        lblId.setText(value.getValue().getEmpId());
        txtName.setText(value.getValue().getName());
        txtAddress.setText(value.getValue().getAddress());
        txtContact.setText(value.getValue().getContact());
        txtNic.setText(value.getValue().getNic());
        txtDob.setText(value.getValue().getDob());
    }



 //----------------- Generate ID -----------------
    private void generateId(){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("From Employee ORDER BY empId DESC");
        query.setMaxResults(1);

        List<Employee> employees = query.list();

        if(!employees.isEmpty()){
            Employee lastEmployee = employees.get(0);
            String lastId = lastEmployee.getEmpId();

            if(lastId != null && !lastId.isEmpty()){
                int num = Integer.parseInt(lastId.split(("_"))[1]);
                num++;
                lblId.setText(String.format("EMP_%04d", num));

            }
        }else {
            lblId.setText("EMP_0001");
        }
    }



    public void btnBackOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) employeeForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}
