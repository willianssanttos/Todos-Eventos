package com.todoseventos.todos_eventos.mapper;

import com.todoseventos.todos_eventos.model.cliente.TipoClienteModel;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoClienteRowMapper implements RowMapper<TipoClienteModel> {

    public TipoClienteModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        TipoClienteModel tipoCliente = new TipoClienteModel();
        tipoCliente.setIdTipoPessoa(rs.getInt(1));
        tipoCliente.setNomeTipoPessoa(rs.getString(2));
        return tipoCliente;
    }
}
