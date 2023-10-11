package Controller;

import Entity.Item;
import Entity.SupplierInvoice;
import Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class SupplierInvoiceController {


    //---------- generate Code --------------------------
    public static String generateId() {
        Session session = HibernateUtil.getSession();
        String invoiceId = "Invoice_0001";

        Query query = session.createQuery("FROM SupplierInvoice ORDER BY invoiceId DESC");
        query.setMaxResults(1);

        List<SupplierInvoice> invoices = query.list();

        if(!invoices.isEmpty()){
            SupplierInvoice lastInvoice = invoices.get(0);
            String lastId = lastInvoice.getInvoiceId();

            if(lastId != null && !lastId.isEmpty()){
                int num = Integer.parseInt(lastId.split(("_"))[1]);
                num++;
                invoiceId = (String.format("Invoice_%04d", num));
            }
        }else{
            return("Invoice_0001");
        }
        return invoiceId;
    }
}
