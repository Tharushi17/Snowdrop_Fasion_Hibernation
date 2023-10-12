package Controller;

import Entity.User;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;
import com.jfoenix.controls.JFXPasswordField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController implements Initializable {

    public JFXButton btnAdd;
    public JFXComboBox cmbUserType;
    public JFXTextField txtAdminEmail;
    public JFXPasswordField txtAdminPassword;
    public BorderPane userForm;
    @FXML
    private JFXPasswordField txtConfirm;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXPasswordField txtPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setUser();

        if (doesAdminUserExist()){
            btnAdd.setDisable(true);

            txtName.setDisable(true);
            txtEmail.setDisable(true);
            txtPassword.setDisable(true);
            txtConfirm.setDisable(true);
        }

        txtAdminPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER){
                if(!txtAdminEmail.getText().isEmpty()){
                    confirmAdmin();
                }
            }
        });


    }


    @FXML
    void btnAddOnAction(ActionEvent event) {

        User user = new User(
                txtEmail.getText(),
                txtName.getText(),
                BCrypt.hashpw(txtPassword.getText(),BCrypt.gensalt()),
                cmbUserType.getValue().toString()

        );


        if(confirmPassword()){
            if (emailFormat()){
                Session session = HibernateUtil.getSession();

                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                session.close();
                new Alert(Alert.AlertType.INFORMATION,"User added successfully..").show();

                clearFields();
            }
            new Alert(Alert.AlertType.ERROR,"Please enter a valid email..").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Please enter the same password...").show();

        }
    }

    private void setUser() {
        cmbUserType.getItems().setAll("Admin", "Default");
    }

    public void cmbOnAction(ActionEvent actionEvent) {
        if (cmbUserType.getValue()!=null){
            if(cmbUserType.getValue().equals("Admin")){
                if(doesAdminUserExist()){

                    btnAdd.setDisable(true);

                    txtEmail.setDisable(true);
                    txtName.setDisable(true);
                    txtPassword.setDisable(true);
                    txtConfirm.setDisable(true);

                }
            }else if(cmbUserType.getValue()!=null && cmbUserType.getValue().equals("Default")){
                confirmAdmin();
                btnAdd.setDisable(false);

                txtEmail.setDisable(false);
                txtName.setDisable(false);
                txtPassword.setDisable(false);
                txtConfirm.setDisable(false);
            }
        }
    }


    private boolean doesAdminUserExist(){
        Session session = HibernateUtil.getSession();
        long count = (long) session.createQuery("SELECT COUNT(*) FROM User WHERE userType = 'Admin'")
                .uniqueResult();
        return count>0;
    }


    private void confirmAdmin(){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("SELECT password, email FROM User WHERE userType = 'Admin'");

        List<Object[]> list = query.list();

        for (Object[] results:list) {
            String hashedPassword = (String) results[0];
            String email = (String) results[1];

            if(email != null && email.equals(txtAdminEmail.getText()) && BCrypt.checkpw(txtAdminPassword.getText(), hashedPassword )){
                new Alert(Alert.AlertType.INFORMATION,"You can Add new user....").show();

                txtName.setDisable(false);
                txtConfirm.setDisable(false);
                txtEmail.setDisable(false);

                btnAdd.setDisable(false);

            }else {
                new Alert(Alert.AlertType.ERROR,"Email or password is wrong..").show();
                txtName.setDisable(false);
                txtConfirm.setDisable(false);
                txtEmail.setDisable(false);
                btnAdd.setDisable(false);
            }
        }
    }


    private boolean emailFormat(){
        String emailAddress = txtEmail.getText();
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(emailAddress);

        return matcher.matches();
    }

    private boolean confirmPassword(){
        String password = txtPassword.getText();
        String confirm = txtConfirm.getText();

        if(!password.isEmpty() && !confirm.isEmpty() && (password.length() == confirm.length())){
            return password.equals(confirm);
        }
        return false;
    }


    private void clearFields(){
        txtName.clear();
        txtPassword.clear();
        txtName.clear();
        txtConfirm.clear();
    }


    public void btnBackOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) userForm.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/LoginForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }



}
