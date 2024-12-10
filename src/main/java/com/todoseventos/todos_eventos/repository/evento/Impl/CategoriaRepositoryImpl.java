package com.todoseventos.todos_eventos.repository.evento.Impl;

import com.todoseventos.todos_eventos.repository.evento.CategoriaRepository;
import com.todoseventos.todos_eventos.exception.TipoCategoriaValidacaoException;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import com.todoseventos.todos_eventos.utils.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CategoriaRepositoryImpl implements CategoriaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(CategoriaRepositoryImpl.class);

    @Override
    @Transactional
    public CategoriaModel procurarId(Integer idCategoria) {
        logger.info(Constantes.DebugBuscarProcesso);

        try {
            String sql = "SELECT * FROM procurar_categoria_por_id(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CategoriaModel.class), idCategoria);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new TipoCategoriaValidacaoException();
        }
    }

    @Override
    @Transactional
    public CategoriaModel buscarNomeCategoria(String nomeCategoria) {
        logger.info(Constantes.DebugBuscarProcesso);

        try {
            String sql = "SELECT * FROM procurar_categoria_por_nome(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CategoriaModel.class), nomeCategoria);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new TipoCategoriaValidacaoException();
        }
    }
}

