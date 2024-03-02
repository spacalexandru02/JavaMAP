package UI;

import entity.Pacient;
import entity.Programare;
import repository.InterfaceRepo;
import service.PacientiService;
import service.ProgramariService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UI {
    Scanner scanner = new Scanner(System.in);
    PacientiService pacientiService;
    ProgramariService programariService;
    InterfaceRepo<Pacient> pacientiRepo;
    InterfaceRepo<Programare> programariRepo;

    public UI(InterfaceRepo<Pacient> pacientiRepo, InterfaceRepo<Programare> programariRepo) {
        this.pacientiRepo = pacientiRepo;
        this.programariRepo = programariRepo;
        this.pacientiService = new PacientiService(pacientiRepo);
        this.programariService = new ProgramariService(programariRepo);
    }

    public void ui() {
        try {
            int option = 1;
            while (option != 0) {
                System.out.println(menuPrint());
                option = scanner.nextInt();
                switch (option) {
                    case 0: {
                        option = 0;
                        break;
                    }
                    case 1: {
                        addPacient();
                        break;
                    }
                    case 2: {
                        addProgramare();
                        break;
                    }
                    case 3: {
                        updatePacient();
                        break;
                    }
                    case 4: {
                        updateProgramare();
                        break;
                    }
                    case 5: {
                        deletePacient();
                        break;
                    }
                    case 6: {
                        deleteProgramare();
                        break;
                    }
                    case 7: {
                        printPacient();
                        break;
                    }
                    case 8: {
                        printProgramare();
                        break;
                    }
                    case 9: {
                        filtrare1();
                        break;
                    }
                    case 10: {
                        filtrare2();
                        break;
                    }
                    case 11: {
                        filtrare3();
                        break;
                    }
                    case 12: {
                        filtrare2();
                        break;
                    }
                    default: {
                        System.out.println("Comanda gresita, Reincercati!");
                    }
                }
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer for 'varsta'.");
            scanner.next();  // Consume the invalid input to prevent an infinite loop.
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    public static String menuPrint() {
        return ("Menu: \n " +
                "0. Iesire\n" +
                "1. Adauga Pacient\n" +
                "2. Adauga Programare\n" +
                "3. Modifica Pacient\n" +
                "4. Modifica Programare\n" +
                "5. Sterge Pacient\n" +
                "6. Sterge Programare\n" +
                "7. Afiseaza Pacienti\n" +
                "8. Afiseaza Programari\n" +
                "9. Numărul de programări pentru fiecare pacient în parte\n" +
                "10. Numărul total de programări pentru fiecare lună a anului\n" +
                "11. Numărul de zile trecute de la ultima programare a fiecărui pacient\n" +
                "12. Cele mai aglomerate luni ale anului.\n" +
                "Selecteaza optiune: ");
    }
    public void addPacient() throws IOException, SQLException {
        System.out.println("nume: ");
        String nume = scanner.next();
        System.out.println("prenume: ");
        String prenume = scanner.next();
        System.out.println("varsta: ");
        Integer varsta = scanner.nextInt();
        Pacient pacient = new Pacient(nume, prenume, varsta);
        pacientiService.add(pacient);
    }

    public void addProgramare() throws IOException, SQLException {
        System.out.println("data: ");
        String data = scanner.next();
        System.out.println("ora: ");
        Integer ora = scanner.nextInt();
        System.out.println("id-ul pacientului: ");
        String pacientID = scanner.next();
        if(!existPacient(pacientID))
            throw new IllegalArgumentException("Item with ID " + pacientID + " not found");
        System.out.println("scopul programarii: ");
        String scopulProgramarii = scanner.next();
        Programare programare = new Programare(data, ora, scopulProgramarii, pacientID);
        programariService.add(programare);
    }

    public void updateProgramare() throws IOException, SQLException {
        System.out.println("id programare: ");
        String id = scanner.next();
        System.out.println("data: ");
        String data = scanner.next();
        System.out.println("ora: ");
        Integer ora = scanner.nextInt();
        System.out.println("id-ul pacientului: ");
        String pacientID = scanner.next();
        System.out.println("scopul programarii: ");
        String scopulProgramarii = scanner.next();
        Programare programare = new Programare(data, ora, scopulProgramarii, pacientID);
        programariService.update(programare, id);
    }

    public void updatePacient() throws IOException, SQLException {
        System.out.println("id pacient: ");
        String id = scanner.next();
        System.out.println("nume: ");
        String nume = scanner.next();
        System.out.println("prenume: ");
        String prenume = scanner.next();
        System.out.println("varsta: ");
        Integer varsta = scanner.nextInt();
        Pacient pacient = new Pacient(nume, prenume, varsta);
        pacientiService.update(pacient, id);
    }

    public void deleteProgramare() throws IOException, SQLException {
        System.out.println("id programare: ");
        String id = scanner.next();
        programariService.delete(id);
    }

    public void deletePacient() throws IOException, SQLException {
        System.out.println("id pacient: ");
        String id = scanner.next();
        pacientiService.delete(id);
    }

    public void printProgramare() throws SQLException {
        Collection<Programare> list= programariService.getAll();
        for(Programare programare: list)
            System.out.println(programare.toString());
    }

    public void printPacient() throws SQLException {
        Collection<Pacient> list = pacientiService.getAll();
        for(Pacient pacient: list)
            System.out.println(pacient.toString());
    }

    public boolean existPacient(String id) throws SQLException {
        Collection<Pacient> pacientList = pacientiService.getAll();
        for(Pacient pacient: pacientList)
            if(Objects.equals(pacient.getUniqueID(), id))
                return true;
        return false;
    }

    public void filtrare1() throws SQLException {
        Collection<Pacient> pacientList = pacientiService.getAll();
        Collection<Programare> programarilist= programariService.getAll();
        Map<Pacient, Long> filteredMap = pacientList.stream()
                .collect(Collectors.toMap(
                        pacient -> pacient,
                        pacient -> programarilist.stream()
                                .filter(programare -> programare.getPacientID().equals(pacient.getUniqueID()))
                                .count()
                ));
        Map<Pacient, Long> sortedDictionary = filteredMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
        sortedDictionary.forEach((pacient, count) ->
                System.out.println("Pacient: " + pacient + ", programari: " + count)
        );
    }
    public void filtrare2() throws SQLException {
        Collection<Programare> programarilist= programariService.getAll();
        String[] monthAbbreviations = new DateFormatSymbols().getShortMonths();
        List<String> monthList = Arrays.asList(monthAbbreviations);
        Map<String, Long> filteredMap = monthList.stream()
                .collect(Collectors.toMap(
                        month -> month,
                        month -> programarilist.stream()
                                .filter(programare -> Objects.equals(programare.getData().split("/")[1], month))
                                .count()
                ));
        Map<String, Long> reversedMap = filteredMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        reversedMap.forEach((month, count) ->
                System.out.println("Luna: " + month + ", programari: " + count)
        );
    }

    public void filtrare3() throws SQLException {
        Collection<Pacient> pacientList = pacientiService.getAll();
        Collection<Programare> programarilist= programariService.getAll();
        List<String> lista = new ArrayList<>();
        Map<String, Long> filteredMap = pacientList.stream()
                .collect(HashMap::new, (map, pacient) -> {
                    long maxDaysLeft = programarilist.stream()
                            .filter(programare -> Objects.equals(programare.getPacientID(), pacient.getUniqueID()))
                            .map(programare -> {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
                                Date firstDate;
                                Date secondDate;
                                try {
                                    firstDate = sdf.parse(programare.getData());
                                    secondDate = sdf.parse("11/Dec/2023");
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                                return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            })
                            .max(Long::compare)
                            .orElse(0L);
                    map.put(pacient.getUniqueID(), maxDaysLeft);
                }, HashMap::putAll);

        filteredMap.forEach((pacient, maxx) ->
                System.out.println("Pacient id:" + pacient + ", Numărul de zile trecute de la ultima programare: " + maxx));
    }

}