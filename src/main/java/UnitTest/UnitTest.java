/*
package UnitTest;
import entity.Pacient;
import entity.Programare;
import org.junit.Test;

import repository.AbstractRepo;
import repository.InterfaceRepo;
import service.PacientiService;
import service.ProgramariService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

public class UnitTest {

    InterfaceRepo<Pacient> pacientiRepo= new AbstractRepo<Pacient>();
    InterfaceRepo<Programare> programariRepo = new AbstractRepo<>();
    PacientiService pacientiService = new PacientiService(pacientiRepo);
    ProgramariService programariService = new ProgramariService(programariRepo);
    Pacient pacient1 = new Pacient("a","a",12);
    Programare programare1 = new Programare("12",12,"nu stiu", pacient1.getUniqueID());
    public UnitTest() throws IOException, SQLException {
        this.pacientiService.add(pacient1);
        this.programariService.add(programare1);
    }

    @Test
    public void TestAdd() throws SQLException {
        assertEquals(1, pacientiService.getAll().size());
        assertEquals(1, programariService.getAll().size());
    }

    @Test
    public void TestUpdate() throws IOException, SQLException {
        Pacient pacient2 = new Pacient("b", "b", 25);
        pacientiService.update(pacient2, pacient1.getUniqueID());
        Programare programare2 = new Programare("asd",16,"poate", pacient2.getUniqueID());
        programariService.update(programare2, programare1.getUniqueID());
        ArrayList<Pacient> pacientList = (ArrayList<Pacient>) pacientiService.getAll();
        ArrayList<Programare> programariList = (ArrayList<Programare>) programariService.getAll();
        assertEquals("b", pacientList.get(0).getNume());
        assertEquals("b", pacientList.get(0).getPrenume());
        assertEquals("asd", programariList.get(0).getData());
        assertEquals("poate", programariList.get(0).getScopulProgramarii());
    }

    @Test
    public void TestDelete() throws IOException, SQLException {
        programariService.delete(programare1.getUniqueID());
        pacientiService.delete(pacient1.getUniqueID());
        assertEquals(0, pacientiService.getAll().size());
        assertEquals(0, programariService.getAll().size());
    }
    private int result(){
        return 5;
    }
}
*/