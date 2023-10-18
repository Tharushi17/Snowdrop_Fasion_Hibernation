package Controller;

import Entity.*;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.ReturnTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SalesReturnController implements Initializable {

    public BorderPane returnForm;
    public JFXTextField txtItemCode;
    public JFXComboBox<String> cmbItemCode;
    public TreeTableColumn colReturnId;
    public TreeTableColumn colOrderId;
    public TreeTableColumn colDate;
    public TreeTableColumn colCostName;
    public TreeTableColumn colAmount;
    public TreeTableColumn colItemCode;
    public TreeTableColumn colDesc;
    public TreeTableColumn colUPrice;
    public TreeTableColumn colQty;

    @FXML
    private Label lblReturnId;

    @FXML
    private Label lblToday;

    @FXML
    private JFXTreeTableView<ReturnTm> tblReturn;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtCustName;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtDisRate;

    @FXML
    private JFXTextField txtOrderId;

    @FXML
    private JFXTextField txtOrderTotal;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtReturnQty;

    @FXML
    private JFXTextField txtUnitPrice;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colReturnId.setCellValueFactory(new TreeItemPropertyValueFactory<>("returnId"));
        colOrderId.setCellValueFactory(new TreeItemPropertyValueFactory<>("orderId"));
        colCostName.setCellValueFactory(new TreeItemPropertyValueFactory<>("custName"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colDate.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        colUPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("uPrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));


        loadTable();
        loadDate();
        generateReturnId();
        getOrders();
        getReturnAmount();


    }

    private void generateReturnId() {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM SalesReturn ORDER BY returnId DESC");
        query.setMaxResults(1);

        List<SalesReturn> returnList = query.list();

        if(!returnList.isEmpty()){
            SalesReturn lastReturn = returnList.get(0);
            String lastId = lastReturn.getReturnId();

            if(lastId != null && !lastId.isEmpty()){
                int num = Integer.parseInt(lastId.split(("_"))[1]);
                num++;
                lblReturnId.setText(String.format("Return_%04d",num));
            }
        }else{
            lblReturnId.setText("Return_0001");
        }
    }

    private void getOrders(){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Orders");
        List<Orders> list = query.list();


        txtOrderId.setOnAction(event ->{
            String orderId = txtOrderId.getText();

            if(txtOrderId.getText().matches("^Order_\\d{4}$")){

                Orders selectedOrder = list.stream()
                        .filter(order -> order.getOrderId().equals(orderId))
                        .findFirst()
                        .orElse(null);

                if(selectedOrder != null) {
                    txtDate.setText(selectedOrder.getDate());
                    txtCustName.setText(selectedOrder.getCustomerName());
                    txtOrderTotal.setText(selectedOrder.getTotal()+"");
                }

                List<String> itemCodes = populateItemCodes(orderId);

            }else{
                new Alert(Alert.AlertType.ERROR,"Unavailable Order ID. Please enter in format Order_****...").show();
            }
        });
    }


    private List<String> populateItemCodes(String orderId){
        cmbItemCode.getItems().clear();
        Session session = HibernateUtil.getSession();

        Query query = session.createQuery("SELECT od.item.itemCode FROM OrderDetails od WHERE od.orders.orderId = :orderId");
        query.setParameter("orderId", orderId);

        List<String> itemCodes = query.list();

        cmbItemCode.getItems().addAll(itemCodes);

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if(!cmbItemCode.getItems().isEmpty()){
                String selectedItemCode = cmbItemCode.getValue().toString();
                List<String> selectedItems = getItemCodesForSelectedItemId(selectedItemCode, orderId);
                itemCodes.addAll(selectedItems);

                findItemDetails(orderId, selectedItemCode);
            }
        });
        session.close();
        return  itemCodes;
    }

    private List<String> getItemCodesForSelectedItemId(String selectedItemCode, String orderId) {
        Session newSession = HibernateUtil.getSession();
        Query query2 = newSession.createQuery("SELECT od.item.itemCode FROM OrderDetails od WHERE od.item.itemCode = :itemCode AND od.orders.orderId = :orderId");
        query2.setParameter("itemCode", selectedItemCode);
        query2.setParameter("orderId", orderId);

        List<String> selectedItems = query2.list();
        newSession.close();

        return selectedItems;
    }


    private void findItemDetails(String orderId, String itemCode){
        //String itemCode = cmbItemCode.getValue().toString();

        Session newSession = HibernateUtil.getSession();
        Query query2 = newSession.createQuery("FROM OrderDetails od WHERE od.item.itemCode = :itemCode AND od.orders.orderId = :orderId");
        query2.setParameter("itemCode", itemCode);
        query2.setParameter("orderId", orderId);

        List<OrderDetails> list2 = query2.list();
        newSession.close();

        int qty = 0;

        for (OrderDetails details :list2) {
            txtDescription.setText(details.getItem().getDescription());
            txtUnitPrice.setText(details.getItem().getSellingPrice()+"");
            txtDisRate.setText(details.getDiscountRate()+"");
            qty += details.getOrderQty();
        }
        txtQty.setText(qty+"");
    }


    private  void setItemProfit(String orderId, String itemCode){
        Session newSession = HibernateUtil.getSession();
        Query query2 = newSession.createQuery("FROM OrderDetails od WHERE od.item.itemCode = :itemCode AND od.orders.orderId = :orderId");
        query2.setParameter("itemCode", itemCode);
        query2.setParameter("orderId", orderId);

        List<OrderDetails> list2 = query2.list();
        for (OrderDetails details :list2) {
            details.setItemProfit(Double.parseDouble(txtOrderTotal.getText())- Double.parseDouble(txtAmount.getText()));
        }
    }
    @FXML
    void btnPlaceReturn(ActionEvent event) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        if(txtAmount != null){
            String orderId = txtOrderId.getText();
            Orders orders = session.get(Orders.class, orderId);

            SalesReturn salesReturn = new SalesReturn(
                    lblReturnId.getText(),
                    lblToday.getText(),
                    Double.parseDouble(txtAmount.getText())
            );
            salesReturn.setOrders(orders);


            SalesReturnDetails returnDetails = new SalesReturnDetails(
                    Integer.parseInt(txtReturnQty.getText()),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Double.parseDouble(txtDisRate.getText()),
                    Double.parseDouble(txtAmount.getText())
            );
            returnDetails.setSalesReturn(salesReturn);

            String selectedItemCode = cmbItemCode.getValue().toString();

            if(selectedItemCode != null){
                Item item = session.get(Item.class, selectedItemCode);

                if(item != null){
                    List<Item> items = new ArrayList<>();
                    items.add(item);

                    for (Item itemUpdate :items) {
                        itemUpdate.setStock(Integer.parseInt(txtQty.getText())+Integer.parseInt(txtReturnQty.getText()));
                    }

                    setItemProfit(orderId, selectedItemCode);
                    System.out.println("items in items array"+items);
                    returnDetails.setItems(items);
                }
            }

            session.save(salesReturn);
            session.save(returnDetails);

            transaction.commit();
            session.close();
            new Alert(Alert.AlertType.INFORMATION,"Return Order is placed successfully..").show();
            clearFields();

        }else{
            new Alert(Alert.AlertType.ERROR,"Please press ENTER key to calculate the amount and continue..").show();
        }
    }

    private void loadTable(){
        ObservableList<ReturnTm> tblList = FXCollections.observableArrayList();

        Session session = HibernateUtil.getSession();
        Query<SalesReturnDetails> query = session.createQuery("From SalesReturnDetails");
        List<SalesReturnDetails> list = query.list();


        for (SalesReturnDetails sReturn :list) {
            if(sReturn.getSalesReturn() != null){

                String itemCodes = sReturn.getItems().stream()
                        .map(Item::getItemCode)
                        .collect(Collectors.joining(", "));

                String desc = getDescriptionForItemCode(itemCodes);

                ReturnTm returnTm = new ReturnTm(
                        sReturn.getSalesReturn().getReturnId(),
                        sReturn.getSalesReturn().getOrders().getOrderId(),
                        sReturn.getSalesReturn().getDate(),
                        sReturn.getSalesReturn().getOrders().getCustomerName(),
                        itemCodes,
                        desc,
                        sReturn.getUnitPrice(),
                        sReturn.getQty(),
                        sReturn.getAmount()
                );

                tblList.add(returnTm);
            }
        }
            TreeItem<ReturnTm> treeItem = new RecursiveTreeItem<>(tblList, RecursiveTreeObject::getChildren);
            tblReturn.setRoot(treeItem);
            tblReturn.setShowRoot(false);
    }

    private String getDescriptionForItemCode(String itemCode) {
        Session session = HibernateUtil.getSession();
        Query<Item> query = session.createQuery("FROM Item WHERE itemCode = :itemCode", Item.class);
        query.setParameter("itemCode", itemCode);
        Item item = query.uniqueResult();

        if (item != null) {
            return item.getDescription();
        } else {
            return "Description not found"; // Or handle this case as needed
        }
    }


    private void getReturnAmount(){
        txtReturnQty.setOnAction(event ->{
            int returnQty = Integer.parseInt(txtReturnQty.getText());
            double uPrice = Double.parseDouble(txtUnitPrice.getText());
            double disRate = Double.parseDouble(txtDisRate.getText());

            double returnAmount = uPrice*returnQty-(returnQty*uPrice*disRate/100);
            txtAmount.setText(returnAmount+"");
        });
    }


    @FXML
    void btnBack(ActionEvent event) {
        Stage stage = (Stage) returnForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    @FXML
    void btnCancel(ActionEvent event) {
        clearFields();
    }

    private void clearFields(){
        generateReturnId();
        txtOrderId.setText("Order_");
        txtDate.clear();
        txtCustName.clear();
        txtOrderTotal.clear();
        txtAmount.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtDisRate.clear();
        txtQty.clear();
        txtReturnQty.clear();
    }


    private void loadDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblToday.setText(f.format(date));
    }

}
