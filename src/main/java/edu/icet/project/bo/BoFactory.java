package edu.icet.project.bo;

import edu.icet.project.bo.custom.impl.*;
import edu.icet.project.util.BoType;

public class BoFactory {
    private static BoFactory instance;

    private BoFactory() {
    }

    public static BoFactory getInstance() {
        return instance == null ? instance = new BoFactory() : instance;
    }

    public <T extends SuperBo> T getBo(BoType type) {
        switch (type) {
            case USER:
                return (T) new UserBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case SUPPLIER:
                return (T) new SupplierBoImpl();
            case PRODUCT:
                return (T) new ProductBoImpl();
            case ORDERS:
                return (T) new OrdersBoImpl();
            case HOME:
                return (T) new HomeBoImpl();
        }
        return null;
    }
}
