package Controller;

import Util.HibernateUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public BorderPane loginForm;
    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;
    private static String loginUserEmail;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    @FXML
    void forgotPasswordOnClick(MouseEvent event) {
        Stage stage = (Stage) loginForm.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/ForgotPasswordForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }



    private boolean confirmLogin (){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("SELECT password, email FROM User");

        List<Object[]> list = query.list();

        for (Object[] results:list) {
            String hashedPassword = (String) results[0];
            String email = (String) results[1];

            if(email != null && email.equals(txtEmail.getText()) && BCrypt.checkpw(txtPassword.getText(),hashedPassword)){
                loginUserEmail = email;
                return true;
            }
        }
        return false;

    }


    public static String getEmail(){
        return loginUserEmail;
    }

    public static String getUserType(String email){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("SELECT userType FROM User WHERE email = :email");
        query.setParameter("email", email);

        String userType = query.uniqueResult().toString();
        System.out.println(userType);

        return userType;


    }


    @FXML
    void btnLogInOnAction(ActionEvent event) {
        Stage stage = (Stage) loginForm.getScene().getWindow();


        if(confirmLogin()){
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Email or password is wrong..").show();
        }

    }


    @FXML
    void btnSignUpOnAction(ActionEvent event) {
        Stage stage = (Stage) loginForm.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/UserForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }


}
