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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersBoImpl implements OrdersBo {

    private final OrdersDao ordersDao = DaoFactory.getInstance().getDao(DaoType.ORDERS);

    @Override
    public boolean saveOrder(Orders dto, ArrayList<OrdersDetails> list) {
        OrdersEntity entity = new ModelMapper().map(dto, OrdersEntity.class);
        ArrayList<OrdersDetailsEntity> newList = new ArrayList<>();

        for (OrdersDetails orders : list) {
            newList.add(new ModelMapper().map(orders, OrdersDetailsEntity.class));
        }
        return ordersDao.save(entity, newList);
    }

    @Override
    public boolean deleteOrder(Orders order, List<OrdersDetails> ordersDetailsList) {
        OrdersEntity entity = new ModelMapper().map(order, OrdersEntity.class);

        List<OrdersDetailsEntity> detailEntity = new ArrayList<>();
        for (OrdersDetails product : ordersDetailsList) {
            detailEntity.add(new ModelMapper().map(product, OrdersDetailsEntity.class));
        }
        return ordersDao.update(entity, detailEntity);
    }


    @Override
    public List<Orders> getAllOrders() {
        List<Orders> list = new ArrayList<>();
        for (OrdersEntity allOrder : ordersDao.getAllOrders()) {
            list.add(new ModelMapper().map(allOrder, Orders.class));
        }
        return list;
    }

    @Override
    public List<OrdersDetails> getAllOrderProduct() {
        List<OrdersDetails> list = new ArrayList<>();
        for (OrdersDetailsEntity allOrder : ordersDao.getAllOrdersDetails()) {
            list.add(new ModelMapper().map(allOrder, OrdersDetails.class));
        }
        return list;
    }


    @Override
    public List<Orders> searchOrder(String data) {
        List<Orders> list = new ArrayList<>();
        for (OrdersEntity orders : ordersDao.getAllOrders()) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orders.getId().toString().equals(data) || date.equals(data) || orders.getPaymentMethod().equals(data) || orders.getStatus().equals(data)) {
                list.add(new ModelMapper().map(orders, Orders.class));
            }
        }
        return list;
    }

    @Override
    public Orders searchOrderById(Integer id) {
        for (OrdersEntity orders : ordersDao.getAllOrders()) {
            if (Objects.equals(orders.getId(), id)) {
                return new ModelMapper().map(orders, Orders.class);
            }
        }
        return null;
    }

    @Override
    public List<OrdersDetails> searchAllOrderProductByOrderId(Integer id) {
        List<OrdersDetails> list = new ArrayList<>();

        for (OrdersDetailsEntity items : ordersDao.getAllOrdersDetails()) {
            if (Objects.equals(items.getOrderId(), id)) {
                list.add(new ModelMapper().map(items, OrdersDetails.class));
            }
        }
        return list;
    }

    @Override
    public OrdersDetails searchOrderProductById(Integer id) {
        for (OrdersDetailsEntity items : ordersDao.getAllOrdersDetails()) {
            if (Objects.equals(items.getId(), id)) {
                return new ModelMapper().map(items, OrdersDetails.class);
            }
        }
        return null;
    }
}
