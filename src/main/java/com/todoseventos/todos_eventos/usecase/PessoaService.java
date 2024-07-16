package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.PessoaDao;
import com.todoseventos.todos_eventos.dao.PessoaFisicaDao;
import com.todoseventos.todos_eventos.dao.PessoaJuridicaDao;
import com.todoseventos.todos_eventos.dao.TipoPessoaDao;
import com.todoseventos.todos_eventos.dto.PessoaRequest;
import com.todoseventos.todos_eventos.dto.PessoaResponse;
import com.todoseventos.todos_eventos.dto.TipoPessoaEnum;
import com.todoseventos.todos_eventos.model.pessoa.PessoaFisicaModel;
import com.todoseventos.todos_eventos.model.pessoa.PessoaJuridicaModel;
import com.todoseventos.todos_eventos.model.pessoa.PessoaModel;
import com.todoseventos.todos_eventos.model.pessoa.TipoPessoaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PessoaService {

    @Autowired
    private PessoaDao pessoaDao;

    @Autowired
    private TipoPessoaDao tipoPessoaDao;

    @Autowired
    private PessoaFisicaDao pessoaFisicaDao;

    @Autowired
    private PessoaJuridicaDao pessoaJuridicaDao;

    public PessoaResponse cadastrarNovaPessoa(PessoaRequest pessoaRequest) {
        if (pessoaRequest.getTipo_pessoa() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuário inválido!");
        }

        TipoPessoaModel tipoPessoaModel = tipoPessoaDao.findByNomeTipoPessoa(pessoaRequest.getTipo_pessoa().name());

        if (Objects.isNull(tipoPessoaModel)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de pessoa inválido!");
        }

        PessoaModel pessoa = PessoaModel.builder()
                .nome(pessoaRequest.getNome())
                .email(pessoaRequest.getEmail())
                .senha(pessoaRequest.getSenha())
                .telefone(pessoaRequest.getTelefone())
                .idPessoa(pessoaRequest.getIdPessoa())
                .cpf(pessoaRequest.getCpf())
                .cnpj(pessoaRequest.getCnpj())
                .dataNascimento(pessoaRequest.getDataNascimento())
                .tipo_pessoa(tipoPessoaModel.getIdTipoPessoa())
                .build();

        PessoaModel pessoaSalva = pessoaDao.save(pessoa);

        if (pessoaRequest.getTipo_pessoa() == TipoPessoaEnum.FISICA){
            PessoaFisicaModel pessoaFisica = PessoaFisicaModel.builder()
                    .cpf(pessoaRequest.getCpf())
                    .dataNascimento(pessoaRequest.getDataNascimento())
                    .idPessoa(pessoaSalva.getIdPessoa().intValue())
                    .build();
            pessoaFisicaDao.save(pessoaFisica);

        } else if (pessoaRequest.getTipo_pessoa() == TipoPessoaEnum.JURIDICA) {
            PessoaJuridicaModel pessoaJuridica = PessoaJuridicaModel.builder()
                    .cnpj(pessoaRequest.getCnpj())
                    .idPessoa(pessoaSalva.getIdPessoa().intValue())
                    .build();
            pessoaJuridicaDao.save(pessoaJuridica);
        }
        return mapearPessoa(pessoaRequest.getTipo_pessoa(), pessoaSalva);
    }

    private static PessoaResponse mapearPessoa(TipoPessoaEnum tipo_pessoa, PessoaModel pessoaSalva){
        return PessoaResponse.builder()
                .nome(pessoaSalva.getNome())
                .email(pessoaSalva.getEmail())
                .telefone(pessoaSalva.getTelefone())
                .tipo_pessoa(tipo_pessoa)
                .idPessoa(pessoaSalva.getIdPessoa())
                .cpf(pessoaSalva.getCpf())
                .dataNascimento(pessoaSalva.getDataNascimento())
                .cnpj(pessoaSalva.getCnpj())
                .build();
    }

    public PessoaResponse procurarPessoaPorCpf(String cpf){
        PessoaFisicaModel pessoaFisicaEncontrada = pessoaFisicaDao.findByCpf(cpf);

        if (Objects.isNull(pessoaFisicaEncontrada)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa fisica não encontrada");
        }

        PessoaModel pessoa = pessoaDao.procurarPorId(Long.valueOf(pessoaFisicaEncontrada.getIdPessoa()));

        return mapearPessoa(TipoPessoaEnum.FISICA, pessoa);
    }

    public PessoaResponse procurarPessoaPorCnpj(String cnpj){
        PessoaJuridicaModel pessoaJuridicaEncontrada = pessoaJuridicaDao.findByCnpj(cnpj);

        if (Objects.isNull(pessoaJuridicaEncontrada)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa jurídica não encontrada");
        }

        PessoaModel pessoa = pessoaDao.procurarPorId(pessoaJuridicaEncontrada.getIdPessoa().longValue());

        return mapearPessoa(TipoPessoaEnum.JURIDICA, pessoa);
    }

    public List<PessoaResponse> listarPessoas(){
        List<PessoaModel> pessoasEncontradas = pessoaDao.findAll();
        List<PessoaResponse> pessoaResponse = new ArrayList<>();

        for (PessoaModel pessoa : pessoasEncontradas){
            TipoPessoaEnum tipoPessoa;
            if (pessoaFisicaDao.findById(pessoa.getIdPessoa())){
                tipoPessoa = TipoPessoaEnum.FISICA;
            } else if (pessoaJuridicaDao.findById(pessoa.getIdPessoa())) {
                tipoPessoa = TipoPessoaEnum.JURIDICA;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Tipo de pessoa não encontrado");
            }
            pessoaResponse.add(mapearPessoa(tipoPessoa, pessoa));
        }

        return pessoaResponse;
    }

    public PessoaResponse atualizarPessoa(Long idPessoa, PessoaRequest pessoaRequest){
        PessoaModel pessoaExistente = pessoaDao.procurarPorId(idPessoa);

        if (Objects.isNull(pessoaExistente)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada!");
        }

        pessoaExistente.setNome(pessoaRequest.getNome());
        pessoaExistente.setEmail(pessoaRequest.getEmail());
        pessoaExistente.setSenha(pessoaRequest.getSenha());
        pessoaExistente.setTelefone(pessoaRequest.getTelefone());

        pessoaDao.update(pessoaExistente);

        if (pessoaRequest.getTipo_pessoa() == TipoPessoaEnum.FISICA){
            PessoaFisicaModel pessoaFisica = pessoaFisicaDao.findByCpf(pessoaRequest.getCpf());
            pessoaFisica.setDataNascimento(pessoaRequest.getDataNascimento());
            pessoaFisicaDao.update(pessoaFisica);
        } else if (pessoaRequest.getTipo_pessoa() == TipoPessoaEnum.JURIDICA) {
            PessoaJuridicaModel pessoaJuridica = pessoaJuridicaDao.findByCnpj(pessoaRequest.getCnpj());
            pessoaJuridicaDao.update(pessoaJuridica);

        }

        return mapearPessoa(pessoaRequest.getTipo_pessoa(), pessoaExistente);
    }

}
