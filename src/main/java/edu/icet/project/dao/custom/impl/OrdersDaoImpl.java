package edu.icet.project.dao.custom.impl;

import edu.icet.project.dao.custom.OrdersDao;
import edu.icet.project.dto.Orders;
import edu.icet.project.entity.CustomerEntity;
import edu.icet.project.entity.OrdersDetailsEntity;
import edu.icet.project.entity.OrdersEntity;
import edu.icet.project.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class OrdersDaoImpl implements OrdersDao {
    @Override
    public boolean save(OrdersEntity entity, ArrayList<OrdersDetailsEntity> list) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.persist(entity);
            for (OrdersDetailsEntity en : list){
                session.persist(en);
            }
            // update qty code ----------------------------
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public List<OrdersEntity> getAllOrders() {
        Session session = HibernateUtil.getSession();
        List<OrdersEntity> list = session.createQuery("FROM OrdersEntity", OrdersEntity.class).list();
        session.close();
        return list;
    }
}
