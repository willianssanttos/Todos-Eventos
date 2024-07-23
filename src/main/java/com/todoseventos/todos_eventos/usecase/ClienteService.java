package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.ClienteDao;
import com.todoseventos.todos_eventos.dao.ClienteFisicaDao;
import com.todoseventos.todos_eventos.dao.ClienteJuridicaDao;
import com.todoseventos.todos_eventos.dao.TipoClienteDao;
import com.todoseventos.todos_eventos.dto.ClienteRequest;
import com.todoseventos.todos_eventos.dto.ClienteResponse;
import com.todoseventos.todos_eventos.dto.TipoClienteEnum;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.pessoa.ClienteFisicaModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteJuridicaModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteModel;
import com.todoseventos.todos_eventos.model.pessoa.TipoClienteModel;
import com.todoseventos.todos_eventos.security.PasswordSecurity;
import com.todoseventos.todos_eventos.utils.Validacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClienteService {

    @Autowired
    private Validacao validacao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private TipoClienteDao tipoClienteDao;

    @Autowired
    private ClienteFisicaDao clienteFisicaDao;

    @Autowired
    private ClienteJuridicaDao clienteJuridicaDao;

    public ClienteResponse cadastrarNovaPessoa(ClienteRequest clienteRequest) {
        if (clienteRequest.getTipo_pessoa() == null) {
            throw new CustomException("Tipo de usuário inválido!");
        }

        TipoClienteModel tipoClienteModel = tipoClienteDao.findByNomeTipoPessoa(clienteRequest.getTipo_pessoa().name());

        if (Objects.isNull(tipoClienteModel)){
            throw new CustomException("Tipo de pessoa inválido!");
        }

        if (!validacao.validarEmail(clienteRequest.getEmail())) {
            throw new CustomException("Email inválido!");
        }

        if (!validacao.validarNumeroTelefone(clienteRequest.getTelefone())) {
            throw new CustomException("Número de celular inválido!");
        }

        // Validar data de nascimento apenas para pessoa física
        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA &&
                !validacao.validarDataNascimento(clienteRequest.getDataNascimento())) {
            throw new CustomException("Data de nascimento inválida!");
        }

        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA) {
            if (!validacao.isCpfValid(clienteRequest.getCpf())) {
                throw new CustomException("CPF inválido!");
            }

            ClienteModel pessoaExistente = clienteDao.procurarPorCpf(clienteRequest.getCpf());
            if (pessoaExistente != null) {
                throw new CustomException("CPF já cadastrado!");
            }

        } else if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.JURIDICA) {
            if (!validacao.isCnpjValid(clienteRequest.getCnpj())) {
                throw new CustomException("CNPJ inválido!");
            }

            ClienteModel pessoaExistente = clienteDao.procurarPorCnpj(clienteRequest.getCnpj());
            if (pessoaExistente != null) {
                throw new CustomException("CNPJ já cadastrado!");
            }
        }

        clienteRequest.setTelefone(validacao.formatarNumeroTelefone(clienteRequest.getTelefone()));
        String tokenSenha = PasswordSecurity.generateToken();

        ClienteModel pessoa = ClienteModel.builder()
                .nome(clienteRequest.getNome())
                .email(clienteRequest.getEmail())
                .senha(tokenSenha)
                .telefone(clienteRequest.getTelefone())
                .tipo_pessoa(tipoClienteModel.getIdTipoPessoa())
                .build();

        ClienteModel pessoaSalva = clienteDao.save(pessoa);

        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA){
            ClienteFisicaModel pessoaFisica = ClienteFisicaModel.builder()
                    .cpf(clienteRequest.getCpf())
                    .dataNascimento(clienteRequest.getDataNascimento())
                    .idPessoa(pessoaSalva.getIdPessoa())
                    .build();
            clienteFisicaDao.save(pessoaFisica);

        } else if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.JURIDICA) {
            ClienteJuridicaModel pessoaJuridica = ClienteJuridicaModel.builder()
                    .cnpj(clienteRequest.getCnpj())
                    .idPessoa(pessoaSalva.getIdPessoa())
                    .build();
            clienteJuridicaDao.save(pessoaJuridica);
        }
        return mapearPessoa(clienteRequest.getTipo_pessoa(), pessoaSalva);
    }

    private static ClienteResponse mapearPessoa(TipoClienteEnum tipo_pessoa, ClienteModel pessoaSalva){
        ClienteResponse.ClienteResponseBuilder builder = ClienteResponse.builder()
                .nome(pessoaSalva.getNome())
                .email(pessoaSalva.getEmail())
                .senha(pessoaSalva.getSenha())
                .telefone(pessoaSalva.getTelefone())
                .tipo_pessoa(tipo_pessoa)
                .idPessoa(pessoaSalva.getIdPessoa());

        if (tipo_pessoa == TipoClienteEnum.FISICA) {
            builder.cpf(pessoaSalva.getCpf())
                    .dataNascimento(pessoaSalva.getDataNascimento());

        } else if (tipo_pessoa == TipoClienteEnum.JURIDICA) {
            builder.cnpj(pessoaSalva.getCnpj());
        }
        return builder.build();
    }

    public ClienteResponse procurarPessoaPorCpf(String cpf) {
        ClienteModel pessoaFisicaEncontrada = clienteDao.procurarPorCpf(cpf);
        if (Objects.isNull(pessoaFisicaEncontrada)) {
            throw new CustomException("CPF não encontrado!");
        }
        return mapearPessoa(TipoClienteEnum.FISICA, pessoaFisicaEncontrada);
    }

    public ClienteResponse procurarPessoaPorCnpj(String cnpj){
        ClienteModel pessoaJuridicaEncontrada = clienteDao.procurarPorCnpj(cnpj);

        if (Objects.isNull(pessoaJuridicaEncontrada)){
            throw new CustomException("CNPJ não encontrada");
        }
        return mapearPessoa(TipoClienteEnum.JURIDICA, pessoaJuridicaEncontrada);
    }


    public List<ClienteResponse> listarPessoas() {
        List<ClienteModel> pessoasEncontradas = clienteDao.listarTodasPessoas();
        List<ClienteResponse> clienteResponse = new ArrayList<>();

        for (ClienteModel pessoa : pessoasEncontradas) {
            TipoClienteEnum tipoPessoa = pessoa.getCpf() != null ? TipoClienteEnum.FISICA : TipoClienteEnum.JURIDICA;
            clienteResponse.add(mapearPessoa(tipoPessoa, pessoa));
        }
        return clienteResponse;
    }

    public ClienteResponse atualizarPessoa(String identificador, ClienteRequest clienteRequest){
        ClienteModel pessoaExistente;

        if (!validacao.validarEmail(clienteRequest.getEmail())) {
            throw new CustomException("Email inválido!");
        }

        if (!validacao.validarNumeroTelefone(clienteRequest.getTelefone())) {
            throw new CustomException("Número de celular inválido!");
        }

        if (!validacao.validarDataNascimento(clienteRequest.getDataNascimento())) {
            throw new CustomException("Data de nascimento inválida!");
        }

        if (identificador.length() == 11) { // CPF
            pessoaExistente = clienteDao.procurarPorCpf(identificador);
        } else if (identificador.length() == 14) { // CNPJ
            pessoaExistente = clienteDao.procurarPorCnpj(identificador);
        } else {
            throw new CustomException("Identificador inválido!");
        }

        if (Objects.isNull(pessoaExistente)) {
            throw new CustomException("Cliente não encontrado!");
        }

        TipoClienteModel tipoClienteModel = tipoClienteDao.findById(pessoaExistente.getTipo_pessoa());

        pessoaExistente.setNome(clienteRequest.getNome());
        pessoaExistente.setEmail(clienteRequest.getEmail());
        pessoaExistente.setSenha(clienteRequest.getSenha());
        pessoaExistente.setTelefone(clienteRequest.getTelefone());
        pessoaExistente.setTipo_pessoa(tipoClienteModel.getIdTipoPessoa());

        ClienteModel clienteAtualizado = clienteDao.update(pessoaExistente);

        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA){
            ClienteFisicaModel pessoaFisica = clienteFisicaDao.findByCpf(identificador);
            if (pessoaFisica != null) {
                pessoaFisica.setIdPessoa(pessoaExistente.getIdPessoa());
                pessoaFisica.setCpf(clienteRequest.getCpf());
                pessoaFisica.setDataNascimento(clienteRequest.getDataNascimento());
                clienteFisicaDao.update(pessoaFisica);
            }
        } else if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.JURIDICA) {
            ClienteJuridicaModel pessoaJuridica = clienteJuridicaDao.findByCnpj(identificador);
            if (pessoaJuridica != null) {
                pessoaJuridica.setIdPessoa(pessoaExistente.getIdPessoa());
                pessoaJuridica.setCnpj(clienteRequest.getCnpj());
                clienteJuridicaDao.update(pessoaJuridica);
            }
        }

        return mapearPessoa(TipoClienteEnum.valueOf(tipoClienteModel.getNomeTipoPessoa()), clienteAtualizado);
    }
}

