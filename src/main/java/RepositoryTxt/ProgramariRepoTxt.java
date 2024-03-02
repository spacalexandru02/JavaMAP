package RepositoryTxt;

import entity.Pacient;
import entity.Programare;
import repository.AbstractRepo;

import java.io.*;

public class ProgramariRepoTxt extends AbstractRepo<Programare> {
    protected String filePath;

    public ProgramariRepoTxt(String filePath) throws IOException {
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
                    String dataProgramare = parts[1];
                    Integer ora = Integer.parseInt(parts[2]);
                    String scopulProgramarii = parts[3];
                    String pacientID = parts[4];
                    Programare item = new Programare(dataProgramare, ora, scopulProgramarii, pacientID, uniqueID);
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
            for (Programare item : data) {
                String line = item.getUniqueID() + "," + item.getData() + "," + item.getOra() + "," + item.getScopulProgramarii() + ","+item.getPacientID();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Programare item) throws IOException {
        super.add(item);
        saveData();
    }
    public void update(Programare newItem, String id) throws IOException {
        super.update(newItem,id);
        saveData();
    }
    public void delete(String id) throws IOException {
        super.delete(id);
        saveData();
    }
}
