package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
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
import java.util.Optional;

public interface EnderecoDao {
    EnderecoModel save(EnderecoModel endereco);
    EnderecoModel update(EnderecoModel endereco);
    Optional<EnderecoModel> procurarPorIdEvento(Integer id);
    void deleteByIdEvento(Integer idEvento);
}

@Repository
class EnderecoDaoImpl implements EnderecoDao {

    private static final Logger logger = LoggerFactory.getLogger(EnderecoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public EnderecoModel save(EnderecoModel endereco) {
        String sql = "SELECT inserir_endereco(?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, endereco);
                ps.execute();
                return null;
            });
            return endereco;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar endereço: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public EnderecoModel update(EnderecoModel endereco) {
        String sql = "SELECT atualizar_endereco(?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, endereco);
                ps.execute();
                return null;
            });
            return endereco;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<EnderecoModel> procurarPorIdEvento(Integer id) {
        String sql = "SELECT * FROM procurar_endereco_por_id_evento(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EnderecoModel.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar endereço por ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteByIdEvento(Integer idEvento) {
        String sql = "SELECT deletar_endereco(?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, idEvento);
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            throw new CustomException("Erro ao deletar endereço: " + e.getMessage());
        }
    }

    private void setPreparedStatementParameters(PreparedStatement ps, EnderecoModel endereco) throws SQLException {
        ps.setString(1, endereco.getRua());
        ps.setString(2, endereco.getNumero());
        ps.setString(3, endereco.getBairro());
        ps.setString(4, endereco.getCidade());
        ps.setString(5, endereco.getUf());
        ps.setString(6, endereco.getCep());
        ps.setInt(7, endereco.getIdEvento());
    }
}
