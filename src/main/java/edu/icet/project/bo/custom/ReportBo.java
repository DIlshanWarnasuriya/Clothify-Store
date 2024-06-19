package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;

public interface ReportBo extends SuperBo {
    Double todayIncome();
    Double monthIncome();
    Double annualIncome();

    int todayOrdersTotal();
    int monthOrdersTotal();
    int annualOrdersTotal();

    int todaySoldProduct();
    int monthSoldProduct();
    int annualSoldProduct();

    int todaySoldMen();
    int monthSoldMen();
    int annualSoldMen();

    int todaySoldWomen();
    int monthSoldWomen();
    int annualSoldWomen();

    int todaySoldKid();
    int monthSoldKid();
    int annualSoldKid();

    int todayOrdersReturn();
    int monthOrdersReturn();
    int annualOrdersReturn();

    int todayOrdersChanged();
    int monthOrdersChanged();
    int annualOrdersChanged();

    int todayReturnMen();
    int monthReturnMen();
    int annualReturnMen();

    int todayReturnWomen();
    int monthReturnWomen();
    int annualReturnWomen();

    int todayReturnKid();
    int monthReturnKid();
    int annualReturnKid();

    int todayPurchasedProduct();
    int monthPurchasedProduct();
    int annualPurchasedProduct();
}
