package repositorySQL;

import entity.Pacient;
import entity.Programare;
import repository.InterfaceRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramariRepoSQL implements InterfaceRepo<Programare> {
    private static final String INSERT_PROGRAMARE = "INSERT INTO programare(uniqueID, data, ora, scopulProgramarii, pacientID) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_PROGRAMARI = "SELECT * FROM programare";
    private static final String SELECT_PROGRAMARE_BY_ID = "SELECT * FROM programare WHERE uniqueID = ?";
    private static final String UPDATE_PROGRAMARE = "UPDATE programare SET data = ?, ora = ?, scopulProgramarii = ?, pacientID = ? WHERE uniqueID = ?";
    private static final String DELETE_PROGRAMARE = "DELETE FROM programare WHERE uniqueID = ?";
    private Connection connection;

    public ProgramariRepoSQL(Connection connection) {
        this.connection = connection;
    }

    public void add(Programare programare) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PROGRAMARE)) {
            statement.setString(1, programare.getUniqueID());
            statement.setString(2, programare.getData());
            statement.setInt(3, programare.getOra());
            statement.setString(4, programare.getScopulProgramarii());
            statement.setString(5, programare.getPacientID());
            statement.executeUpdate();
        }
    }

    public List<Programare> getAll() throws SQLException {
        List<Programare> programari = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PROGRAMARI);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Programare programare = mapResultSetToProgramare(resultSet);
                programari.add(programare);
            }
        }
        return programari;
    }

    public Programare getProgramareById(String uniqueID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PROGRAMARE_BY_ID)) {
            statement.setString(1, uniqueID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProgramare(resultSet);
                }
            }
        }
        return null;
    }

    public void update(Programare programare, String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PROGRAMARE)) {
            statement.setString(1, programare.getData());
            statement.setInt(2, programare.getOra());
            statement.setString(3, programare.getScopulProgramarii());
            statement.setString(4, programare.getPacientID());
            statement.setString(5, programare.getUniqueID());
            statement.executeUpdate();
        }
    }

    public void delete(String uniqueID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PROGRAMARE)) {
            statement.setString(1, uniqueID);
            statement.executeUpdate();
        }
    }

    private Programare mapResultSetToProgramare(ResultSet resultSet) throws SQLException {
        String uniqueID = resultSet.getString("uniqueID");
        String data = resultSet.getString("data");
        int ora = resultSet.getInt("ora");
        String scopulProgramarii = resultSet.getString("scopulProgramarii");
        String pacientID = resultSet.getString("pacientID");
        return new Programare(data, ora, scopulProgramarii,pacientID,uniqueID);
    }
}

