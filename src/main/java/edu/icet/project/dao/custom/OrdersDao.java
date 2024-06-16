package edu.icet.project.dao.custom;

import edu.icet.project.dao.SuperDao;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.dto.Product;
import edu.icet.project.entity.OrdersDetailsEntity;
import edu.icet.project.entity.OrdersEntity;
import java.util.ArrayList;
import java.util.List;

public interface OrdersDao extends SuperDao {
    boolean save(OrdersEntity entity, ArrayList<OrdersDetailsEntity> list);
    List<OrdersEntity> getAllOrders();
    List<OrdersDetailsEntity> getAllOrdersDetails();
    boolean updateOrderProduct(OrdersDetailsEntity oProduct, Product product);

    boolean updateOrder(OrdersEntity entity, List<OrdersDetailsEntity> detailEntity, List<Product> productsList);
}
