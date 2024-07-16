/*package com.todoseventos.todos_eventos.configuration.tipopessoa;

import com.todoseventos.todos_eventos.dao.TipoPessoaDao;
import com.todoseventos.todos_eventos.dto.TipoPessoaEnum;
import com.todoseventos.todos_eventos.model.pessoa.TipoPessoaModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TipoPessoaPersistenciaConfiguration {

    @Autowired
    private TipoPessoaDao tipoPessoaDao;

    @PostConstruct
    public void iniciarTipoPessoaTabela(){
        TipoPessoaModel pessoaFisica = TipoPessoaModel.builder()
                .nomeTipoPessoa("FiSICA")
                .build();
        TipoPessoaModel pessoaJuridica = TipoPessoaModel.builder()
                .nomeTipoPessoa("JURIDICA")
                .build();

        tipoPessoaDao.save(pessoaFisica);
    }
}
*/