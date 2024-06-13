package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.custom.CustomerBo;
import edu.icet.project.dao.DaoFactory;
import edu.icet.project.dao.custom.CustomerDao;
import edu.icet.project.dto.Customer;
import edu.icet.project.entity.CustomerEntity;
import edu.icet.project.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

public class CustomerBoImpl implements CustomerBo {

    CustomerDao customerDao = DaoFactory.getInstance().getDao(DaoType.CUSTOMER);

    @Override
    public boolean saveCustomer(Customer dto) {
        return customerDao.save(new ModelMapper().map(dto, CustomerEntity.class));
    }

    @Override
    public boolean updateCustomer(Customer dto) {
        return customerDao.update(new ModelMapper().map(dto, CustomerEntity.class));
    }

    @Override
    public boolean deleteCustomer(Customer dto) {
        return customerDao.update(new ModelMapper().map(dto, CustomerEntity.class));
    }

    @Override
    public ObservableList<Customer> getAll() {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        for (CustomerEntity entity: customerDao.getAll()){
            list.add(new ModelMapper().map(entity, Customer.class));
        }
        return list;
    }

    @Override
    public ObservableList<Customer> search(String data) {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        for (CustomerEntity entity : customerDao.getAll()){
            if (entity.getId().toString().equals(data) || entity.getName().equals(data) || entity.getEmail().equals(data) || entity.getAddress().equals(data) || entity.getContactNo().equals(data) || entity.getGender().equals(data)){
                list.add(new ModelMapper().map(entity, Customer.class));
            }
        }
        return list;
    }
}
