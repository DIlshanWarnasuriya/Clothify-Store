package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dao.DaoFactory;
import edu.icet.project.dao.custom.UserDao;
import edu.icet.project.dto.User;
import edu.icet.project.entity.UserEntity;
import edu.icet.project.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;


public class UserBoImpl implements UserBo {

    private final UserDao userDao = DaoFactory.getInstance().getDao(DaoType.USER);

    @Override
    public boolean saveUser(User dto) {
        return userDao.save(new ModelMapper().map(dto, UserEntity.class));
    }

    @Override
    public boolean updateUser(User dto) {
        return userDao.update(new ModelMapper().map(dto, UserEntity.class));
    }

    @Override
    public boolean deleteUser(User dto) {
        return userDao.update(new ModelMapper().map(dto, UserEntity.class));
    }

    @Override
    public ObservableList<User> getAll() {
        ObservableList<User> list = FXCollections.observableArrayList();

        for (UserEntity entity : userDao.getAll()){
            list.add(new ModelMapper().map(entity, User.class));
        }
        return list;
    }

    @Override
    public ObservableList<User> search(String data) {
        ObservableList<User> tableData = FXCollections.observableArrayList();

        for (User user : getAll()){
            if (user.getId().toString().equals(data) || user.getName().equals(data) || user.getEmail().equals(data) || user.getAddress().equals(data) || user.getContactNo().equals(data) || user.getGender().equals(data) || user.getUserType().equals(data)){
                tableData.add(user);
            }
        }
        return tableData;
    }


}
