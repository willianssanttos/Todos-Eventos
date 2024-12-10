package com.todoseventos.todos_eventos.repository.cliente.Impl;

import com.todoseventos.todos_eventos.repository.cliente.ClienteRepository;
import com.todoseventos.todos_eventos.exception.*;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import com.todoseventos.todos_eventos.utils.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImpl.class);

    @Override
    @Transactional
    public ClienteModel procurarPorCpf(String cpf) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_cliente_por_cpf(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cpf);
        } catch (DataAccessException e){
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarClienteNotFoundException();
        }
    }

    @Override
    @Transactional
    public ClienteModel procurarPorCnpj(String cnpj) {
        logger.info(Constantes.ErrorRecuperarContas);
        try {
            String sql = "SELECT * FROM procurar_cliente_por_cnpj(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cnpj);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarClienteNotFoundException();
        }
    }

    @Override
    @Transactional
    public ClienteModel procurarPorEmail(String email) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_cliente_por_email(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), email);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new EmailExistenteException();
        }
    }

    @Override
    @Transactional
    public ClienteModel salvarCliente(ClienteModel pessoa) {
        logger.info(Constantes.DebugRegistroProcesso);
        try {
            String sql = "SELECT inserir_cliente(?, ?, ?, ?, ?)";
            Integer idPessoa = jdbcTemplate.queryForObject(sql, new Object[]{
                    pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa()
            }, Integer.class);
            pessoa.setIdPessoa(idPessoa);
            return pessoa;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new ClienteException();
        }
    }

    @Override
    @Transactional
    public ClienteModel atualizarCliente(ClienteModel pessoa) {
        logger.info(Constantes.DebugEditarProcesso);
        try {
            String sql = "SELECT atualizar_cliente(?, ?, ?, ?, ?, ?)";
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
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new AtualizarContaException();
        }
    }

    @Override
    @Transactional
    public List<ClienteModel> listarTodasPessoas() {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM listar_todos_clientes()";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ClienteModel.class));
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarClienteNotFoundException();
        }
    }
}
