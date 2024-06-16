package edu.icet.project.dao.custom.impl;

import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.ProductBo;
import edu.icet.project.dao.custom.OrdersDao;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.Product;
import edu.icet.project.entity.CustomerEntity;
import edu.icet.project.entity.OrdersDetailsEntity;
import edu.icet.project.entity.OrdersEntity;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import edu.icet.project.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class OrdersDaoImpl implements OrdersDao {

    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);

    @Override
    public boolean save(OrdersEntity entity, ArrayList<OrdersDetailsEntity> list) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try{

            // save Order
            session.persist(entity);

            // save order product to order details
            for (OrdersDetailsEntity en : list){
                session.persist(en);
            }

            // update qty of product
            for (OrdersDetailsEntity en : list){
                Product product = productBo.searchById(en.getProductId());
                int qty = product.getQty() - en.getQty();
                product.setQty(qty);

                productBo.updateProduct(product);
            }

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

    @Override
    public List<OrdersDetailsEntity> getAllOrdersDetails() {
        Session session = HibernateUtil.getSession();
        List<OrdersDetailsEntity> list = session.createQuery("FROM OrdersDetailsEntity", OrdersDetailsEntity.class).list();
        session.close();
        return list;
    }

    @Override
    public boolean updateOrderProduct(OrdersDetailsEntity entity, Product product) {

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.update(entity);
            productBo.updateProduct(product);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateOrder(OrdersEntity entity, List<OrdersDetailsEntity> detailEntity, List<Product> productsList) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.update(entity);

            for (OrdersDetailsEntity items : detailEntity){
                session.update(items);
            }

            for (Product product : productsList){
                productBo.updateProduct(product);
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }
}
