package isel.meic.thesis.proto.db.dao;

import isel.meic.thesis.proto.dataTypes.Trip;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// REMOVIDO: @Repository
public class TripDAO {
    private final Connection conn;

    public TripDAO(Connection conn) {
        this.conn = conn;
        try {
            clearTrip();
            dailyUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao limpar tabela de viagens", e);
        }
    }

    // REMOVIDO: Construtor vazio e setConnection()

    private void dailyUpdate() throws SQLException {
        String selectSql = "SELECT * FROM TRIP_TEMPLATE";
        String insertSql = "INSERT INTO TRIP_DAILY (origin, destination, departure_date, destination_date, status) VALUES (?, ?, ?, ?, ?)";
        LocalDate dataAtual = LocalDate.now();

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            while (rs.next()) {
                String origem = rs.getString("origin");
                String destino = rs.getString("destination");
                Time horaPartida = rs.getTime("departure_time");
                Time horaChegada = rs.getTime("destination_time");

                LocalDateTime tempoPartida = LocalDateTime.of(dataAtual, horaPartida.toLocalTime());
                LocalDateTime tempoChegada = LocalDateTime.of(dataAtual, horaChegada.toLocalTime());

                insertStmt.setString(1, origem);
                insertStmt.setString(2, destino);
                insertStmt.setTimestamp(3, Timestamp.valueOf(tempoPartida));
                insertStmt.setTimestamp(4, Timestamp.valueOf(tempoChegada));
                insertStmt.setString(5, "scheduled");
                insertStmt.executeUpdate();
            }
        }
    }

    public List<Trip> getAllTrips() throws SQLException { // Adicionado throws SQLException
        String sql = "SELECT * FROM TRIP_DAILY";
        List<Trip> viagens = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Trip v = new Trip(
                        rs.getInt("id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_date"),
                        rs.getTimestamp("destination_date"),
                        rs.getString("status")
                );
                viagens.add(v);
            }
        }
        return viagens;
    }

    public Trip getTripById(int id) throws SQLException { // Adicionado throws SQLException
        String sql = "SELECT id, origin, destination, departure_date, destination_date, status FROM TRIP_DAILY WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Trip(
                            rs.getInt("id"),
                            rs.getString("origin"),
                            rs.getString("destination"),
                            rs.getTimestamp("departure_date"),
                            rs.getTimestamp("destination_date"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    private void clearTrip() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM TRIP_DAILY");
            stmt.executeUpdate("ALTER TABLE TRIP_DAILY AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao limpar tabela de viagens", e);
        }
    }
}
