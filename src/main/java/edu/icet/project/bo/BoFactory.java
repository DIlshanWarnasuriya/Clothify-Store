package edu.icet.project.bo;

import edu.icet.project.bo.custom.impl.UserBoImpl;
import edu.icet.project.util.BoType;

public class BoFactory {
    private static BoFactory instance;
    private BoFactory(){}

    public static BoFactory getInstance(){
        return instance==null ? instance = new BoFactory() : instance;
    }

    public <T extends SuperBo> T getBo(BoType type){
        switch (type){
            case USER: return (T) new UserBoImpl();
        }
        return null;
    }
}
