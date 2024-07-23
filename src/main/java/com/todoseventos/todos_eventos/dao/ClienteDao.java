package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.ClienteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ClienteModel procurarPorCpf(String cpf) {
        try {
            String sql = "SELECT p.id_pessoa, p.nome, p.email, p.senha, p.telefone, tp.nome_tipo_pessoa, pf.cpf, pf.datanascimento " +
                    "FROM PESSOA p " +
                    "LEFT JOIN PESSOA_FISICA pf ON p.id_pessoa = pf.id_pessoa " +
                    "LEFT JOIN TIPO_PESSOA tp ON p.id_tipo_pessoa = tp.id_tipo_pessoa " +
                    "WHERE pf.cpf = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cpf);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ClienteModel procurarPorCnpj(String cnpj) {
        try {
            String sql = "SELECT p.id_pessoa, p.nome, p.email, p.senha, p.telefone, tp.nome_tipo_pessoa, pj.cnpj " +
                    "FROM PESSOA p " +
                    "LEFT JOIN PESSOA_JURIDICA pj ON p.id_pessoa = pj.id_pessoa " +
                    "LEFT JOIN TIPO_PESSOA tp ON p.id_tipo_pessoa = tp.id_tipo_pessoa " +
                    "WHERE pj.cnpj = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteModel.class), cnpj);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ClienteModel save(ClienteModel pessoa) {
        String sql = "INSERT INTO PESSOA (nome, email, senha, telefone, id_tipo_pessoa) VALUES (?,?,?,?,?) RETURNING id_pessoa";
        Long idPessoa = jdbcTemplate.queryForObject(sql, new Object[] { pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa() }, Long.class);
        pessoa.setIdPessoa(idPessoa);
        return pessoa;
    }

    public ClienteModel update(ClienteModel pessoa){
        String sql = "UPDATE PESSOA SET nome = ?, email = ?, senha = ?, telefone = ?, id_tipo_pessoa = ? WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa(), pessoa.getIdPessoa());
        return pessoa;
    }

    public List<ClienteModel> listarTodasPessoas(){
        String sql = "SELECT p.id_pessoa, p.nome, p.email, p.senha, p.telefone, tp.nome_tipo_pessoa, pf.cpf, pf.datanascimento, pj.cnpj " +
                "FROM PESSOA p " +
                "LEFT JOIN TIPO_PESSOA tp ON p.id_tipo_pessoa = tp.id_tipo_pessoa " +
                "LEFT JOIN PESSOA_FISICA pf ON p.id_pessoa = pf.id_pessoa " +
                "LEFT JOIN PESSOA_JURIDICA pj ON p.id_pessoa = pj.id_pessoa";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ClienteModel.class));
    }
}


