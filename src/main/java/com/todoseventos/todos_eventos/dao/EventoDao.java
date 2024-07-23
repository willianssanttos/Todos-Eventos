package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public EventoModel save(EventoModel evento) {
        String sql = "INSERT INTO EVENTO (nome_evento, dataHora_evento, descricao, id_categoria) VALUES (?,?,?,?) RETURNING id_evento";
        Long idEvento = jdbcTemplate.queryForObject(sql, Long.class, evento.getNome_evento(), evento.getDataHora_evento(), evento.getDescricao(), evento.getId_categoria());
        evento.setIdEvento(idEvento);
        return evento;
    }

    public EventoModel update(EventoModel evento) {
        String sql = "UPDATE EVENTO SET nome_evento = ?, dataHora_evento = ?, descricao = ?, id_categoria = ? WHERE id_evento = ?";
        jdbcTemplate.update(sql, evento.getNome_evento(), evento.getDataHora_evento(), evento.getDescricao(), evento.getId_categoria(), evento.getIdEvento());
        return evento;
    }

    public EventoModel procurarPorNome(String nomeEvento) {
        String sql = "SELECT * FROM EVENTO WHERE nome_evento = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), nomeEvento);
    }

    public List<EventoModel> localizarEvento() {
        String sql = "SELECT e.*, ed.rua, ed.numero, ed.bairro, ed.cidade, ed.cep, ed.uf FROM EVENTO e JOIN ENDERECO ed ON e.id_evento = ed.id_evento";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventoModel.class));
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar eventos: " + e.getMessage());
        }
    }
}
