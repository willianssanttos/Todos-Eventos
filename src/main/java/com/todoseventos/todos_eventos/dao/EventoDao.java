package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EventoDao {
    EventoModel save(EventoModel evento);
    EventoModel update(EventoModel evento);
    EventoModel procurarPorNome(String nomeEvento);
    EventoModel procurarPorId(Long idEvento);
    List<EventoModel> localizarEvento();
    void deleteById(Long idEvento);
}
@Repository
class EventoDaoImpl implements EventoDao {

    private static final Logger logger = LoggerFactory.getLogger(EventoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public EventoModel save(EventoModel evento) {
        String sql = "SELECT inserir_evento(?, ?, ?, ?, ?, ?) AS id_evento";
        logger.info("Executando SQL para salvar evento: {}", sql);
        try {
            Long idEvento = jdbcTemplate.queryForObject(sql, new Object[]{
                    evento.getNome_evento().trim(),
                    evento.getDataHora_evento(),
                    evento.getDataHora_eventofinal(),
                    evento.getDescricao().trim(),
                    evento.getStatus().trim(),
                    evento.getId_categoria()
            }, Long.class);
            evento.setIdEvento(idEvento);
            return evento;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar evento: " + e.getMessage());
        }
    }

    @Override
    public EventoModel update(EventoModel evento) {
        String sql = "UPDATE EVENTO SET nome_evento = ?, dataHora_evento = ?, dataHora_eventofinal = ?, descricao = ?, status = ?, id_categoria = ? WHERE id_evento = ?";
        try {
            jdbcTemplate.update(sql, evento.getNome_evento(), evento.getDataHora_evento(), evento.getDataHora_eventofinal(), evento.getDescricao(), evento.getStatus(), evento.getId_categoria(), evento.getIdEvento());
            return evento;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar evento: " + e.getMessage());
        }
    }

    @Override
    public EventoModel procurarPorNome(String nomeEvento) {
        String sql = "SELECT * FROM EVENTO WHERE nome_evento = ?";
        logger.info("Executando SQL para buscar evento por nome: {}", sql);
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), nomeEvento);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Evento não encontrado com nome: " + nomeEvento);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar evento por nome: " + e.getMessage());
        }
    }

    @Override
    public EventoModel procurarPorId(Long idEvento) {
        String sql = "SELECT * FROM EVENTO WHERE id_evento = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), idEvento);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Evento não encontrado com ID: " + idEvento);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar evento por ID: " + e.getMessage());
        }
    }

    @Override
    public List<EventoModel> localizarEvento() {
        String sql = "SELECT e.*, ed.rua, ed.numero, ed.bairro, ed.cidade, ed.cep, ed.uf FROM EVENTO e JOIN ENDERECO ed ON e.id_evento = ed.id_evento";
        logger.info("Executando SQL para buscar evento: {}", sql);
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventoModel.class));
        } catch (Exception e) {
            throw new CustomException("Erro ao listar eventos: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long idEvento) {
        String sql = "DELETE FROM EVENTO WHERE id_evento = ?";
        try {
            jdbcTemplate.update(sql, idEvento);
        } catch (Exception e) {
            throw new CustomException("Erro ao deletar evento: " + e.getMessage());
        }
    }
}