package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import java.util.ArrayList;
import java.util.List;

public interface OrdersBo extends SuperBo {
    boolean saveOrder(Orders dto, ArrayList<OrdersDetails> list);
    List<Orders> getAllOrders();
    Orders searchOrderById(Integer id);
    List<Orders> searchOrder(String date);
    boolean deleteOrder(Orders order, List<OrdersDetails> ordersDetailsList);

    List<OrdersDetails> searchAllOrderProductByOrderId(Integer id);
    OrdersDetails searchOrderProductById(Integer id);
    List<OrdersDetails> getAllOrderProduct();
}
