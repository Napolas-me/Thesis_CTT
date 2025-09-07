package isel.meic.thesis.proto.dao;

import isel.meic.thesis.proto.dto.EntityDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp; // Importar Timestamp
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Repository // Marca esta classe como um componente de repositório Spring
public class EntityDAO {

    private final JdbcTemplate jdbcTemplate;

    // O Spring injetará automaticamente o JdbcTemplate
    public EntityDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper para mapear ResultSet para EntityDTO
    private final RowMapper<EntityDTO> rowMapper = (rs, rowNum) -> {
        EntityDTO entity = new EntityDTO();
        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        entity.setDescription(rs.getString("description"));
        entity.setCreatedDate(rs.getTimestamp("created_at").toLocalDateTime());
        entity.setUpdatedDate(rs.getTimestamp("updated_at").toLocalDateTime());
        entity.setType(rs.getString("type")); // Mapeia o novo campo 'type'
        entity.setOrigin(rs.getString("origin")); // Corrigido para 'origin'
        entity.setDestination(rs.getString("destination")); // Corrigido para 'destination'
        entity.setMaxTransfers(rs.getInt("max_transfers")); // Mapeia o novo campo 'max_transfers'
        entity.setDeadline(rs.getTimestamp("deadline").toLocalDateTime()); // Mapeia 'deadline'
        entity.setStatus(rs.getString("status"));
        return entity;
    };

    /**
     * Cria uma nova entrada na tabela UA.
     * @param entity O EntityDTO a ser criado.
     * @return O EntityDTO com o ID gerado pelo banco de dados.
     */
    public EntityDTO createEntity(EntityDTO entity) {
        String sql = "INSERT INTO UA (name, description, type, origin, destination, max_transfers, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, Objects.requireNonNullElse(entity.getType(), "NORMAL")); // Define 'NORMAL' como padrão se null
            ps.setString(4, entity.getOrigin());
            ps.setString(5, entity.getDestination());
            ps.setInt(6, Objects.requireNonNullElse(entity.getMaxTransfers(), 0)); // Define 0 como padrão se null
            ps.setTimestamp(7, Timestamp.valueOf(entity.getDeadline())); // Usar Timestamp.valueOf
            ps.setString(8, Objects.requireNonNullElse(entity.getStatus(), "created")); // Define 'created' como padrão se null
            return ps;
        }, keyHolder);

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return entity;
    }

    /**
     * Encontra uma entrada UA pelo seu ID.
     * @param id O ID da entidade a ser encontrada.
     * @return O EntityDTO correspondente, ou null se não for encontrado.
     */
    public EntityDTO findById(Integer id) {
        String sql = "SELECT id, name, description, created_at, updated_at, type, origin, destination, max_transfers, deadline, status FROM UA WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null; // Retorna null se nenhum resultado for encontrado
        }
    }

    /**
     * Retorna todas as entradas da tabela UA.
     * @return Uma lista de EntityDTOs.
     */
    public List<EntityDTO> findAll() {
        String sql = "SELECT id, name, description, created_at, updated_at, type, origin, destination, max_transfers, deadline, status FROM UA";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * Retorna todas as entradas da tabela UA com um status específico.
     * @param status O status a ser filtrado (e.g., "active", "completed", "created").
     * @return Uma lista de EntityDTOs com o status especificado.
     */
    public List<EntityDTO> findByStatus(String status) {
        String sql = "SELECT id, name, description, created_at, updated_at, type, origin, destination, max_transfers, deadline, status FROM UA WHERE status = ?";
        return jdbcTemplate.query(sql, new Object[]{status}, rowMapper);
    }


    /**
     * Atualiza uma entrada existente na tabela UA.
     * @param entity O EntityDTO com os dados atualizados.
     * @return O número de linhas afetadas (1 se atualizado com sucesso, 0 caso contrário).
     */
    public int updateEntity(EntityDTO entity) {
        String sql = "UPDATE UA SET name = ?, description = ?, type = ?, origin = ?, destination = ?, max_transfers = ?, deadline = ?, status = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                entity.getName(),
                entity.getDescription(),
                entity.getType(), // Atualiza o novo campo 'type'
                entity.getOrigin(),
                entity.getDestination(),
                entity.getMaxTransfers(), // Atualiza o novo campo 'max_transfers'
                Timestamp.valueOf(entity.getDeadline()), // Usar Timestamp.valueOf
                entity.getStatus(),
                entity.getId());
    }

    /**
     * Elimina uma entrada da tabela UA pelo seu ID.
     * @param id O ID da entidade a ser eliminada.
     * @return O número de linhas afetadas (1 se eliminado com sucesso, 0 caso contrário).
     */
    public int deleteEntity(Integer id) {
        String sql = "DELETE FROM UA WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * Função de depuração: Redefine o 'deadline' de todas as entidades UA
     * para a data atual, mantendo a hora original.
     * @return O número de linhas afetadas.
     */
    public int resetAllDeadlinesToNow() {
        List<EntityDTO> allEntities = findAll(); // Obter todas as entidades
        LocalDate today = LocalDate.now(); // Data atual
        int updatedCount = 0;

        String updateSql = "UPDATE UA SET deadline = ? WHERE id = ?";

        for (EntityDTO entity : allEntities) {
            LocalDateTime originalDeadline = entity.getDeadline();
            if (originalDeadline != null) {
                LocalTime originalTime = originalDeadline.toLocalTime(); // Obter a hora original
                LocalDateTime newDeadline = LocalDateTime.of(today, originalTime); // Combinar data atual com hora original
                updatedCount += jdbcTemplate.update(updateSql, Timestamp.valueOf(newDeadline), entity.getId());
            }
        }
        return updatedCount;
    }

    /**
     * Função de depuração: Redefine o 'deadline' de todas as entidades UA
     * para 23:55 da data atual.
     * @return O número de linhas afetadas.
     */
    public int resetAllDeadlinesToLastSlot() {
        List<EntityDTO> allEntities = findAll(); // Obter todas as entidades
        LocalDate today = LocalDate.now(); // Data atual
        int updatedCount = 0;

        String updateSql = "UPDATE UA SET data_chegada = ? WHERE id = ?"; // Use 'data_chegada' as per your DB schema

        for (EntityDTO entity : allEntities) {
            // Set the new deadline to 23:55 of the current day
            LocalDateTime newDeadline = LocalDateTime.of(today, LocalTime.of(23, 55));
            updatedCount += jdbcTemplate.update(updateSql, Timestamp.valueOf(newDeadline), entity.getId());
        }
        return updatedCount;
    }


}
