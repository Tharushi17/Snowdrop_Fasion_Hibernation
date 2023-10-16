package Controller;

import Entity.*;
import Util.HibernateUtil;
import com.jfoenix.controls.*;
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
import model.CartTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.rgb;

public class PlaceOrderController implements Initializable {

    public Label lblEmpName;
    public BorderPane placeOrderForm;
    public JFXTextField txtCash;
    public TreeTableColumn<?, ?> colDiscount;
    @FXML
    private JFXComboBox<String > cmbEmpId;

    @FXML
    private JFXComboBox<String> cmbItemCode;

    @FXML
    private TreeTableColumn<?, ?> colCustName;

    @FXML
    private TreeTableColumn<?, ?> colEmpId;

    @FXML
    private TreeTableColumn<?, ?> colItemCode;

    @FXML
    private TreeTableColumn<?, ?> colOption;

    @FXML
    private TreeTableColumn<?, ?> colOrderId;

    @FXML
    private TreeTableColumn<?, ?> colQty;

    @FXML
    private TreeTableColumn<?, ?> colSellingP;

    @FXML
    private TreeTableColumn<?, ?> colTotal;

    @FXML
    private TreeTableColumn<?, ?> colUnitP;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblId;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTotalDis;

    @FXML
    private Label llblStock;

    @FXML
    private JFXTreeTableView<CartTm> tblCart;

    @FXML
    private JFXTextField txtCustContact;

    @FXML
    private JFXTextField txtCustEmail;

    @FXML
    private JFXTextField txtCustName;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private JFXTextField txtDisRate;

    @FXML
    private JFXTextField txtQty;


    @FXML
    private JFXTextField txtSellingP;

    @FXML
    private JFXTextField txtUnitPrice;
    ObservableList<CartTm> tblList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colOrderId.setCellValueFactory(new TreeItemPropertyValueFactory<>("orderId"));
        colEmpId.setCellValueFactory(new TreeItemPropertyValueFactory<>("empId"));
        colCustName.setCellValueFactory(new TreeItemPropertyValueFactory<>("customerName"));
        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colUnitP.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colSellingP.setCellValueFactory(new TreeItemPropertyValueFactory<>("sellingPrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new TreeItemPropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new TreeItemPropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        tblCart.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if(newVal != null){
                setData(newVal);
            }
        });

        generateId();
        setEmpIdToCmb();
        setItemToCmb();
        setDate();


    //-------------------- set discount rate and selling price ----------

        txtDisRate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                double unitPrice = Double.parseDouble(txtUnitPrice.getText());
                double disRate = Double.parseDouble(txtDisRate.getText());

                if(t1 != null){
                    double sellingP = unitPrice-(unitPrice*disRate/100);
                    txtSellingP.setText(sellingP+"");
                }
            }
        });


        txtCash.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                double total = Double.parseDouble(lblTotal.getText());
                double cash = Double.parseDouble(txtCash.getText());

                if(t1 != null){
                    double balance = total-cash;
                    lblBalance.setText(balance+"");
                }
            }
        });


    //-------------------------- set stock --------------------------

//        txtQty.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//
//                if(!llblStock.getText().isEmpty() && !txtQty.getText().isEmpty()){
//                    int stock = Integer.parseInt(llblStock.getText());
//                    int qty = Integer.parseInt(txtQty.getText());
//
//                    llblStock.setText((stock-qty)+"");
//
//                    if(stock-qty <0){
//                        new Alert(Alert.AlertType.ERROR,"Insufficient Stock...").show();
//                    }
//                }
//            }
//        });

    }


    private void setDate(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }

    private void setData(TreeItem<CartTm> value){
        lblId.setText(value.getValue().getOrderId());
        cmbEmpId.setValue(value.getValue().getEmpId());
        txtCustName.setText(value.getValue().getCustomerName());
        cmbItemCode.setValue(value.getValue().getItemCode());
        txtUnitPrice.setText(value.getValue().getUnitPrice()+"");
        txtSellingP.setText(value.getValue().getSellingPrice()+"");
        txtQty.setText(value.getValue().getQty()+"");
        txtDisRate.setText(value.getValue().getDisRate()+"");
    }

    private double findTotal(){
        double total = 0.0;
        for (CartTm tm :tblList) {
            total += tm.getTotal();
        }
        return total;
    }

    private double findTotalDiscount(){
        double discount = 0.0;
        for (CartTm tm :tblList) {
            discount += tm.getDiscount();
        }
        return discount;
    }


