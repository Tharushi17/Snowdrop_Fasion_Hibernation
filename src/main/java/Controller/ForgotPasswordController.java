package Controller;

import Entity.User;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXPasswordField;
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
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {

    public JFXPasswordField txtOtp;
    @FXML
    private BorderPane loginForm;

    @FXML
    private JFXPasswordField txtConfirm;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtPassword.setDisable(true);
        txtConfirm.setDisable(true);


        txtEmail.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER){
                if(!txtEmail.getText().isEmpty() && emailDoesExist()){
                    //sendOTPtoMail();
                    new Alert(Alert.AlertType.INFORMATION,"You can update your password...").show();
                    txtPassword.setDisable(false);
                    txtConfirm.setDisable(false);
                }else{
                    new Alert(Alert.AlertType.ERROR,"No user Email Found...").show();
                }
            }
        });
    }

    private void sendOTPtoMail() {
        String smtHostServer ;
    }

    private String generateOTP(){
        Random random = new Random();
        int otp = 100000+ random.nextInt();
        return String.valueOf(otp);
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) loginForm.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/LoginForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        txtConfirm.clear();
        txtPassword.clear();
        txtEmail.clear();
    }

    @FXML
    void btnChangeOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();

        User user = session.find(User.class, txtEmail.getText());

        if(confirmPassword()){
            user.setPassword(txtConfirm.getText());
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
        }else{
            new Alert(Alert.AlertType.ERROR,"Passwords don't Match...").show();
        }
        new Alert(Alert.AlertType.INFORMATION,"Password is updated...").show();
    }




    private boolean emailDoesExist() {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("SELECT email FROM User");
        List<String> emailList = query.list();

        for (String email : emailList) {
            if (email != null && email.equals(txtEmail.getText())) {
                return true;
            }
        }
        return false;
    }


    private boolean confirmPassword(){
        String password = txtPassword.getText();
        String confirm = txtConfirm.getText();

        if(!password.isEmpty() && !confirm.isEmpty() && (password.length() == confirm.length())){
            return password.equals(confirm);
        }
        return false;
    }


}

