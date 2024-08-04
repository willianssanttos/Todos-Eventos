package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
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
    EnderecoModel salverEndereco(EnderecoModel endereco);
    EnderecoModel atualizarEndereco(EnderecoModel endereco);
    Optional<EnderecoModel> procurarPorIdEvento(Integer id);
    void deletarPorIdEvento(Integer idEvento);
}

@Repository
class EnderecoDaoImpl implements EnderecoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public EnderecoModel salverEndereco(EnderecoModel endereco) {
        String sql = "SELECT inserir_endereco(?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, endereco);
                ps.execute();
                return null;
            });
            return endereco;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_SALVAR + e.getMessage());
        }
    }


    @Override
    @Transactional
    public EnderecoModel atualizarEndereco(EnderecoModel endereco) {
        String sql = "SELECT atualizar_endereco(?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, endereco);
                ps.execute();
                return null;
            });
            return endereco;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_ATUALIZAR + e.getMessage());
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
            throw new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deletarPorIdEvento(Integer idEvento) {
        String sql = "SELECT deletar_endereco(?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, idEvento);
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_EXCLUIR + e.getMessage());
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
