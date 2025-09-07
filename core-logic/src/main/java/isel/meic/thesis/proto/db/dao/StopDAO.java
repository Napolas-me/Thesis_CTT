package isel.meic.thesis.proto.db.dao;

import isel.meic.thesis.proto.dataTypes.Stop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// REMOVIDO: @Repository
public class StopDAO {
    private final Connection conn;

    public StopDAO(Connection conn) {
        this.conn = conn;
        try {
            clearStop();
            dailyUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar ParagemDAO", e);
        }
    }

    // REMOVIDO: Construtor vazio e setConnection()

    private void dailyUpdate() throws SQLException {
        String selectSql = "SELECT * FROM STOP_TEMPLATE";
        String insertSql = "INSERT INTO STOP_DAILY (stop_name, gate_name, arrival_date, departure_date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            while (rs.next()) {
                String local = rs.getString("stop_name");
                String gate = rs.getString("gate_name");
                Time chegada = rs.getTime("arrival_time");
                Time partida = rs.getTime("departure_time");

                LocalDate today = LocalDate.now();
                Timestamp tsChegada = (chegada != null) ? Timestamp.valueOf(today.atTime(chegada.toLocalTime())) : null;
                Timestamp tsPartida = (partida != null) ? Timestamp.valueOf(today.atTime(partida.toLocalTime())) : null;

                insertStmt.setString(1, local);
                insertStmt.setString(2, gate);
                insertStmt.setTimestamp(3, tsChegada);
                insertStmt.setTimestamp(4, tsPartida);
                insertStmt.setString(5, "scheduled");
                insertStmt.executeUpdate();
            }
        }
    }

    // Este método não é usado na sua lógica atual, mas se for usado,
    // o nome da tabela no SQL deve ser 'STOP_DAILY' e não 'PARAGEM'.
    // public void insertParagem(String local, String gate, Timestamp chegada, Timestamp partida, String status) throws SQLException { ... }

    public List<Stop> getAllStop() throws SQLException {
        String sql = "SELECT * FROM STOP_DAILY";
        List<Stop> paragens = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Stop p = new Stop(
                        rs.getInt("id"),
                        rs.getString("stop_name"), // Corrigido de local_name para stop_name
                        rs.getString("gate_name"),
                        rs.getTimestamp("arrival_date"),
                        rs.getTimestamp("departure_date"),
                        rs.getString("status")
                );
                paragens.add(p);
            }
        }
        return paragens;
    }

    public Stop getStopById(int id) throws SQLException { // Adicionado throws SQLException
        String sql = "SELECT id, stop_name, gate_name, arrival_date, departure_date, status FROM STOP_DAILY WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Stop(
                            rs.getInt("id"),
                            rs.getString("stop_name"),
                            rs.getString("gate_name"),
                            rs.getTimestamp("arrival_date"),
                            rs.getTimestamp("departure_date"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null; // Retorna null se nenhuma paragem for encontrada com o ID dado
    }


    private void clearStop() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM STOP_DAILY");
            stmt.executeUpdate("ALTER TABLE STOP_DAILY AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao limpar paragens", e);
        }
    }
}
