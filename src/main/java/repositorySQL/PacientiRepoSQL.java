package repositorySQL;

import entity.Pacient;
import repository.InterfaceRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacientiRepoSQL implements InterfaceRepo<Pacient> {
    private static final String INSERT_PACIENT = "INSERT INTO pacient(uniqueID, nume, prenume, varsta) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PACIENTS = "SELECT * FROM pacient";
    private static final String SELECT_PACIENT_BY_ID = "SELECT * FROM pacient WHERE uniqueID = ?";
    private static final String UPDATE_PACIENT = "UPDATE pacient SET nume = ?, prenume = ?, varsta = ? WHERE uniqueID = ?";
    private static final String DELETE_PACIENT = "DELETE FROM pacient WHERE uniqueID = ?";
    private Connection connection;

    public PacientiRepoSQL(Connection connection) {
        this.connection = connection;
    }

    public void add(Pacient pacient) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PACIENT)) {
            statement.setString(1, pacient.getUniqueID());
            statement.setString(2, pacient.getNume());
            statement.setString(3, pacient.getPrenume());
            statement.setInt(4, pacient.getVarsta());
            statement.executeUpdate();
        }
    }

    public List<Pacient> getAll() throws SQLException {
        List<Pacient> pacients = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PACIENTS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Pacient pacient = mapResultSetToPacient(resultSet);
                pacients.add(pacient);
            }
        }
        return pacients;
    }

    public Pacient getPacientById(String uniqueID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PACIENT_BY_ID)) {
            statement.setString(1, uniqueID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPacient(resultSet);
                }
            }
        }
        return null;
    }

    public void update(Pacient pacient, String id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PACIENT)) {
            statement.setString(1, pacient.getNume());
            statement.setString(2, pacient.getPrenume());
            statement.setInt(3, pacient.getVarsta());
            statement.setString(4, pacient.getUniqueID());
            statement.executeUpdate();
        }
    }

    public void delete(String uniqueID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PACIENT)) {
            statement.setString(1, uniqueID);
            statement.executeUpdate();
        }
    }

    private Pacient mapResultSetToPacient(ResultSet resultSet) throws SQLException {
        String uniqueID = resultSet.getString("uniqueID");
        String nume = resultSet.getString("nume");
        String prenume = resultSet.getString("prenume");
        int varsta = resultSet.getInt("varsta");
        return new Pacient(nume,prenume, varsta, uniqueID);
    }
}
