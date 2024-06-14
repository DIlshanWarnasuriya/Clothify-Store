package edu.icet.project.bo.custom;

import edu.icet.project.bo.SuperBo;
import edu.icet.project.dto.Supplier;
import javafx.collections.ObservableList;

public interface SupplierBo extends SuperBo {
    boolean saveSupplier(Supplier dto);

    boolean updateSupplier(Supplier dto);

    boolean deleteSupplier(Supplier dto);

    ObservableList<Supplier> getAll();

    ObservableList<Supplier> search(String data);

    Supplier searchById(int id);
}
