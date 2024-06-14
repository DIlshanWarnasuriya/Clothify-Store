package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.Product;
import javafx.collections.ObservableList;

public interface ProductBo extends SuperBo {
    boolean saveProduct(Product dto);

    boolean updateProduct(Product dto);

    boolean deleteProduct(Product dto);

    ObservableList<Product> getAllProduct();

    ObservableList<Product> searchProduct(String data);

    Product searchById(Integer id);

    boolean equalsProduct(Product product);
}
