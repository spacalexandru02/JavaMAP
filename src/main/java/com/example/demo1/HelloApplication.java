package com.example.demo1;

import RepositoryTxt.PacientiRepoTxt;
import RepositoryTxt.ProgramariRepoTxt;
import UI.UI;
import configReader.ConfigReader;
import entity.Pacient;
import entity.Programare;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repository.InterfaceRepo;
import repositoryBin.RepoBin;
import repositorySQL.DatabaseConnector;
import repositorySQL.PacientiRepoSQL;
import repositorySQL.ProgramariRepoSQL;
import repositoryXML.PacientiRepoXML;
import repositoryXML.ProgramariRepoXML;
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

public class HelloApplication extends Application {
    static PacientiService pacientiService;
    static ProgramariService programariService;

    public List<String> filtrare1() throws SQLException {
        Collection<Pacient> pacientList = pacientiService.getAll();
        Collection<Programare> programarilist= programariService.getAll();
        List<String> lista = new ArrayList<>();
        Map<String, Long> filteredMap = pacientList.stream()
                .collect(Collectors.toMap(
                        pacient -> pacient.getUniqueID(),
                        pacient -> programarilist.stream()
                                .filter(programare -> programare.getPacientID().equals(pacient.getUniqueID()))
                                .count()
                ));
        Map<String, Long> sortedDictionary = filteredMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
        sortedDictionary.forEach((pacient, count) ->
                lista.add("Pacient: " + pacient + ", programari: " + count)
        );
        return lista;
    }
    public List<String> filtrare2() throws SQLException {
        Collection<Programare> programarilist= programariService.getAll();
        String[] monthAbbreviations = new DateFormatSymbols().getShortMonths();
        List<String> monthList = Arrays.asList(monthAbbreviations);
        List<String> lista = new ArrayList<>();
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
                lista.add("Luna: " + month + ", programari: " + count)
        );
        return lista;
    }

    public List<String> filtrare3() throws SQLException {
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
                lista.add("Pacient id:" + pacient + ", Numărul de zile trecute de la ultima programare: " + maxx));
        return lista;
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        VBox mainVerticalBox = new VBox();
        ListView<Programare> programareListView = new ListView<>();
        ObservableList<Programare> programari = FXCollections.observableArrayList(programariService.getAll());
        programareListView.setItems(programari);
        mainVerticalBox.getChildren().add(programareListView);

        GridPane programariGridPane = new GridPane();
        Label idLabel = new Label("ID");
        TextField idTextField = new TextField();
        Label dataLabel = new Label("Data");
        TextField dataTextField = new TextField();
        Label oraLabel = new Label("ora");
        TextField oraTextField = new TextField();
        Label scopulProgramariiLabel = new Label("scopul programarii");
        TextField scopulProgramariiTextField = new TextField();
        Label pacientIDLabel = new Label("id pacient");
        TextField pacientIDTextField = new TextField();

        programariGridPane.add(dataLabel,0,0);
        programariGridPane.add(dataTextField,1,0);
        programariGridPane.add(oraLabel,0,1);
        programariGridPane.add(oraTextField,1,1);
        programariGridPane.add(scopulProgramariiLabel,0,2);
        programariGridPane.add(scopulProgramariiTextField,1,2);
        programariGridPane.add(pacientIDLabel,0,3);
        programariGridPane.add(pacientIDTextField,1,3);
        programariGridPane.add(idLabel,0,4);
        programariGridPane.add(idTextField,1,4);
        mainVerticalBox.getChildren().add(programariGridPane);

        HBox programariActionsHorizontalBox = new HBox();
        Button addProgramariButton = new Button("Add");
        Button deleteProgramariButton = new Button("Delete");
        Button updateProgramariButton = new Button("Update");
        Button filtrare1Button = new Button("Filtrare 1");
        Button filtrare2Button = new Button("Filtrare 2");
        Button filtrare3Button = new Button("Filtrare 3");
        Button filtrare4Button = new Button("Filtrare 4");
        programariActionsHorizontalBox.getChildren().add(addProgramariButton);
        programariActionsHorizontalBox.getChildren().add(deleteProgramariButton);
        programariActionsHorizontalBox.getChildren().add(updateProgramariButton);
        programariActionsHorizontalBox.getChildren().add(filtrare1Button);
        programariActionsHorizontalBox.getChildren().add(filtrare2Button);
        programariActionsHorizontalBox.getChildren().add(filtrare3Button);
        programariActionsHorizontalBox.getChildren().add(filtrare4Button);
        mainVerticalBox.getChildren().add(programariActionsHorizontalBox);

        addProgramariButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String id = idTextField.getText();
                    int ora = Integer.parseInt(oraTextField.getText());
                    String data = dataTextField.getText();
                    String scopulProgramarii = scopulProgramariiTextField.getText();
                    String pacientID = pacientIDTextField.getText();
                    Programare programare = new Programare(data, ora, scopulProgramarii, pacientID, id);
                    programariService.add(programare);
                    programari.setAll(programariService.getAll());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();

                    Scene scene = new Scene(mainVerticalBox, 320, 240);
                    stage.setTitle("Medical application!");
                    stage.setScene(scene);
                    stage.show();
                }
            }});
        updateProgramariButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String id = idTextField.getText();
                    int ora = Integer.parseInt(oraTextField.getText());
                    String data = dataTextField.getText();
                    String scopulProgramarii = scopulProgramariiTextField.getText();
                    String pacientID = pacientIDTextField.getText();
                    Programare programare = new Programare(data, ora, scopulProgramarii, pacientID);
                    programariService.update(programare, id);
                    programari.setAll(programariService.getAll());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();

                    Scene scene = new Scene(mainVerticalBox, 320, 240);
                    stage.setTitle("Medical application!");
                    stage.setScene(scene);
                    stage.show();
                }
            }});
        deleteProgramariButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String id = idTextField.getText();
                    int ora = Integer.parseInt(oraTextField.getText());
                    String data = dataTextField.getText();
                    String scopulProgramarii = scopulProgramariiTextField.getText();
                    String pacientID = pacientIDTextField.getText();
                    Programare programare = new Programare(data, ora, scopulProgramarii, pacientID);
                    programariService.delete(id);
                    programari.setAll(programariService.getAll());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();

                    Scene scene = new Scene(mainVerticalBox, 320, 240);
                    stage.setTitle("Medical application!");
                    stage.setScene(scene);
                    stage.show();
                }
            }});

        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Programari", mainVerticalBox);
        tab1.setClosable(false);
        tabPane.getTabs().add(tab1);

        VBox secondVerticalBox = new VBox();
        ListView<Pacient> pacientListView = new ListView<>();
        ObservableList<Pacient> pacienti = FXCollections.observableArrayList(pacientiService.getAll());
        pacientListView.setItems(pacienti);
        secondVerticalBox.getChildren().add(pacientListView);

        GridPane pacientiGridPane = new GridPane();
        Label idLabel2 = new Label("ID");
        TextField idTextField2 = new TextField();
        Label numeLabel = new Label("Nume");
        TextField numeTextField = new TextField();
        Label prenumeLabel = new Label("Prenume");
        TextField prenumeTextField = new TextField();
        Label varstaLabel = new Label("Varsta");
        TextField varstaTextField = new TextField();

        pacientiGridPane.add(idLabel2,0,0);
        pacientiGridPane.add(idTextField2,1,0);
        pacientiGridPane.add(numeLabel,0,1);
        pacientiGridPane.add(numeTextField,1,1);
        pacientiGridPane.add(prenumeLabel,0,2);
        pacientiGridPane.add(prenumeTextField,1,2);
        pacientiGridPane.add(varstaLabel,0,3);
        pacientiGridPane.add(varstaTextField,1,3);
        secondVerticalBox.getChildren().add(pacientiGridPane);
        HBox pacientiActionsHorizontalBox = new HBox();
        Button addPacientiButton = new Button("Add");
        Button deletePacientiButton = new Button("Delete");
        Button updatePacientiButton = new Button("Update");
        pacientiActionsHorizontalBox.getChildren().add(addPacientiButton);
        pacientiActionsHorizontalBox.getChildren().add(deletePacientiButton);
        pacientiActionsHorizontalBox.getChildren().add(updatePacientiButton);
        secondVerticalBox.getChildren().add(pacientiActionsHorizontalBox);

        addPacientiButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String id = idTextField2.getText();
                    int varsta = Integer.parseInt(varstaTextField.getText());
                    String prenume = prenumeTextField.getText();
                    String nume = numeTextField.getText();
                    Pacient pacient = new Pacient(nume, prenume, varsta, id);
                    pacientiService.add(pacient);
                    pacienti.setAll(pacientiService.getAll());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();

                    Scene scene = new Scene(secondVerticalBox, 320, 240);
                    stage.setTitle("Medical application!");
                    stage.setScene(scene);
                    stage.show();
                }
            }});
        updatePacientiButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String id = idTextField2.getText();
                    int varsta = Integer.parseInt(varstaTextField.getText());
                    String prenume = prenumeTextField.getText();
                    String nume = numeTextField.getText();
                    Pacient pacient = new Pacient(nume, prenume, varsta, id);
                    pacientiService.update(pacient, id);
                    pacienti.setAll(pacientiService.getAll());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();

                    Scene scene = new Scene(secondVerticalBox, 320, 240);
                    stage.setTitle("Medical application!");
                    stage.setScene(scene);
                    stage.show();
                }
            }});
        deletePacientiButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String id = idTextField2.getText();
                    int varsta = Integer.parseInt(varstaTextField.getText());
                    String prenume = prenumeTextField.getText();
                    String nume = numeTextField.getText();
                    Pacient pacient = new Pacient(nume, prenume, varsta, id);
                    pacientiService.delete(id);
                    pacienti.setAll(pacientiService.getAll());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();

                    Scene scene = new Scene(secondVerticalBox, 320, 240);
                    stage.setTitle("Medical application!");
                    stage.setScene(scene);
                    stage.show();
                }
            }});

        filtrare1Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox thirdVerticalBox = new VBox();
                ListView<String > filtrare1ListView = new ListView<>();
                ObservableList<String> filtrari1;
                try {
                    filtrari1 = FXCollections.observableArrayList(filtrare1());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                filtrare1ListView.setItems(filtrari1);
                thirdVerticalBox.getChildren().add(filtrare1ListView);

                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(thirdVerticalBox, 450, 450));
                stage.show();
            }});

        filtrare2Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox thirdVerticalBox = new VBox();
                ListView<String > filtrare1ListView = new ListView<>();
                ObservableList<String> filtrari1;
                try {
                    filtrari1 = FXCollections.observableArrayList(filtrare2());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                filtrare1ListView.setItems(filtrari1);
                thirdVerticalBox.getChildren().add(filtrare1ListView);

                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(thirdVerticalBox, 450, 450));
                stage.show();
            }});

        filtrare3Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox thirdVerticalBox = new VBox();
                ListView<String > filtrare1ListView = new ListView<>();
                ObservableList<String> filtrari1;
                try {
                    filtrari1 = FXCollections.observableArrayList(filtrare3());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                filtrare1ListView.setItems(filtrari1);
                thirdVerticalBox.getChildren().add(filtrare1ListView);

                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(thirdVerticalBox, 450, 450));
                stage.show();
            }});

        filtrare4Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox thirdVerticalBox = new VBox();
                ListView<String > filtrare1ListView = new ListView<>();
                ObservableList<String> filtrari1;
                try {
                    filtrari1 = FXCollections.observableArrayList(filtrare2());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                filtrare1ListView.setItems(filtrari1);
                thirdVerticalBox.getChildren().add(filtrare1ListView);

                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(thirdVerticalBox, 450, 450));
                stage.show();
            }});
        Tab tab2 = new Tab("Pacienti");
        tab2.setClosable(false);
        tab2.setContent(secondVerticalBox);
        tabPane.getTabs().add(tab2);

        secondVerticalBox.setPadding(new Insets(10, 10, 10, 10));
        mainVerticalBox.setPadding(new Insets(10, 10, 10, 10));


        VBox vBox = new VBox(tabPane);
        Scene tabs = new Scene(vBox);

        stage.setTitle("Hello!");
        stage.setScene(tabs);
        stage.show();
    }
    public static void main(String[] args) throws IOException, SQLException {
        ConfigReader configReader = new ConfigReader();
        Map<String,String> config = configReader.config();

        if(Objects.equals(config.get("Repository"), "binary")) {
            RepoBin<Pacient> repoPacienti = new RepoBin<Pacient> (config.get("Pacienti"));
            RepoBin<Programare> repoProgramari = new RepoBin<Programare>(config.get("Programari"));
            if(Objects.equals(config.get("App"), "Console"))
            {
                UI ui = new UI(repoPacienti, repoProgramari);
                ui.ui();
            }
            pacientiService = new PacientiService(repoPacienti);
            programariService = new ProgramariService(repoProgramari);
        }
        else if(Objects.equals(config.get("Repository"), "text")) {
            PacientiRepoTxt repoPacienti = new PacientiRepoTxt (config.get("Pacienti"));
            ProgramariRepoTxt repoProgramari = new ProgramariRepoTxt(config.get("Programari"));
            if(Objects.equals(config.get("App"), "Console"))
            {
                UI ui = new UI(repoPacienti, repoProgramari);
                ui.ui();
            }
            pacientiService = new PacientiService(repoPacienti);
            programariService = new ProgramariService(repoProgramari);
        }
        else if(Objects.equals(config.get("Repository"), "xml")){
            PacientiRepoXML repoPacienti = new PacientiRepoXML (config.get("Pacienti"));
            ProgramariRepoXML repoProgramari = new ProgramariRepoXML(config.get("Programari"));
            if(Objects.equals(config.get("App"), "Console"))
            {
                UI ui = new UI(repoPacienti, repoProgramari);
                ui.ui();
            }
            pacientiService = new PacientiService(repoPacienti);
            programariService = new ProgramariService(repoProgramari);
        }
        else {
            DatabaseConnector connector = new DatabaseConnector();
            connector.connect();
            PacientiRepoSQL repoPacienti = new PacientiRepoSQL(connector.getConnection());
            ProgramariRepoSQL repoProgramari = new ProgramariRepoSQL(connector.getConnection());
            if(Objects.equals(config.get("App"), "Console"))
            {
                UI ui = new UI(repoPacienti, repoProgramari);
                ui.ui();
            }
            pacientiService = new PacientiService(repoPacienti);
            programariService = new ProgramariService(repoProgramari);
        }
        launch();
    }
}