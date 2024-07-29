package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteJuridicaModel;
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
}

@Repository
class ParticipacaoDaoImpl implements ParticipacaoDao {

    private static final Logger logger = LoggerFactory.getLogger(ParticipacaoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ParticipacaoModel save(ParticipacaoModel participacao) {
        String sql = "INSERT INTO PARTICIPACAO (cpf, cnpj, id_evento, status) VALUES (?,?,?,?) RETURNING id_participacao";
        logger.info("Executando SQL para salvar participação: {}", sql);
        Long idParticipacao = jdbcTemplate.queryForObject(sql, Long.class, participacao.getCpf(), participacao.getCnpj(), participacao.getIdEvento(), participacao.getStatus());
        participacao.setIdParticipacao(idParticipacao);
        logger.info("Participação salva com ID: {}", idParticipacao);
        return participacao;
    }

    @Override
    public ParticipacaoModel update(ParticipacaoModel participacao) {
        String sql = "UPDATE PARTICIPACAO SET status = ? WHERE id_participacao = ?";
        logger.info("Executando SQL para atualizar participação: {}", sql);
        jdbcTemplate.update(sql, participacao.getStatus(), participacao.getIdParticipacao());
        logger.info("Participação atualizada com ID: {}", participacao.getIdParticipacao());
        return participacao;
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
        }
    }
}