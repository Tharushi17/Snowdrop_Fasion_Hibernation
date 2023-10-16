package Util;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory = createSessionFactory();

    private static SessionFactory createSessionFactory() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("Snowdrop_Fashion.cfg.xml ")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Supplier.class)
                .addAnnotatedClass(SupplierInvoice.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Orders.class)
                .addAnnotatedClass(OrderDetails.class)
                .addAnnotatedClass(SalesReturn.class)
                .addAnnotatedClass(SalesReturnDetails.class)
                .addAnnotatedClass(Payment.class)


                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();
    }




    public static Session getSession(){
        return sessionFactory.openSession();
    }

}
