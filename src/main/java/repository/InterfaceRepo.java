package repository;

import entity.AbstractEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public interface InterfaceRepo <T extends AbstractEntity>{
    void add(T item) throws IOException, SQLException;

    void update(T newItem, String id) throws IOException, SQLException;

    void delete(String id) throws IOException, SQLException;

    Collection<T> getAll() throws SQLException;
}
