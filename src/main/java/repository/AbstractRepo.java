package repository;
import entity.AbstractEntity;
import java.io.IOException;
import java.util.*;

public class AbstractRepo<T extends AbstractEntity> implements InterfaceRepo<T> {

    protected List<T> data = new ArrayList<>();

    @Override
    public void add(T item) throws IOException {
        if(data.size() > 0 && data.get(0) instanceof entity.Programare)
            data.add(item);
        else
            data.add(item);
    }

    @Override
    public void update(T newItem, String id) throws IOException {
        for (int i = 0; i < data.size(); i++) {
            T item = data.get(i);
            if (item instanceof AbstractEntity entity) {
                if (entity.getUniqueID().equals(id)) {
                    data.set(i, newItem);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Item with ID " + id + " not found");
    }

    @Override
    public void delete(String id) throws IOException {
        T itemToDelete = null;
        for (T item : data) {
            if (item instanceof AbstractEntity entity) {
                if (entity.getUniqueID().equals(id)) {
                    itemToDelete = item;
                    break;
                }
            }
        }

        if (itemToDelete != null) {
            data.remove(itemToDelete);
        } else {
            throw new IllegalArgumentException("Item with ID " + id + " not found");
        }
    }

    public boolean existsByID(String id) {
        for (T item : data) {
            if (item instanceof AbstractEntity entity) {
                if (entity.getUniqueID().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Collection<T> getAll() {
        return new ArrayList<T>(data);
    }

    public Iterator<T> iterator() {
        return new ArrayList<T>(data).iterator();
    }
}