package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.User;

public interface UserBo extends SuperBo {
    boolean saveUser(User dto);
}
