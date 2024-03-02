package repositoryBin;

import entity.AbstractEntity;
import repository.AbstractRepo;

import java.io.*;
import java.util.ArrayList;

public class RepoBin<T extends AbstractEntity> extends AbstractRepo<T> {
    protected String filePath;

    public RepoBin(String filePath) throws IOException {
        this.filePath = filePath;
        loadData();
    }

    private void loadData() {
        try {
            FileInputStream readData = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(readData);
            data = (ArrayList<T>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            data = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    private void saveData() {
        try {
            FileOutputStream fileout = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fileout);
            oos.writeObject(data);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T item) throws IOException {
        super.add(item);
        saveData();
    }
    public void update(T newItem, String id) throws IOException {
        super.update(newItem,id);
        saveData();
    }
    public void delete(String id) throws IOException {
        super.delete(id);
        saveData();
    }
}
