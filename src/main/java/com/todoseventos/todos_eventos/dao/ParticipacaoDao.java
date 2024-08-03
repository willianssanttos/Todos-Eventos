package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface ParticipacaoDao {
    ParticipacaoModel save(ParticipacaoModel participacao);
    ParticipacaoModel update(ParticipacaoModel participacao);
    ParticipacaoModel findById(Integer idParticipacao);
    List<ParticipacaoModel> findByIdEvento(Integer idEvento);
}

@Repository
class ParticipacaoDaoImpl implements ParticipacaoDao {

    private static final Logger logger = LoggerFactory.getLogger(ParticipacaoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ParticipacaoModel save(ParticipacaoModel participacao) {
        String sql = "SELECT inserir_participacao(?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, participacao);
                ps.execute();
                return null;
            });
            Integer idParticipacao = jdbcTemplate.queryForObject("SELECT currval(pg_get_serial_sequence('participacao','id_participacao'))", Integer.class);
            participacao.setIdParticipacao(idParticipacao);
            logger.info("Participação salva com ID: {}", idParticipacao);
            return participacao;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar Participacao: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ParticipacaoModel update(ParticipacaoModel participacao) {
        String sql = "SELECT atualizar_participacao(?, ?)";
        logger.info("Executando SQL para atualizar participação: {}", sql);
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, participacao.getIdParticipacao());
                ps.setString(2, participacao.getStatus());
                ps.execute();
                return null;
            });
            logger.info("Participação atualizada com ID: {}", participacao.getIdParticipacao());
            return participacao;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar participação: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ParticipacaoModel findById(Integer idParticipacao) {
        String sql = "SELECT * FROM procurar_participacao_por_id(?)";
        logger.info("Executando SQL para buscar participação por ID: {}", sql);
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), idParticipacao);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Nenhuma participação encontrada com ID: {}", idParticipacao);
            return null;
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar participação por ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ParticipacaoModel> findByIdEvento(Integer idEvento) {
        String sql = "SELECT * FROM procurar_participacoes_por_id_evento(?)";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), idEvento);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar participações por ID do evento: " + e.getMessage());
        }
    }

    private void setPreparedStatementParameters(PreparedStatement ps, ParticipacaoModel participacao) throws SQLException {
        ps.setString(1, participacao.getCpf());
        ps.setString(2, participacao.getCnpj());
        ps.setInt(3, participacao.getIdEvento());
        ps.setString(4, participacao.getStatus());
    }
}
