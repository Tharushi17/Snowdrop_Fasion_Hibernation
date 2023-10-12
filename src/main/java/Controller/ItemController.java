package Controller;
import Entity.Category;
import Entity.Item;
import Entity.Supplier;
import Entity.SupplierInvoice;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXComboBox;
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
import model.ItemTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;


public class ItemController implements Initializable {

    public JFXTextField txtCode;
    public Label lblDate;
    public BorderPane itemForm;
    public JFXComboBox<String> cmbSup;
    public JFXComboBox<String> cmbCategory;
    public JFXComboBox<String> cmbSize;
    public JFXTreeTableView<ItemTm> tblItem;
    public TreeTableColumn colCode;
    public TreeTableColumn colDesc;
    public TreeTableColumn colSupId;
    public TreeTableColumn colStock;
    public TreeTableColumn colBuying;
    public TreeTableColumn colSelling;
    public TreeTableColumn colCategory;
    public TreeTableColumn colType;
    public TreeTableColumn colProfit;


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
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colSupId.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierId"));
        colStock.setCellValueFactory(new TreeItemPropertyValueFactory<>("stock"));
        colBuying.setCellValueFactory(new TreeItemPropertyValueFactory<>("buyingPrice"));
        colSelling.setCellValueFactory(new TreeItemPropertyValueFactory<>("sellingPrice"));
        colCategory.setCellValueFactory(new TreeItemPropertyValueFactory<>("categoryId"));
        colType.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        colProfit.setCellValueFactory(new TreeItemPropertyValueFactory<>("profit"));


        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null){
                setData(newVal);
            }
        });

        generateCode();
        setSupplierId();
        setType();
        setCategoryId();
        loadTable();
        loadDate();


        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                tblItem.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> itemTmTreeItem) {
                        return itemTmTreeItem.getValue().getItemCode().contains(newVal)||
                                itemTmTreeItem.getValue().getDescription().contains(newVal);
                    }
                });
            }
        });

        txtSellingPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {

                if(!txtBuyingPrice.getText().isEmpty()){
                    profit();
                }
            }
        });

        txtNewQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                setStockLbl();
            }
        });
    }

    private void loadDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }


    private void setData(TreeItem<ItemTm> value) {
        txtCode.setText(value.getValue().getItemCode());
        txtDescription.setText(value.getValue().getDescription());
        cmbSup.setValue(value.getValue().getSupplierId());
        lblStock.setText(value.getValue().getStock()+"");
        txtBuyingPrice.setText(value.getValue().getBuyingPrice()+"");
        txtSellingPrice.setText(value.getValue().getSellingPrice()+"");
        cmbCategory.setValue(value.getValue().getCategoryId());
        cmbSize.setValue(value.getValue().getType());
        lblProfit.setText(value.getValue().getProfit()+"");

    }


    private void loadTable(){
        ObservableList<ItemTm> tblList = FXCollections.observableArrayList();

        Session session = HibernateUtil.getSession();
        Query<Item> query = session.createQuery("FROM Item");
        List<Item> list = query.list();

        for (Item item:list) {

            tblList.add(new ItemTm(
                    item.getItemCode(),
                    item.getDescription(),
                    item.getSupplier().getSupplierId(),
                    item.getStock(),
                    item.getBuyingPrice(),
                    item.getSellingPrice(),
                    item.getCategory().getCategoryId(),
                    item.getType(),
                    item.getProfit()

            ));


            TreeItem<ItemTm> treeItem =new RecursiveTreeItem<>(tblList, RecursiveTreeObject::getChildren);
            tblItem.setRoot(treeItem);
            tblItem.setShowRoot(false);
        }
    }


    @FXML
    void btnAddOnAction(ActionEvent event) {

        try{
            String selectedSupplierId = cmbSup.getValue();
            String selectedCategoryId = cmbCategory.getValue();

            Supplier selectedSupplier = getSupplierById(selectedSupplierId);
            Category selectedCategory = getCategoryById(selectedCategoryId);

            Item item = new Item(
                    txtCode.getText(),
                    txtDescription.getText(),
                    Integer.parseInt(lblStock.getText()),
                    Double.parseDouble(txtBuyingPrice.getText()),
                    Double.parseDouble(txtSellingPrice.getText()),
                    Double.parseDouble(lblProfit.getText()),
                    cmbSize.getValue()
            );


            SupplierInvoice invoice = new SupplierInvoice(
                    SupplierInvoiceController.generateId(),
                    lblDate.getText(),
                    Integer.parseInt(txtNewQty.getText())
            );
            invoice.setSupplier(selectedSupplier);
            invoice.setCategory(selectedCategory);


            item.setSupplier(selectedSupplier);
            item.setCategory(selectedCategory);

            Session session = HibernateUtil.getSession();

            session.beginTransaction();
            session.save(item);
            session.save(invoice);
            session.getTransaction().commit();
            session.close();

            loadTable();
            clearFields();

            new Alert(Alert.AlertType.INFORMATION,"Item Saved Successfully..").show();

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error saving item...").show();
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(Item.class, txtCode.getText()));
        transaction.commit();
        session.close();

        new Alert(Alert.AlertType.INFORMATION,"Item Deleted Successfully..").show();
        loadTable();
        clearFields();

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();

            Item item = session.get(Item.class, txtCode.getText());

            item.setItemCode(txtCode.getText());
            item.setDescription(txtDescription.getText());

            String supplierId = cmbSup.getValue();
            Supplier selectedSupplier = session.get(Supplier.class, supplierId);
            item.setSupplier(selectedSupplier);

            item.setStock(Integer.parseInt(lblStock.getText()));
            item.setBuyingPrice(Double.parseDouble(txtBuyingPrice.getText()));
            item.setSellingPrice(Double.parseDouble(txtSellingPrice.getText()));

            String categoryId = cmbCategory.getValue();
            Category selectedCategory = session.get(Category.class, categoryId);
            item.setCategory(selectedCategory);

            item.setType(cmbSize.getValue());
            item.setProfit(Double.parseDouble(lblProfit.getText()));


            session.update(item);
            transaction.commit();

            new Alert(Alert.AlertType.INFORMATION,"Item Updated Successfully..").show();
            loadTable();
            clearFields();

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error in Updating Iten...").show();

        }finally {
            session.close();
        }


    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }




