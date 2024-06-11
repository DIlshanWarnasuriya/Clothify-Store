package edu.icet.project.dao;

import java.util.ArrayList;

public interface CrudDao<T> extends SuperDao {
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(String id);
    ArrayList<T> getAll();
    T search(String id);
}
