package RepositoryTxt;

import entity.Pacient;
import entity.Programare;
import repository.AbstractRepo;

import java.io.*;
import java.util.ArrayList;

public class PacientiRepoTxt extends AbstractRepo<Pacient> {
    protected String filePath;

    public PacientiRepoTxt(String filePath) throws IOException {
        this.filePath = filePath;
        loadData();
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String uniqueID = parts[0];
                    String nume = parts[1];
                    String prenume = parts[2];
                    Integer varsta = Integer.parseInt(parts[3]);
                    Pacient item = new Pacient(nume, prenume, varsta, uniqueID);
                    if (item != null) {
                        data.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Pacient item : data) {
                String line = item.getUniqueID() + "," + item.getNume() + "," + item.getPrenume() + "," + item.getVarsta();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Pacient item) throws IOException {
        super.add(item);
        saveData();
    }
    public void update(Pacient newItem, String id) throws IOException {
        super.update(newItem,id);
        saveData();
    }
    public void delete(String id) throws IOException {
        super.delete(id);
        saveData();
    }
}
