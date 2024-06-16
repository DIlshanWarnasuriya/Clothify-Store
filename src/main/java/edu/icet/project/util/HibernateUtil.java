package edu.icet.project.util;

import edu.icet.project.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory session = createSession();

    private static SessionFactory createSession() {
        StandardServiceRegistry build = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metaData = new MetadataSources(build)
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(CustomerEntity.class)
                .addAnnotatedClass(SupplierEntity.class)
                .addAnnotatedClass(ProductEntity.class)
                .addAnnotatedClass(OrdersEntity.class)
                .addAnnotatedClass(OrdersDetailsEntity.class)

                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metaData.getSessionFactoryBuilder().build();
    }

    public static Session getSession(){
        return session.openSession();
    }
}
