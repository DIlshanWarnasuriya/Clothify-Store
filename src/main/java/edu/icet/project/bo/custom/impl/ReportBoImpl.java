package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.*;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.dto.Product;
import edu.icet.project.util.BoType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportBoImpl implements ReportBo {
    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);
    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final OrdersBo ordersBo = BoFactory.getInstance().getBo(BoType.ORDERS);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);

    private Date date = new Date();
    String toDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
    String month = new SimpleDateFormat("yyyy-MM").format(date);
    String year = new SimpleDateFormat("yyyy").format(date);

    public Double todayIncome(){
        double income=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        income+=product.getTotal();
                    }
                }
            }
        }
        return income;
    }

    public Double monthIncome(){
        double income=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        income+=product.getTotal();
                    }
                }
            }
        }
        return income;
    }

    public Double annualIncome(){
        double income=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        income+=product.getTotal();
                    }
                }
            }
        }
        return income;
    }

    public int todayOrdersTotal(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay) && !orders.getStatus().equals("return")){
                count++;
            }
        }
        return count;
    }

    public int monthOrdersTotal(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month) && !orders.getStatus().equals("return")){
                count++;
            }
        }
        return count;
    }

    public int annualOrdersTotal(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year) && !orders.getStatus().equals("return")){
                count++;
            }
        }
        return count;
    }

    public int todaySoldProduct(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthSoldProduct(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualSoldProduct(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int todaySoldMen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthSoldMen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualSoldMen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int todaySoldWomen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthSoldWomen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualSoldWomen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int todaySoldKid(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthSoldKid(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualSoldKid(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (!product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }


    // return orders
    public int todayOrdersReturn(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay) && orders.getStatus().equals("return")){
                count++;
            }
        }
        return count;
    }

    public int monthOrdersReturn(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month) && orders.getStatus().equals("return")){
                count++;
            }
        }
        return count;
    }

    public int annualOrdersReturn(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year) && orders.getStatus().equals("return")){
                count++;
            }
        }
        return count;
    }

    // cancel orders
    public int todayOrdersChanged(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay) && orders.getStatus().equals("changed")){
                count++;
            }
        }
        return count;
    }

    public int monthOrdersChanged(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month) && orders.getStatus().equals("changed")){
                count++;
            }
        }
        return count;
    }

    public int annualOrdersChanged(){
        int count = 0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year) && orders.getStatus().equals("changed")){
                count++;
            }
        }
        return count;
    }


    // return men
    public int todayReturnMen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthReturnMen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualReturnMen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Men")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    // return women
    public int todayReturnWomen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthReturnWomen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualReturnWomen(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Women")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    // return kid
    public int todayReturnKid(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            if (orderDate.equals(toDay)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int monthReturnKid(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy-MM").format(orders.getDate());
            if (orderDate.equals(month)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    public int annualReturnKid(){
        int qty=0;
        for (Orders orders : ordersBo.getAllOrders()){
            String orderDate = new SimpleDateFormat("yyyy").format(orders.getDate());
            if (orderDate.equals(year)){
                for (OrdersDetails product : ordersBo.searchAllOrderProductByOrderId(orders.getId())) {
                    if (product.getStatus().equals("return") && productBo.searchById(product.getProductId()).getCategory().equals("Kid")){
                        qty+=product.getQty();
                    }
                }
            }
        }
        return qty;
    }

    // Purchased Product
    public int todayPurchasedProduct(){
        int count=0;
        for (Product product : productBo.getAllProduct()){
            String productDate = new SimpleDateFormat("yyyy-MM-dd").format(product.getDate());
            if (productDate.equals(toDay)){
                count++;
            }
        }
        return count;
    }

    public int monthPurchasedProduct(){
        int count=0;
        for (Product product : productBo.getAllProduct()){
            String productDate = new SimpleDateFormat("yyyy-MM").format(product.getDate());
            if (productDate.equals(month)){
                count++;
            }
        }
        return count;
    }

    public int annualPurchasedProduct(){
        int count=0;
        for (Product product : productBo.getAllProduct()){
            String productDate = new SimpleDateFormat("yyyy").format(product.getDate());
            if (productDate.equals(year)){
                count++;
            }
        }
        return count;
    }











}
