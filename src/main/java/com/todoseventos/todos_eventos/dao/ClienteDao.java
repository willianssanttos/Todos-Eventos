package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ClienteDao {
    ClienteModel procurarPorCpf(String cpf);
    ClienteModel procurarPorCnpj(String cnpj);
    ClienteModel procurarPorEmail(String email);
    ClienteModel salvarCliente(ClienteModel pessoa);
    ClienteModel atualizarCliente(ClienteModel pessoa);
    List<ClienteModel> listarTodasPessoas();
}
@Repository
class ClienteDaoImpl implements ClienteDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ClienteModel procurarPorCpf(String cpf) {
        String sql = "SELECT * FROM procurar_cliente_por_cpf(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cpf);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_CLIENTE_CPF + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClienteModel procurarPorCnpj(String cnpj) {
        String sql = "SELECT * FROM procurar_cliente_por_cnpj(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cnpj);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_CLIENTE_CNPJ + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClienteModel procurarPorEmail(String email) {
        String sql = "SELECT * FROM procurar_cliente_por_email(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), email);
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_USUARIO_POR_EMAIL + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClienteModel salvarCliente(ClienteModel pessoa) {
        String sql = "SELECT inserir_cliente(?, ?, ?, ?, ?)";
        try {
            Integer idPessoa = jdbcTemplate.queryForObject(sql, new Object[]{
                    pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa()
            }, Integer.class);
            pessoa.setIdPessoa(idPessoa);
            return pessoa;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_SALVAR_CLIENTE + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClienteModel atualizarCliente(ClienteModel pessoa) {
        String sql = "SELECT atualizar_cliente(?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoa.getIdPessoa());
                ps.setString(2, pessoa.getNome());
                ps.setString(3, pessoa.getEmail());
                ps.setString(4, pessoa.getSenha());
                ps.setString(5, pessoa.getTelefone());
                ps.setInt(6, pessoa.getTipo_pessoa());
                ps.execute();
                return null;
            });
            return pessoa;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_ATUALIZAR_CLIENTE + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ClienteModel> listarTodasPessoas() {
        String sql = "SELECT * FROM listar_todos_clientes()";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ClienteModel.class));
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_LISTAR_TODOS + e.getMessage());
        }
    }
}
