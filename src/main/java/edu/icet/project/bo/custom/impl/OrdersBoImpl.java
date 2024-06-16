package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.custom.OrdersBo;
import edu.icet.project.dao.DaoFactory;
import edu.icet.project.dao.custom.OrdersDao;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.entity.OrdersDetailsEntity;
import edu.icet.project.entity.OrdersEntity;
import edu.icet.project.util.DaoType;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class OrdersBoImpl implements OrdersBo {

    private final OrdersDao ordersDao = DaoFactory.getInstance().getDao(DaoType.ORDERS);

    @Override
    public boolean save(Orders dto, ArrayList<OrdersDetails> list) {
        OrdersEntity entity = new ModelMapper().map(dto, OrdersEntity.class);
        ArrayList<OrdersDetailsEntity> newList = new ArrayList<>();

        for (OrdersDetails orders : list){
            newList.add(new ModelMapper().map(orders, OrdersDetailsEntity.class));
        }
        return ordersDao.save(entity, newList);
    }

    @Override
    public List<Orders> getAllOrders() {
        List<Orders> list = new ArrayList<>();
        for (OrdersEntity allOrder : ordersDao.getAllOrders()) {
            list.add(new ModelMapper().map(allOrder, Orders.class));
        }
        return list;
    }
}
