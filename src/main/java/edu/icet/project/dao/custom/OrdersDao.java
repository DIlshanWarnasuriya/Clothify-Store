package edu.icet.project.dao.custom;

import edu.icet.project.dao.SuperDao;
import edu.icet.project.entity.OrdersDetailsEntity;
import edu.icet.project.entity.OrdersEntity;
import java.util.ArrayList;
import java.util.List;

public interface OrdersDao extends SuperDao {
    boolean save(OrdersEntity entity, ArrayList<OrdersDetailsEntity> list);
    List<OrdersEntity> getAllOrders();
    boolean update(OrdersEntity entity, List<OrdersDetailsEntity> detailEntity);

    List<OrdersDetailsEntity> getAllOrdersDetails();


}
