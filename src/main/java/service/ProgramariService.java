package service;

import entity.Pacient;
import entity.Programare;
import repository.AbstractRepo;
import repository.InterfaceRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ProgramariService {
    private InterfaceRepo<Programare> repo;

    public ProgramariService(InterfaceRepo<Programare> repo) {
        this.repo = repo;
    }

    public void add(Programare item) throws IOException, SQLException {
        if(item.getOra()<0 && item.getOra()>24)
            throw new IllegalArgumentException("wrong hour. Retry!");
        this.repo.add(item);
    }

    public void update(Programare item, String id) throws IOException, SQLException {
        this.repo.update(item, id);
    }

    public void delete(String index) throws IOException, SQLException {
        this.repo.delete(index);
    }

    public Collection<Programare> getAll() throws SQLException {
        return this.repo.getAll();
    }
}
