package Controller;

import Entity.OrderDetails;
import Entity.Orders;
import Util.HibernateUtil;
import com.jfoenix.controls.JFXButton;
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
import model.OrderDetailsTm;
import model.OrdersTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static javafx.scene.paint.Color.rgb;

public class OrderDetailsController implements Initializable {

    public JFXTreeTableView<OrderDetailsTm> tblDetails;
    @FXML
    private TreeTableColumn<?, ?> colArrears;

    @FXML
    private TreeTableColumn<?, ?> colBtn;

    @FXML
    private TreeTableColumn<?, ?> colContact;

    @FXML
    private TreeTableColumn<?, ?> colDate;


    @FXML
    private TreeTableColumn<?, ?> colDisRate;

    @FXML
    private TreeTableColumn<?, ?> colDiscount;

    @FXML
    private TreeTableColumn<?, ?> colEmail;

    @FXML
    private TreeTableColumn<?, ?> colEmpId;

    @FXML
    private TreeTableColumn<?, ?> colItemCode;

    @FXML
    private TreeTableColumn<?, ?> colName;

    @FXML
    private TreeTableColumn<?, ?> colOrder;

    @FXML
    private TreeTableColumn<?, ?> colOrderId2;

    @FXML
    private TreeTableColumn<?, ?> colQty;

    @FXML
    private TreeTableColumn<?, ?> colTotal;

    @FXML
    private TreeTableColumn<?, ?> colTotalProfit;

    @FXML
    private Label lblDate;

    @FXML
    private BorderPane orderDetailsForm;

    @FXML
    private JFXTreeTableView<OrdersTm> tblOrder;

    @FXML
    private JFXTextField txtSearch;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrder.setCellValueFactory(new TreeItemPropertyValueFactory<>("orderId"));
        colEmpId.setCellValueFactory(new TreeItemPropertyValueFactory<>("empId"));
        colDate.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("custName"));
        colContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("custContact"));
        colEmail.setCellValueFactory(new TreeItemPropertyValueFactory<>("custEmail"));
        colDiscount.setCellValueFactory(new TreeItemPropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new TreeItemPropertyValueFactory<>("total"));
        colArrears.setCellValueFactory(new TreeItemPropertyValueFactory<>("arrears"));
        colBtn.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        colOrderId2.setCellValueFactory(new TreeItemPropertyValueFactory<>("orderId"));
        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colTotalProfit.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalProfit"));
        colDisRate.setCellValueFactory(new TreeItemPropertyValueFactory<>("disRate"));

        loadOrdersToTable();
        setDate();


        tblOrder.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if(newVal != null){
                loadOrderDetails((TreeItem<OrdersTm>) newVal);
            }
        });


        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                tblOrder.setPredicate(new Predicate<TreeItem<OrdersTm>>() {
                    @Override
                    public boolean test(TreeItem<OrdersTm> ordersTmTreeItem) {
                        return ordersTmTreeItem.getValue().getOrderId().contains(newVal) ||
                                ordersTmTreeItem.getValue().getCustName().contains(newVal);
                    }
                });
            }
        });
    }


    private void setDate(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }

    private void loadOrdersToTable(){
        ObservableList<OrdersTm> tmList = FXCollections.observableArrayList();

        Session session = HibernateUtil.getSession();
        Query<Orders> query = session.createQuery("FROM Orders");
        List<Orders> list = query.list();


        for (Orders order :list) {

            JFXButton btn = new JFXButton("Delete");
            btn.setTextFill(rgb(4,4,4));
            btn.setStyle("-fx-font-weight: BOLD");

            OrdersTm ordersTm = new OrdersTm(
                    order.getOrderId(),
                    order.getEmployee().getEmpId(),
                    order.getDate(),
                    order.getCustomerName(),
                    order.getCustomerContact(),
                    order.getCustomerEmail(),
                    order.getTotalDiscount(),
                    order.getTotal(),
                    order.getArrears(),
                    btn
            );

            btn.setOnAction(actionEvent -> {
                Optional<ButtonType> btnType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Delete this order and its Details ?", ButtonType.YES, ButtonType.NO).showAndWait();

                if(btnType.get() == ButtonType.YES){
                    boolean orderDeleted = deleteOrder(order, order.getOrderId());

                    if(orderDeleted){
                        tmList.remove(ordersTm);
                        tblOrder.refresh();

                        tblDetails.refresh();
                    }
                }
            });

            tmList.add(ordersTm);

            TreeItem<OrdersTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            tblOrder.setRoot(treeItem);
            tblOrder.setShowRoot(false);
        }

    }


    private void loadOrderDetails(TreeItem<OrdersTm> value){
        if(value != null){
            OrdersTm selectedOrder = value.getValue();
            String selectedId = selectedOrder.getOrderId();

            ObservableList<OrderDetailsTm> tmList = FXCollections.observableArrayList();

            Session session = HibernateUtil.getSession();
            Query<OrderDetails> query = session.createQuery("FROM OrderDetails WHERE orderId = :orderId");
            query.setParameter("orderId", selectedId);
            List<OrderDetails> list = query.list();


            for(OrderDetails orderDetails : list){
                OrderDetailsTm detailsTm = new OrderDetailsTm(
                        orderDetails.getOrders().getOrderId(),
                        orderDetails.getItem().getItemCode(),
                        orderDetails.getOrderQty(),
                        orderDetails.getItemProfit(),
                        orderDetails.getDiscountRate()
                );

                tmList.add(detailsTm);

                TreeItem<OrderDetailsTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
                tblDetails.setRoot(treeItem);
                tblDetails.setShowRoot(false);
            }
        }
    }


    private boolean deleteOrder(Orders orders, String orderId){
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.delete(session.find(Orders.class, orderId));
            transaction.commit();

            return true;

        }catch (Exception e){

            if(transaction != null){
                transaction.rollback();
            }

            e.printStackTrace();
            return false;

        }finally {
            session.close();
        }
    }
    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) orderDetailsForm.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../View/DashBoard.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

}
