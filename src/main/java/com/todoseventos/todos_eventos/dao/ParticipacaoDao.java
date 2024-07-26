package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParticipacaoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ParticipacaoModel save(ParticipacaoModel participacao) {
        String sql = "INSERT INTO PARTICIPACAO (cpf, cnpj, id_evento, status) VALUES (?,?,?,?) RETURNING id_participacao";
        Long idParticipacao = jdbcTemplate.queryForObject(sql, Long.class, participacao.getCpf(), participacao.getCnpj(), participacao.getIdEvento(), participacao.getStatus());
        participacao.setIdParticipacao(idParticipacao);
        return participacao;
    }

    public ParticipacaoModel update(ParticipacaoModel participacao) {
        String sql = "UPDATE PARTICIPACAO SET status = ? WHERE id_participacao = ?";
        jdbcTemplate.update(sql, participacao.getStatus(), participacao.getIdParticipacao());
        return participacao;
    }

    public ParticipacaoModel findById(Long idParticipacao) {
        String sql = "SELECT * FROM PARTICIPACAO WHERE id_participacao = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), idParticipacao);
    }

    public List<ParticipacaoModel> findAll() {
        String sql = "SELECT * FROM PARTICIPACAO";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class));
    }

    public ParticipacaoModel findByCpfAndEvento(String cpf, Long idEvento) {
        String sql = "SELECT * FROM PARTICIPACAO WHERE cpf = ? AND id_evento = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), cpf, idEvento);
        } catch (Exception e) {
            return null;
        }
    }

    public ParticipacaoModel findByCnpjAndEvento(String cnpj, Long idEvento) {
        String sql = "SELECT * FROM PARTICIPACAO WHERE cnpj = ? AND id_evento = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), cnpj, idEvento);
        } catch (Exception e) {
            return null;
        }
    }
}
