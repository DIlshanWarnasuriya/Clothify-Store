package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;

import java.util.ArrayList;
import java.util.List;

public interface OrdersBo extends SuperBo {
    boolean save(Orders dto, ArrayList<OrdersDetails> list);
    List<Orders> getAllOrders();
}