//-------------------- Add to cart -----------------------
    @FXML
    void btnAddToCart(ActionEvent event) {
        boolean isExist = false;

        int qty = Integer.parseInt(txtQty.getText());
        double unitP = Double.parseDouble(txtUnitPrice.getText());
        double sellingP = Double.parseDouble(txtSellingP.getText());
        double disRate = Double.parseDouble(txtDisRate.getText());


        int totalQty = 0;

        if(emailFormat()){
            if(validContact()){

                for (CartTm tm:tblList) {
                    if(tm.getItemCode().equals(cmbItemCode.getValue().toString())){

                        totalQty += tm.getQty();
                        tm.setQty(tm.getQty()+qty);
                        tm.setTotal((tm.getQty()*sellingP));
                        tm.setDiscount((unitP*tm.getQty())*disRate/100);
                        isExist = true;
                    }
                }

                if (totalQty+qty > Integer.parseInt(llblStock.getText())){
                    new Alert(Alert.AlertType.ERROR,"Insufficient Stock...").show();
                    clearItemDetails();
                    return;
                }


                if(!isExist && !txtQty.getText().isEmpty()){
                    JFXButton btn = new JFXButton("Delete");
                    btn.setTextFill(rgb(4,4,4));
                    btn.setStyle("-fx-font-weight: BOLD");

                    CartTm cartTm = new CartTm(
                            lblId.getText(),
                            cmbEmpId.getValue(),
                            txtCustName.getText(),
                            cmbItemCode.getValue(),
                            unitP,
                            sellingP,
                            qty,
                            (unitP*qty)*disRate/100,
                            disRate,
                            Double.parseDouble(txtSellingP.getText())*Integer.parseInt(txtQty.getText()),
                            btn
                    );


                    btn.setOnAction(actionEvent -> {
                        tblList.remove(cartTm);
                        lblTotal.setText(String.format("%.2f", findTotal()));
                        lblTotalDis.setText(String.format("%.2f", findTotalDiscount()));
                        updateStockOnItemRemoval(cmbItemCode.getValue(), qty);
                        tblCart.refresh();
                    });

                    tblList.add(cartTm);

                    TreeItem<CartTm> treeItem = new RecursiveTreeItem<>(tblList, RecursiveTreeObject::getChildren);
                    tblCart.setRoot(treeItem);
                    tblCart.setShowRoot(false);
                }

            }else{
                new Alert(Alert.AlertType.ERROR,"Please Enter a valid Contact Number...").show();
            }
        }else{
             new Alert(Alert.AlertType.ERROR,"Please Enter a valid Email...").show();
        }


        lblTotal.setText(String.format("%.2f", findTotal()));
        lblTotalDis.setText(String.format("%.2f", findTotalDiscount()));

        updateStock();
        tblCart.refresh();

    }



    @FXML
    void btnPlaceOrder(ActionEvent event) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();

            String selectedEmpId = cmbEmpId.getValue();
            Employee selectedEmp = getEmployeeId(selectedEmpId);

            String selectedItemCode = cmbItemCode.getValue();
            Item selectedItem = getItemCode(selectedItemCode);

            Orders order = new Orders(
                    lblId.getText(),
                    lblDate.getText(),
                    Double.parseDouble(lblTotalDis.getText()),
                    txtCustContact.getText(),
                    Double.parseDouble(lblTotal.getText()),
                    txtCustName.getText(),
                    txtCustEmail.getText(),
                    Double.parseDouble(lblBalance.getText())
            );

            order.setEmployee(selectedEmp);


            List<OrderDetails> detailsList = new ArrayList<>();

            for (CartTm tm : tblList) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrders(order);
                orderDetails.setItem(selectedItem);
                orderDetails.setOrderQty(tm.getQty());

                double itemProfit = selectedItem.getProfit();
                int orderQty = tm.getQty();
                double disRate = tm.getDisRate();
                double totalProfit = (itemProfit*orderQty)-(tm.getSellingPrice()*orderQty*disRate/100);

                orderDetails.setDiscountRate(disRate);
                orderDetails.setItemProfit(totalProfit);
                detailsList.add(orderDetails);
            }


            if(txtCash.getText().isEmpty()){
                new Alert(Alert.AlertType.ERROR,"Enter the cash before place order...").show();
            }else{

                Payment payment = new Payment(
                        generatePymentId(),
                        lblDate.getText(),
                        Double.parseDouble(lblTotal.getText()),
                        Double.parseDouble(txtCash.getText()),
                        Double.parseDouble(lblBalance.getText())
                );
                payment.setOrder(order);


                session.save(order);
                session.save(payment);

                for (OrderDetails detail : detailsList) {
                    session.save(detail);
                }

                transaction.commit();
                new Alert(Alert.AlertType.INFORMATION,"Order is placed Successfully...").show();
                clearFields();
                tblCart.refresh();

                //print bill???????????????
            }


        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
                new Alert(Alert.AlertType.ERROR,"Something went wrong...").show();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }



    private boolean emailFormat(){
        String emailAddress = txtCustEmail.getText();
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(emailAddress);

        return matcher.matches();
    }

    boolean validContact(){
        if(txtCustContact.getText().length() != 10 || txtCustContact.getText().charAt(0) != '0'){
            return false;
        }
        return true;
    }


