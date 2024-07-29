package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EnderecoDao {
    EnderecoModel save(EnderecoModel endereco);
    EnderecoModel update(EnderecoModel endereco);
    Optional<EnderecoModel> procurarPorIdEvento(Long id);
    void deleteByIdEvento(Long idEvento);
}

@Repository
class EnderecoDaoImpl implements EnderecoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public EnderecoModel save(EnderecoModel endereco) {
        String sql = "INSERT INTO ENDERECO (rua, numero, bairro, cidade, uf, cep, id_evento) VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, endereco.getRua(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade(), endereco.getUf(), endereco.getCep(), endereco.getIdEvento());
        return endereco;
    }

    @Override
    public EnderecoModel update(EnderecoModel endereco) {
        String sql = "UPDATE ENDERECO SET rua = ?, numero = ?, bairro = ?, cidade = ?, uf = ?, cep = ? WHERE id_evento = ?";
        jdbcTemplate.update(sql, endereco.getRua(), endereco.getNumero(), endereco.getBairro(), endereco.getCidade(), endereco.getUf(), endereco.getCep(), endereco.getIdEvento());
        return endereco;
    }

    @Override
    public Optional<EnderecoModel> procurarPorIdEvento(Long id) {
        String sql = "SELECT * FROM ENDERECO WHERE id_evento = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EnderecoModel.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar endereço por ID: " + e.getMessage());
        }
    }

    @Override
    public void deleteByIdEvento(Long idEvento) {
        String sql = "DELETE FROM ENDERECO WHERE id_evento = ?";
        jdbcTemplate.update(sql, idEvento);
    }
}