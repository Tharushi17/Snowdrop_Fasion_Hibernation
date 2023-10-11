package Controller;

import Entity.Category;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.CategoryTm;
import model.SupplierTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class CategoryController implements Initializable {

    public JFXTreeTableView<CategoryTm> tblCategory;
    public TreeTableColumn colId;
    public TreeTableColumn colSize;
    public TreeTableColumn colGender;
    @FXML
    private BorderPane categoryForm;

    @FXML
    private Label lblCategoryId;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtSize;

    @FXML
    private JFXTextField tztGender;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("categoryId"));
        colSize.setCellValueFactory(new TreeItemPropertyValueFactory<>("gender"));
        colGender.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));


        tblCategory.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if(newValue != null){
                setData(newValue);
            }
        });

        generateId();
        loadTable();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                tblCategory.setPredicate(new Predicate<TreeItem<CategoryTm>>() {
                    @Override
                    public boolean test(TreeItem<CategoryTm> categoryTmTreeItem) {
                        return categoryTmTreeItem.getValue().getCategoryId().contains(newVal) ||
                                categoryTmTreeItem.getValue().getGender().contains(newVal);

                    }
                });
            }
        });
    }

    private void setData(TreeItem<CategoryTm> newValue) {
        lblCategoryId.setText(newValue.getValue().getCategoryId());
        txtSize.setText(newValue.getValue().getSize());
        tztGender.setText(newValue.getValue().getGender());
    }


    private void loadTable(){
        ObservableList<CategoryTm> tblList = FXCollections.observableArrayList();

        Session session = HibernateUtil.getSession();
        Query<Category> query = session.createQuery("FROM Category");
        List<Category> list = query.list();

        for (Category category:list) {

            tblList.add(new CategoryTm(
                    category.getCategoryId(),
                    category.getGender(),
                    category.getSize()
            ));
        }
        TreeItem<CategoryTm> treeItem = new RecursiveTreeItem<>(tblList, RecursiveTreeObject::getChildren);
        tblCategory.setRoot(treeItem);
        tblCategory.setShowRoot(false);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Category category = new Category(
                lblCategoryId.getText(),
                txtSize.getText(),
                tztGender.getText()
        );

        Session session = HibernateUtil.getSession();

        session.beginTransaction();
        session.save(category);
        session.getTransaction().commit();
        session.close();

        loadTable();
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        Optional<ButtonType> btnType = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this Category and ASSOCIATED DATA ?", ButtonType.YES, ButtonType.NO).showAndWait();
        if(btnType.get() == ButtonType.YES){
            session.delete(session.find(Category.class, lblCategoryId.getText()));
            transaction.commit();
            session.close();

            new Alert(Alert.AlertType.INFORMATION,"Category Deleted Successfully..").show();
            loadTable();
            clearFields();
        }else {
            new Alert(Alert.AlertType.INFORMATION,"Something Went Wrong..").show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();
        Category category = session.find(Category.class, lblCategoryId.getText());

        category.setCategoryId(lblCategoryId.getText());
        category.setSize(txtSize.getText());
        category.setGender(tztGender.getText());

        Transaction transaction = session.beginTransaction();
        session.save(category);
        transaction.commit();
        session.close();

        clearFields();
        loadTable();
    }



    private void clearFields() {
        generateId();
        txtSize.clear();
        tztGender.clear();
    }


    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) categoryForm.getScene().getWindow();

        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/ItemForm.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }


    private void generateId() {
        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("FROM Category ORDER BY categoryId DESC");
        query.setMaxResults(1);

        List<Category> categories = query.list();

        if(!categories.isEmpty()){
            Category lastCategory = categories.get(0);
            String lastId = lastCategory.getCategoryId();

            if(lastId != null && !lastId.isEmpty()){
                int num = Integer.parseInt(lastId.split(("_"))[1]);
                num++;
                lblCategoryId.setText(String.format("CAT_%04d",num));

            }
        }else{
            lblCategoryId.setText("CAT_0001");
        }
    }

}
