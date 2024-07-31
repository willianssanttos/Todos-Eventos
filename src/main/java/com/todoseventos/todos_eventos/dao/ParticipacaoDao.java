package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface ParticipacaoDao {
    ParticipacaoModel save(ParticipacaoModel participacao);
    ParticipacaoModel update(ParticipacaoModel participacao);
    ParticipacaoModel findById(Long idParticipacao);

    List<ParticipacaoModel> findByIdEvento(Long idEvento);
}

@Repository
class ParticipacaoDaoImpl implements ParticipacaoDao {

    private static final Logger logger = LoggerFactory.getLogger(ParticipacaoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ParticipacaoModel save(ParticipacaoModel participacao) {
        String sql = "SELECT inserir_participacao(CAST(? AS VARCHAR), CAST(? AS VARCHAR), CAST(? AS INT), CAST(? AS VARCHAR))";
        try {
            Long idParticipacao = jdbcTemplate.queryForObject(sql, new Object[]{
                    participacao.getCpf(),
                    participacao.getCnpj(),
                    participacao.getIdEvento(),
                    participacao.getStatus()
            }, Long.class);

            participacao.setIdParticipacao(idParticipacao);
            logger.info("Participação salva com ID: {}", idParticipacao);
            return participacao;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar Participacao: " + e.getMessage());
        }
    }

    @Override
    public ParticipacaoModel update(ParticipacaoModel participacao) {
        String sql = "UPDATE PARTICIPACAO SET status = ? WHERE id_participacao = ?";
        logger.info("Executando SQL para atualizar participação: {}", sql);
        try {
            jdbcTemplate.update(sql, participacao.getStatus(), participacao.getIdParticipacao());
            logger.info("Participação atualizada com ID: {}", participacao.getIdParticipacao());
            return participacao;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar participação: " + e.getMessage());
        }
    }

    @Override
    public ParticipacaoModel findById(Long idParticipacao) {
        String sql = "SELECT * FROM PARTICIPACAO WHERE id_participacao = ?";
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
    public List<ParticipacaoModel> findByIdEvento(Long idEvento) {
        String sql = "SELECT * FROM PARTICIPACAO WHERE id_evento = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), idEvento);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar participações por ID do evento: " + e.getMessage());
        }
    }
}