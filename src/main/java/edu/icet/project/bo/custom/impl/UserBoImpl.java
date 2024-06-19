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

import java.util.Objects;


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
        for (UserEntity entity : userDao.getAll()) {
            if (!entity.getStatus().equals("deleted")){
                list.add(new ModelMapper().map(entity, User.class));
            }
        }
        return list;
    }

    @Override
    public ObservableList<User> search(String data) {
        ObservableList<User> tableData = FXCollections.observableArrayList();
        for (UserEntity entity : userDao.getAll()) {
            if (entity.getId().toString().equals(data) || entity.getName().equals(data) || entity.getEmail().equals(data) || entity.getAddress().equals(data) || entity.getContactNo().equals(data) || entity.getGender().equals(data) || entity.getUserType().equals(data)) {
                tableData.add(new ModelMapper().map(entity, User.class));
            }
        }
        return tableData;
    }

    @Override
    public User searchById(Integer id) {
        for (UserEntity entity : userDao.getAll()) {
            if (Objects.equals(entity.getId(), id)) {
                return new ModelMapper().map(entity, User.class);
            }
        }
        return null;
    }


}
