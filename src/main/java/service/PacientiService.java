package service;

import entity.Pacient;
import entity.Programare;
import repository.AbstractRepo;
import repository.InterfaceRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PacientiService {
    private InterfaceRepo<Pacient> repo;

    public PacientiService(InterfaceRepo<Pacient> repo) {
        this.repo = repo;
    }

    public void add(Pacient item) throws IOException, SQLException {
        this.repo.add(item);
    }

    public void update(Pacient item, String id) throws IOException, SQLException {
        this.repo.update(item, id);
    }

    public void delete(String index) throws IOException, SQLException {
        this.repo.delete(index);
    }

    public Collection<Pacient> getAll() throws SQLException {
        return this.repo.getAll();
    }
}
