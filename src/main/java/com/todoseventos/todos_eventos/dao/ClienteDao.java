package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ClienteDao {

    private static final Logger logger = LoggerFactory.getLogger(ClienteDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ClienteModel procurarPorCpf(String cpf) {
        String sql = "SELECT p.*, pf.cpf, pf.dataNascimento, tp.nome_tipo_pessoa FROM PESSOA p " +
                "LEFT JOIN PESSOA_FISICA pf ON p.id_pessoa = pf.id_pessoa " +
                "LEFT JOIN TIPO_PESSOA tp ON p.id_tipo_pessoa = tp.id_tipo_pessoa " +
                "WHERE pf.cpf = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cpf);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar cliente por CPF: " + e.getMessage());
        }
    }

    public ClienteModel procurarPorCnpj(String cnpj) {
        String sql = "SELECT p.*, pj.cnpj, tp.nome_tipo_pessoa FROM PESSOA p " +
                "LEFT JOIN PESSOA_JURIDICA pj ON p.id_pessoa = pj.id_pessoa " +
                "LEFT JOIN TIPO_PESSOA tp ON p.id_tipo_pessoa = tp.id_tipo_pessoa " +
                "WHERE pj.cnpj = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cnpj);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar cliente por CNPJ: " + e.getMessage());
        }
    }

    public ClienteModel procurarPorEmail(String email) {
        String sql = "SELECT * FROM PESSOA WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar usuário por email: " + e.getMessage());
        }
    }


    public ClienteModel save(ClienteModel pessoa) {
        String sql = "INSERT INTO PESSOA (nome, email, senha, telefone, id_tipo_pessoa) VALUES (?,?,?,?,?) RETURNING id_pessoa";
        try {
            Long idPessoa = jdbcTemplate.queryForObject(sql, new Object[]{pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa()}, Long.class);
            pessoa.setIdPessoa(idPessoa);
            return pessoa;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public ClienteModel update(ClienteModel pessoa) {
        String sql = "UPDATE PESSOA SET nome = ?, email = ?, senha = ?, telefone = ?, id_tipo_pessoa = ? WHERE id_pessoa = ?";
        try {
            jdbcTemplate.update(sql, pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa(), pessoa.getIdPessoa());
            return pessoa;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public List<ClienteModel> listarTodasPessoas() {
        String sql = "SELECT p.id_pessoa, p.nome, p.email, p.senha, p.telefone, tp.nome_tipo_pessoa, pf.cpf, pf.datanascimento, pj.cnpj " +
                "FROM pessoa p " +
                "LEFT JOIN tipo_pessoa tp ON p.id_tipo_pessoa = tp.id_tipo_pessoa " +
                "LEFT JOIN pessoa_fisica pf ON p.id_pessoa = pf.id_pessoa " +
                "LEFT JOIN pessoa_juridica pj ON p.id_pessoa = pj.id_pessoa";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ClienteModel.class));
        } catch (Exception e) {
            throw new CustomException("Erro ao listar todas as pessoas: " + e.getMessage());
        }
    }
}