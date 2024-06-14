package edu.icet.project.bo.custom.impl;

import edu.icet.project.bo.custom.SupplierBo;
import edu.icet.project.dao.DaoFactory;
import edu.icet.project.dao.custom.SupplierDao;
import edu.icet.project.dto.Supplier;
import edu.icet.project.entity.SupplierEntity;
import edu.icet.project.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

public class SupplierBoImpl implements SupplierBo {
    SupplierDao supplierDao = DaoFactory.getInstance().getDao(DaoType.SUPPLIER);

    @Override
    public boolean saveSupplier(Supplier dto) {
        return supplierDao.save(new ModelMapper().map(dto, SupplierEntity.class));
    }

    @Override
    public boolean updateSupplier(Supplier dto) {
        return supplierDao.update(new ModelMapper().map(dto, SupplierEntity.class));
    }

    @Override
    public boolean deleteSupplier(Supplier dto) {
        return supplierDao.update(new ModelMapper().map(dto, SupplierEntity.class));
    }

    @Override
    public ObservableList<Supplier> getAll() {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        for (SupplierEntity entity : supplierDao.getAll()) {
            list.add(new ModelMapper().map(entity, Supplier.class));
        }
        return list;
    }

    @Override
    public ObservableList<Supplier> search(String data) {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        for (SupplierEntity entity : supplierDao.getAll()) {
            if (entity.getId().toString().equals(data) || entity.getName().equals(data) || entity.getEmail().equals(data) || entity.getContactNo().equals(data) || entity.getGender().equals(data) || entity.getCompany().equals(data)) {
                list.add(new ModelMapper().map(entity, Supplier.class));
            }
        }
        return list;
    }

    @Override
    public Supplier searchById(int id) {
        for (SupplierEntity entity : supplierDao.getAll()) {
            if (entity.getId() == id && !entity.getStatus().equals("deleted")) {
                return new ModelMapper().map(entity, Supplier.class);
            }
        }
        return null;
    }
}