//----------- set foreign key employee id in table -----------------
    private Employee getEmployeeId(String empId){
        Session session = HibernateUtil.getSession();

        try{
            Employee employee = session.get(Employee.class, empId);
            return employee;
        }finally {
            session.close();
        }
    }

    private Item getItemCode(String itemCode){
        Session session = HibernateUtil.getSession();

        try{
            Item item = session.get(Item.class, itemCode);
            return item;
        }finally {
            session.close();
        }
    }


//------------------- generate id ------------------------------
    private void generateId(){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Orders ORDER BY orderId DESC");
        query.setMaxResults(1);

        List<Orders> ordersList = query.list();

        if(!ordersList.isEmpty()){
            Orders lastOrder = ordersList.get(0);
            String lastId = lastOrder.getOrderId();

            if(lastId != null && !lastId.isEmpty()){
                int num = Integer.parseInt(lastId.split(("_"))[1]);
                num++;
                lblId.setText(String.format("Order_%04d",num));
            }
        }else{
            lblId.setText("Order_0001");
        }
    }


    private String generatePymentId(){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Payment ORDER BY paymentId DESC");
        query.setMaxResults(1);

        List<Payment> list = query.list();

        if(!list.isEmpty()){
            Payment lastPay = list.get(0);
            String lastId = lastPay.getPaymentId();

            if(!list.isEmpty()){


                if(lastId != null && !lastId.isEmpty()) {
                    int num = Integer.parseInt(lastId.split(("_"))[1]);
                    num++;
                    return (String.format("Pay_%04d", num));
                }
            }
        }
        return ("Pay_0001");
    }


//---------------- clear fields ------------------------
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearItemDetails();
    }

    private void clearItemDetails(){
        txtDesc.clear();
        txtQty.clear();
        llblStock.setText("0");
        txtUnitPrice.clear();
        txtSellingP.clear();
        txtDisRate.clear();
    }
    private void clearFields(){

        generateId();
        lblEmpName.setText("");
        txtCustName.clear();
        txtCustContact.clear();
        txtCustEmail.clear();
        txtDesc.clear();
        txtQty.clear();
        llblStock.setText("0");
        txtUnitPrice.clear();
        txtSellingP.clear();
        txtDisRate.clear();
        lblTotalDis.setText("0.0");
        lblTotal.setText("0.0");
        lblBalance.setText("0.0");
        txtCash.clear();
    }


//----------- update stock ----------------------
    private void updateStock(){
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        String itemCode = cmbItemCode.getValue();
        int qty = Integer.parseInt(txtQty.getText());

        try{
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, itemCode);

            int currentStock = item.getStock();
            item.setStock(currentStock-qty);

            session.update(item);
            transaction.commit();
        }catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    private void updateStockOnItemRemoval(String itemCode, int qty){
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, itemCode);

            int currentStock = item.getStock();
            item.setStock(currentStock+qty);

            session.update(item);
            transaction.commit();

        }catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }
    }


//---------------- combo box --------------------------
    private void setEmpIdToCmb() {
        Session session = HibernateUtil.getSession();
        try{
            Query query = session.createQuery("SELECT empId, name FROM Employee");
            List<Object[]> employees = query.list();

            Map<String, String> employeeMap = new HashMap<>();

            for (Object[] employee:employees) {
                String empId = (String) employee[0];
                String empName = (String) employee[1];
                employeeMap.put(empId, empName);
            }

            cmbEmpId.getItems().addAll(employeeMap.keySet());

            cmbEmpId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
                if(newVal != null){
                    String selectedId = newVal;
                    String selectedName = employeeMap.get(selectedId);
                    lblEmpName.setText(selectedName);
                }else{
                    lblEmpName.setText("employee name");
                }
            });
        }finally {
            session.close();
        }
    }


    private void setItemToCmb(){
        Session session = HibernateUtil.getSession();

        try {
            Query query = session.createQuery("SELECT itemCode, description, sellingPrice, stock FROM Item ");
            List<Object[]> items = query.list();

            Map<String, Item> itemMap = new HashMap<>();

            for (Object[] item: items) {
                String itemCode = (String) item[0];
                String desc = (String) item[1];
                double sellingP = (double) item[2];
                int stock = (int) item[3];

                Item itemObj = new Item(itemCode, desc, sellingP, stock);
                itemMap.put(itemCode, itemObj);
            }

            cmbItemCode.getItems().addAll(itemMap.keySet());

            cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
                if(newVal != null){
                    String selectedCode = newVal;
                    Item selectedItem = itemMap.get(selectedCode);

                    txtDesc.setText(selectedItem.getDescription());
                    llblStock.setText(selectedItem.getStock()+"");
                    txtUnitPrice.setText(selectedItem.getSellingPrice()+"");
                }else{
                    txtUnitPrice.setText("");
                    txtDesc.setText("");
                    llblStock.setText("0");
                }
            });

        }finally {
            session.close();
        }
    }



    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) placeOrderForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

}
