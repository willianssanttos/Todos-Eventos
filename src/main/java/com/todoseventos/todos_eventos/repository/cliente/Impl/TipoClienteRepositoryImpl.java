package com.todoseventos.todos_eventos.repository.cliente.Impl;

import com.todoseventos.todos_eventos.mapper.TipoClienteRowMapper;
import com.todoseventos.todos_eventos.repository.cliente.TipoClienteRepository;
import com.todoseventos.todos_eventos.exception.BuscarClienteNotFoundException;
import com.todoseventos.todos_eventos.model.cliente.TipoClienteModel;
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
public class TipoClienteRepositoryImpl implements TipoClienteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(TipoClienteRepositoryImpl.class);

    @Override
    @Transactional
    public TipoClienteModel buscarPorNomeTipoPessoa(String nomeTipoPessoa) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_tipo_cliente_por_nome(?)";
            return jdbcTemplate.queryForObject(sql, new Object[] { nomeTipoPessoa }, new TipoClienteRowMapper());
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarClienteNotFoundException();
        }
    }
}