//------------------ profit ------------------------------
    private void profit(){
        double buyingP = Double.parseDouble((txtBuyingPrice.getText()));
        double sellingP = Double.parseDouble((txtSellingPrice.getText()));
        double profit = sellingP-buyingP;
        lblProfit.setText(String.valueOf(profit));
    }


//-------------------- qty update --------------------------
    private void setStockLbl(){
        int currentStock = Integer.parseInt(lblStock.getText());
        int newQty = Integer.parseInt(txtNewQty.getText());
        int newStock = currentStock + newQty;
        lblStock.setText(newStock+"");
    }


//----------- set foreign key items in table -----------------
    private Supplier getSupplierById(String supplierId){
        Session session = HibernateUtil.getSession();

        try{
            Supplier supplier = session.get(Supplier.class, supplierId);
            return supplier;
        }finally {
            session.close();
        }
    }

    private Category getCategoryById(String categoryId){
        Session session = HibernateUtil.getSession();

        try{
            Category category = session.get(Category.class, categoryId);
            return category;
        }finally {
            session.close();
        }
    }


    private void clearFields() {
        generateCode();
        txtDescription.clear();
        txtSupName.clear();
        txtNewQty.clear();
        txtBuyingPrice.clear();
        txtSellingPrice.clear();
        txtSearch.clear();
        lblProfit.setText("0.0");
        lblStock.setText("0.0");
    }

//---------- generate Code --------------------------
    private void generateCode() {
        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("FROM Item ORDER BY itemCode DESC");
        query.setMaxResults(1);

        List<Item> itemList = query.list();

        if(!itemList.isEmpty()){
            Item lastItem = itemList.get(0);
            String lastCode = lastItem.getItemCode();

            if(lastCode != null && !lastCode.isEmpty()){
                int num = Integer.parseInt(lastCode.split(("_"))[1]);
                num++;
                txtCode.setText(String.format("ITEM_%04d", num));

            }
        }else{
            txtCode.setText("ITEM_0001");
        }
    }




//------------ combo box ------------------------------
    private void setType() {
        cmbSize.getItems().setAll("Adults", "Kids");
    }

    private void setCategoryId() {
        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("SELECT categoryId FROM Category");
        List<String> categories = query.list();

        cmbCategory.getItems().addAll(categories);
        session.close();

    }



    private void setSupplierId(){
        Session session = HibernateUtil.getSession();

        try{
            Query query = session.createQuery("SELECT supplierId, supplierName FROM Supplier");
            List<Object[]> suppliers= query.list();

            Map<String , String> supplierMap = new HashMap<>();

            for (Object[] supplier :suppliers) {
                String supplierId = (String) supplier[0];
                String supplierName = (String) supplier[1];
                supplierMap.put(supplierId, supplierName);

            }

            cmbSup.getItems().addAll(supplierMap.keySet());

            cmbSup.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
                if(newVal != null){
                    String selectedId = newVal;
                    String selectedName = supplierMap.get(selectedId);
                    txtSupName.setText(selectedName);
                }else{
                    txtSupName.clear();
                }
            });
        }finally {
            session.close();
        }

    }



//---------- interface controlling ---------------------
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


