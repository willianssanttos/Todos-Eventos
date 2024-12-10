package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.model.cliente.ClienteFisicoModel;
import com.todoseventos.todos_eventos.repository.cliente.ClienteRepository;
import com.todoseventos.todos_eventos.repository.cliente.ClienteFisicaRepository;
import com.todoseventos.todos_eventos.repository.cliente.ClienteJuridicaRepository;
import com.todoseventos.todos_eventos.repository.cliente.TipoClienteRepository;
import com.todoseventos.todos_eventos.dto.cliente.ClienteRequest;
import com.todoseventos.todos_eventos.dto.cliente.ClienteResponse;
import com.todoseventos.todos_eventos.dto.Enuns.TipoClienteEnum;
import com.todoseventos.todos_eventos.exception.*;
import com.todoseventos.todos_eventos.model.cliente.ClienteFisicoModel;
import com.todoseventos.todos_eventos.model.cliente.ClienteJuridicoModel;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import com.todoseventos.todos_eventos.model.cliente.TipoClienteModel;
import com.todoseventos.todos_eventos.utils.Constantes;
import com.todoseventos.todos_eventos.utils.Validacoes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClienteService {

    @Autowired
    private Validacoes validacoes;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TipoClienteRepository tipoClienteRepository;

    @Autowired
    private ClienteFisicaRepository clienteFisicaRepository;

    @Autowired
    private ClienteJuridicaRepository clienteJuridicaRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    /**
     * Cadastra uma nova pessoa (física ou jurídica).
     * @param clienteRequest Objeto contendo os detalhes da pessoa a ser cadastrada.
     * @return Um objeto de resposta contendo os detalhes da pessoa cadastrada.
     */
    public ClienteResponse cadastrarNovaPessoa(ClienteRequest clienteRequest) {
        try {
            if (clienteRequest.getTipo_pessoa() == null) {
                throw new TipoCategoriaValidacaoException();
            }

            TipoClienteModel tipoClienteModel = tipoClienteRepository.buscarPorNomeTipoPessoa(clienteRequest.getTipo_pessoa().name());

            if (Objects.isNull(tipoClienteModel)) {
                throw new TipoCategoriaValidacaoException();
            }

            validarDados(clienteRequest);

            clienteRequest.setTelefone(validacoes.formatarNumeroTelefone(clienteRequest.getTelefone()));
            String encodedPassword = passwordEncoder.encode(clienteRequest.getSenha());

            ClienteModel pessoa = ClienteModel.builder()
                    .nome(clienteRequest.getNome())
                    .email(clienteRequest.getEmail())
                    .senha(encodedPassword)
                    .telefone(clienteRequest.getTelefone())
                    .tipo_pessoa(tipoClienteModel.getIdTipoPessoa())
                    .build();

            ClienteModel pessoaSalva = clienteRepository.salvarCliente(pessoa);

            if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA) {
                ClienteFisicoModel pessoaFisica = ClienteFisicoModel.builder()
                        .cpf(clienteRequest.getCpf())
                        .dataNascimento(clienteRequest.getDataNascimento())
                        .idPessoa(pessoaSalva.getIdPessoa())
                        .build();
                clienteFisicaRepository.salvarCliFisico(pessoaFisica);

            } else if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.JURIDICA) {
                ClienteJuridicoModel pessoaJuridica = ClienteJuridicoModel.builder()
                        .cnpj(clienteRequest.getCnpj())
                        .idPessoa(pessoaSalva.getIdPessoa())
                        .build();
                clienteJuridicaRepository.salvarCliJuridico(pessoaJuridica);
            }
            return mapearPessoa(clienteRequest.getTipo_pessoa(), pessoaSalva);
        } catch (Exception e)
        {
            logger.error(Constantes.ERRO_SALVAR_CLIENTE + e.getMessage());
            throw new ClienteException();
        }
    }

    /**
     * Valida os dados do cliente.
     * @param clienteRequest Objeto contendo os detalhes do cliente a ser validado.
     */
    private void validarDados(ClienteRequest clienteRequest) {
        if (!validacoes.validarEmail(clienteRequest.getEmail())) {
            throw new EmailValidacaoException();
        }

        if (!validacoes.validarNumeroTelefone(clienteRequest.getTelefone())) {
            throw new NumeroCelularValidacaoException();
        }

        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA &&
            !validacoes.validarDataNascimento(clienteRequest.getDataNascimento())) {
            throw new DataNascimentoValidacaoException();
        }

        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA) {
            if (!validacoes.isCpfValid(clienteRequest.getCpf())) {
                throw new CepValidacaoExcecao();
            }

            ClienteModel pessoaExistente = clienteRepository.procurarPorCpf(clienteRequest.getCpf());
            if (pessoaExistente != null) {
                throw new CpfNotFoundException();
            }

        } else if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.JURIDICA) {
            if (!validacoes.isCnpjValid(clienteRequest.getCnpj())) {
                throw new CnpjValidacaoException();
            }

            ClienteModel pessoaExistente = clienteRepository.procurarPorCnpj(clienteRequest.getCnpj());
            if (pessoaExistente != null) {
                throw new CnpjNotFoundException();
            }
        }
    }

    /**
     * Mapeia os detalhes de uma pessoa (física ou jurídica) para um objeto de resposta.
     * @param tipo_pessoa O tipo da pessoa (física ou jurídica).
     * @param pessoaSalva O objeto pessoa contendo os detalhes da pessoa salva.
     * @return Um objeto de resposta contendo os detalhes da pessoa.
     */
    private static ClienteResponse mapearPessoa(TipoClienteEnum tipo_pessoa, ClienteModel pessoaSalva) {
        ClienteResponse.ClienteResponseBuilder builder = ClienteResponse.builder()
                .nome(pessoaSalva.getNome())
                .email(pessoaSalva.getEmail())
                .senha(pessoaSalva.getSenha())
                .telefone(pessoaSalva.getTelefone())
                .tipo_pessoa(tipo_pessoa)
                .idPessoa(pessoaSalva.getIdPessoa());

        if (tipo_pessoa == TipoClienteEnum.FISICA) {
            builder.cpf(String.valueOf(pessoaSalva.getClienteFisicaModel()))
                    .dataNascimento(String.valueOf(pessoaSalva.getClienteFisicaModel()));
        } else if (tipo_pessoa == TipoClienteEnum.JURIDICA) {
            builder.cnpj(String.valueOf(pessoaSalva.getClienteJuridicaModel()));
        }
        return builder.build();
    }

    /**
     * Procura uma pessoa física pelo CPF.
     * @param cpf O CPF da pessoa física.
     * @return Um objeto de resposta contendo os detalhes da pessoa encontrada.
     */
    public ClienteResponse procurarPessoaPorCpf(String cpf) {
        ClienteModel pessoaFisicaEncontrada = clienteRepository.procurarPorCpf(cpf);
        if (Objects.isNull(pessoaFisicaEncontrada)) {
            throw new CpfNotFoundException();
        }
        return mapearPessoa(TipoClienteEnum.FISICA, pessoaFisicaEncontrada);
    }

    /**
     * Procura uma pessoa jurídica pelo CNPJ.
     * @param cnpj O CNPJ da pessoa jurídica.
     * @return Um objeto de resposta contendo os detalhes da pessoa encontrada.
     */
    public ClienteResponse procurarPessoaPorCnpj(String cnpj) {
        ClienteModel pessoaJuridicaEncontrada = clienteRepository.procurarPorCnpj(cnpj);

        if (Objects.isNull(pessoaJuridicaEncontrada)) {
            throw new CnpjNotFoundException();
        }
        return mapearPessoa(TipoClienteEnum.JURIDICA, pessoaJuridicaEncontrada);
    }

    /**
     * Lista todas as pessoas cadastradas.
     * @return Uma lista de objetos de resposta contendo os detalhes das pessoas cadastradas.
     */
    public List<ClienteResponse> listarPessoas() {
        List<ClienteModel> pessoasEncontradas = clienteRepository.listarTodasPessoas();
        List<ClienteResponse> clienteResponse = new ArrayList<>();

        for (ClienteModel pessoa : pessoasEncontradas) {
            TipoClienteEnum tipoPessoa = pessoa.getClienteFisicaModel() != null ? TipoClienteEnum.FISICA : TipoClienteEnum.JURIDICA;
            clienteResponse.add(mapearPessoa(tipoPessoa, pessoa));
        }
        return clienteResponse;
    }

    /**
     * Atualiza os detalhes de uma pessoa (física ou jurídica).
     * @param identificador O CPF ou CNPJ da pessoa a ser atualizada.
     * @param clienteRequest Objeto contendo os novos detalhes da pessoa.
     * @return Um objeto de resposta contendo os detalhes da pessoa atualizada.
     */
    public ClienteResponse atualizarPessoa(String identificador, ClienteRequest clienteRequest) {
        ClienteModel pessoaExistente;


        if (identificador.length() == 11) { // CPF
            pessoaExistente = clienteRepository.procurarPorCpf(identificador);
        } else if (identificador.length() == 14) { // CNPJ
            pessoaExistente = clienteRepository.procurarPorCnpj(identificador);
        } else {
            throw new IdentificadorValidacaoException();
        }

        if (Objects.isNull(pessoaExistente)) {
            throw new ClienteNotFoundException();
        }

        TipoClienteModel tipoClienteModel = tipoClienteRepository.buscarPorNomeTipoPessoa(clienteRequest.getTipo_pessoa().name());
        String encodedPassword = passwordEncoder.encode(clienteRequest.getSenha());

        pessoaExistente.setNome(clienteRequest.getNome());
        pessoaExistente.setEmail(clienteRequest.getEmail());
        pessoaExistente.setSenha(encodedPassword);
        pessoaExistente.setTelefone(clienteRequest.getTelefone());
        pessoaExistente.setTipo_pessoa(tipoClienteModel.getIdTipoPessoa());

        ClienteModel clienteAtualizado = clienteRepository.atualizarCliente(pessoaExistente);

        if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.FISICA) {
            ClienteFisicoModel pessoaFisica = clienteFisicaRepository.procurarCpf(identificador);
            if (pessoaFisica != null) {
                pessoaFisica.setIdPessoa(clienteAtualizado.getIdPessoa());
                pessoaFisica.setCpf(clienteRequest.getCpf());
                pessoaFisica.setDataNascimento(clienteRequest.getDataNascimento());
                clienteFisicaRepository.atualizarCliFisico(pessoaFisica);
            }
        } else if (clienteRequest.getTipo_pessoa() == TipoClienteEnum.JURIDICA) {
            ClienteJuridicoModel pessoaJuridica = clienteJuridicaRepository.procurarCnpj(identificador);
            if (pessoaJuridica != null) {
                pessoaJuridica.setIdPessoa(clienteAtualizado.getIdPessoa());
                pessoaJuridica.setCnpj(clienteRequest.getCnpj());
                clienteJuridicaRepository.atualizarJuridico(pessoaJuridica);
            }
        }
        return mapearPessoa(clienteRequest.getTipo_pessoa(), clienteAtualizado);
    }
}