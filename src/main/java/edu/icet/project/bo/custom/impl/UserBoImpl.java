package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dao.DaoFactory;
import edu.icet.project.dao.custom.UserDao;
import edu.icet.project.dto.User;
import edu.icet.project.entity.UserEntity;
import edu.icet.project.util.DaoType;
import org.modelmapper.ModelMapper;

public class UserBoImpl implements UserBo {

    private UserDao userDao = DaoFactory.getInstance().getDao(DaoType.USER);

    @Override
    public boolean saveUser(User dto) {
        return userDao.save(new ModelMapper().map(dto, UserEntity.class));
    }
}
