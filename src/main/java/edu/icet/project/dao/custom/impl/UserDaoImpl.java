package edu.icet.project.dao.custom.impl;

import edu.icet.project.dao.custom.UserDao;
import edu.icet.project.entity.UserEntity;
import edu.icet.project.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(UserEntity entity) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();

        try{
            session.persist(entity);
        }catch (Exception e){
            transaction.rollback();
        }finally {
            session.close();
        }
        return true;
    }

    @Override
    public boolean update(UserEntity entity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public ArrayList<UserEntity> getAll() {
        return null;
    }

    @Override
    public UserEntity search(String id) {
        return null;
    }

}
