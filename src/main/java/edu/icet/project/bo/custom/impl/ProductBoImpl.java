package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.custom.ProductBo;
import edu.icet.project.dao.DaoFactory;
import edu.icet.project.dao.custom.ProductDao;
import edu.icet.project.dto.Product;
import edu.icet.project.dto.User;
import edu.icet.project.entity.ProductEntity;
import edu.icet.project.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class ProductBoImpl implements ProductBo {

    private final ProductDao productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);

    @Override
    public boolean saveProduct(Product dto) {
        return productDao.save(new ModelMapper().map(dto, ProductEntity.class));
    }

    @Override
    public boolean updateProduct(Product dto) {
        return productDao.update(new ModelMapper().map(dto, ProductEntity.class));
    }

    @Override
    public boolean deleteProduct(Product dto) {
        return productDao.update(new ModelMapper().map(dto, ProductEntity.class));
    }

    @Override
    public ObservableList<Product> getAllProduct() {
        ObservableList<Product> list = FXCollections.observableArrayList();
        for (ProductEntity entity : productDao.getAll()) {
            if (!entity.getStatus().equals("deleted")){
                list.add(new ModelMapper().map(entity, Product.class));
            }
        }
        return list;
    }

    @Override
    public ObservableList<Product> searchProduct(String data) {
        ObservableList<Product> list = FXCollections.observableArrayList();
        for (ProductEntity entity : productDao.getAll()) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(entity.getDate());
            if (entity.getId().toString().equals(data) || entity.getName().equals(data) || entity.getCategory().equals(data) || entity.getPrice().toString().equals(data) || entity.getSize().equals(data) || date.equals(data)) {
                list.add(new ModelMapper().map(entity, Product.class));
            }
        }
        return list;
    }

    @Override
    public Product searchById(Integer id) {
        for (ProductEntity entity : productDao.getAll()) {
            if (Objects.equals(entity.getId(), id)) {
                return new ModelMapper().map(entity, Product.class);
            }
        }
        return null;
    }

    @Override
    public boolean equalsProduct(Product product) {
        for (ProductEntity entity : productDao.getAll()) {
            if (entity.getName().equals(product.getName()) && entity.getCategory().equals(product.getCategory()) && entity.getSize().equals(product.getSize()) && Objects.equals(entity.getSupplierId(), product.getSupplierId()) && !entity.getStatus().equals("deleted")) {
                return true;
            }
        }
        return false;
    }
}
