package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.User;
import javafx.collections.ObservableList;

public interface UserBo extends SuperBo {
    boolean saveUser(User dto);

    boolean updateUser(User dto);

    boolean deleteUser(User dto);

    ObservableList<User> getAll();

    ObservableList<User> search(String data);

    User searchById(Integer id);
}
