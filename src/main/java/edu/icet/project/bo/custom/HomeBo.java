package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.OrdersDetails;

import java.util.List;

public interface HomeBo extends SuperBo {
    int getUserCount();
    int getCustomersCount();
    int getProductCount();
    int getSupplierCount();
    int getTodayOrderProductCount();
    double getIncome();
    List<OrdersDetails> bestSellingProduct();
    int[] getMonthSale();
    int[] getYearSale();

    List<OrdersDetails> bestSellingMenProduct();
    List<OrdersDetails> bestSellingWomenProduct();
    List<OrdersDetails> bestSellingKidProduct();
}
