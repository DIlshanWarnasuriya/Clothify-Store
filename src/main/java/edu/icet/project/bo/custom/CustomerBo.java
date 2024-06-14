package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.Customer;
import javafx.collections.ObservableList;

public interface CustomerBo extends SuperBo {
    boolean saveCustomer(Customer dto);
    boolean updateCustomer(Customer dto);
    boolean deleteCustomer(Customer customer);
    ObservableList<Customer> getAll();
    ObservableList<Customer> search(String data);
    Customer searchById(Integer id);
}
