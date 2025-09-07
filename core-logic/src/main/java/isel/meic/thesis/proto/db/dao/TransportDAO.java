package isel.meic.thesis.proto.db.dao;

import isel.meic.thesis.proto.dataTypes.Transport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// REMOVIDO: @Repository
public class TransportDAO {

    private final Connection conn;

    public TransportDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Transport> getAllTransportes() throws SQLException { // Adicionado throws SQLException
        List<Transport> transports = new ArrayList<>();
        String sql = "SELECT id, type, name, capacity, max_capacity, status, carbon_emissions_g_km FROM TRANSPORT";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String name = rs.getString("name");
                int capacity = rs.getInt("capacity");
                int max_capacity = rs.getInt("max_capacity");
                String status = rs.getString("status");
                double em = rs.getDouble("carbon_emissions_g_km"); // Usar getDouble para DECIMAL

                Transport t = new Transport(id, name, type, capacity, max_capacity, status, em);
                transports.add(t);
            }
        }
        return transports;
    }

    public Transport getTransportById(int id) throws SQLException { // Adicionado throws SQLException
        String sql = "SELECT id, type, name, capacity, max_capacity, status, carbon_emissions_g_km FROM TRANSPORT WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    int capacity = rs.getInt("capacity");
                    int max_capacity = rs.getInt("max_capacity");
                    String status = rs.getString("status");
                    double em = rs.getDouble("carbon_emissions_g_km"); // Usar getDouble para DECIMAL

                    return new Transport(id, name, type, capacity, max_capacity, status, em);
                }
            }
        }
        return null;
    }

    public void updateTransport(Transport transport) throws SQLException { // Adicionado throws SQLException
        String sql = "UPDATE TRANSPORT SET type = ?, name = ?, capacity = ?, max_capacity = ?, status = ?, carbon_emissions_g_km = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transport.getType().toString()); // Converter Enum para String
            stmt.setString(2, transport.getName());
            stmt.setInt(3, transport.getCapacity());
            stmt.setInt(4, transport.getMaxCapacity());
            stmt.setString(5, transport.getStatus().toString()); // Converter Enum para String
            stmt.setDouble(6, transport.getEmissions()); // Usar setDouble
            stmt.setInt(7, transport.getId());
            stmt.executeUpdate();
        }
    }
}
