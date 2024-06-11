package edu.icet.project.dao;

import edu.icet.project.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public interface CrudDao<T> extends SuperDao {
    boolean save(T entity);
    boolean update(T entity);
    List<T> getAll();
}
