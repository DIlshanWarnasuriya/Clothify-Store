package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.*;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.util.BoType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeBoImpl implements HomeBo {

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final OrdersBo ordersBo = BoFactory.getInstance().getBo(BoType.ORDERS);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);

    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public int getUserCount(){
        return userBo.getAll().size();
    }

    public int getCustomersCount(){
        return customerBo.getAll().size();
    }

    public int getProductCount(){
        return productBo.getAllProduct().size();
    }

    public int getSupplierCount(){
        return supplierBo.getAll().size();
    }


    public int getTodayOrderProductCount(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(date)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        qty += product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public double getIncome(){
        double income=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(date)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        income+=product.getTotal();
                    }
                }
            }
        }
        return income;
    }

    public List<OrdersDetails> bestSellingProduct(){
        List<OrdersDetails> list = new ArrayList<>();

        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(date)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        int index = isHaveProduct(list, product);
                        if (index<0){
                            list.add(product);
                        }
                        else{
                            list.get(index).setQty(list.get(index).getQty() + product.getQty());
                        }
                    }
                }
            }
        }

        for (int j=list.size()-1; j>0; j--){
            for (int i=0; i<j; i++){
                if (list.get(i).getQty() > list.get(i+1).getQty()){
                    OrdersDetails t = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, t);
                }
            }
        }
        return list;
    }
    private int isHaveProduct(List<OrdersDetails> list, OrdersDetails product){
        int count = 0;
        for (OrdersDetails pro : list){
            if (Objects.equals(pro.getProductId(), product.getProductId())){
                return count;
            }
            count++;
        }
        return -1;
    }

    public int[] getMonthSale(){
        int nowYer = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

        int[] monthQty = new int[12];
        for (int i=1; i<13; i++){
            for (Orders orders : ordersBo.getAllOrders()){
                int orderMonth = Integer.parseInt(new SimpleDateFormat("MM").format(orders.getDate()));
                int orderYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(orders.getDate()));

                if (orderMonth == i && orderYear == nowYer){
                    for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                        if (!product.getStatus().equals("return")){
                            monthQty[i] += product.getQty();
                        }
                    }
                }
            }
        }
        return monthQty;
    }

    public int[] getYearSale(){
        int nowYer = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

        int[] yearQty = new int[3];
        for (int i=2; i>=0; i--){
            for (Orders orders : ordersBo.getAllOrders()){
                int orderYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(orders.getDate()));
                if (orderYear == (nowYer-i)){
                    for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                        if (!product.getStatus().equals("return")){
                            yearQty[i] += product.getQty();
                        }
                    }
                }
            }
        }
        return yearQty;
    }


    public List<OrdersDetails> bestSellingMenProduct(){
        List<OrdersDetails> list = new ArrayList<>();

        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(date)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        int index = isHaveProduct(list, product);
                        if (index<0){
                            list.add(product);
                        }
                        else{
                            list.get(index).setQty(list.get(index).getQty() + product.getQty());
                        }
                    }
                }
            }
        }

        for (int j=list.size()-1; j>0; j--){
            for (int i=0; i<j; i++){
                if (list.get(i).getQty() > list.get(i+1).getQty()){
                    OrdersDetails t = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, t);
                }
            }
        }
        return list;
    }

    public List<OrdersDetails> bestSellingWomenProduct(){
        List<OrdersDetails> list = new ArrayList<>();

        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(date)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        int index = isHaveProduct(list, product);
                        if (index<0){
                            list.add(product);
                        }
                        else{
                            list.get(index).setQty(list.get(index).getQty() + product.getQty());
                        }
                    }
                }
            }
        }

        for (int j=list.size()-1; j>0; j--){
            for (int i=0; i<j; i++){
                if (list.get(i).getQty() > list.get(i+1).getQty()){
                    OrdersDetails t = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, t);
                }
            }
        }
        return list;
    }

    public List<OrdersDetails> bestSellingKidProduct(){
        List<OrdersDetails> list = new ArrayList<>();

        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(date)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        int index = isHaveProduct(list, product);
                        if (index<0){
                            list.add(product);
                        }
                        else{
                            list.get(index).setQty(list.get(index).getQty() + product.getQty());
                        }
                    }
                }
            }
        }

        for (int j=list.size()-1; j>0; j--){
            for (int i=0; i<j; i++){
                if (list.get(i).getQty() > list.get(i+1).getQty()){
                    OrdersDetails t = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, t);
                }
            }
        }
        return list;
    }

}
