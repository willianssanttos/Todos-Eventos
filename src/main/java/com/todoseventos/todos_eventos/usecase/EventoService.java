package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.*;
import com.todoseventos.todos_eventos.dto.CategoriaEnum;
import com.todoseventos.todos_eventos.dto.CepResponse;
import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.model.cliente.ClienteFisicaModel;
import com.todoseventos.todos_eventos.model.cliente.ClienteJuridicaModel;
import com.todoseventos.todos_eventos.utils.Validacoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EventoService {

    @Autowired
    private EventoDao eventoDao;

    @Autowired
    private Validacoes validacoes;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CepService cepService;

    @Autowired
    private EnderecoDao enderecoDao;

    @Autowired
    private ParticipacaoDao participacaoDao;

    @Autowired
    private ClienteFisicaDao clienteFisicaDao;

    @Autowired
    private ClienteJuridicaDao clienteJuridicaDao;

    @Autowired
    private CategoriaDao categoriaDao;

    /**
     * Cadastra um novo evento.
     * @param eventoRequest Objeto contendo os detalhes do evento a ser cadastrado.
     * @return Um objeto de resposta contendo os detalhes do evento cadastrado.
     */
    public EventoResponse cadastrarNovoEvento(EventoRequest eventoRequest) {

        if (eventoRequest.getCategoria() == null) {
            throw new CustomException(CustomException.TIPO_CATEGORIA_INVALIDO);
        }
        // Verificar se a categoria é válida
        CategoriaModel categoriaModel = categoriaDao.buscarNomeCategoria(eventoRequest.getCategoria().name());

        if (Objects.isNull(categoriaModel)) {
            throw new CustomException(CustomException.CATEGORIA_INVALIDA);
        }

        // Validar o CEP
        if (!validacoes.validarCep(eventoRequest.getCep())) {
            throw new CustomException(CustomException.CEP_INVALIDO);
        }

        // Consultar e preencher dados do CEP
        CepResponse cepResponse = cepService.consultarCep(eventoRequest.getCep());
        eventoRequest.setRua(cepResponse.getLogradouro());
        eventoRequest.setBairro(cepResponse.getBairro());
        eventoRequest.setCidade(cepResponse.getLocalidade());
        eventoRequest.setUf(cepResponse.getUf());

        // Criar e salvar o evento
        EventoModel evento = EventoModel.builder()
                .nome_evento(eventoRequest.getNome_evento())
                .dataHora_evento(eventoRequest.getDataHora_evento())
                .dataHora_eventofinal(eventoRequest.getDataHora_eventofinal())
                .descricao(eventoRequest.getDescricao())
                .status("ATIVO")
                .id_categoria(categoriaModel.getIdCategoria())
                .build();

        EventoModel eventoSalvo = eventoDao.salvarEvento(evento);

        // Criar e salvar o endereço
        EnderecoModel endereco = EnderecoModel.builder()
                .idEvento(eventoSalvo.getIdEvento())
                .rua(eventoRequest.getRua())
                .numero(eventoRequest.getNumero())
                .bairro(eventoRequest.getBairro())
                .cidade(eventoRequest.getCidade())
                .cep(eventoRequest.getCep())
                .uf(eventoRequest.getUf())
                .build();

        EnderecoModel enderecoSalvo = enderecoDao.salverEndereco(endereco);

        return mapearEvento(categoriaModel, eventoSalvo, enderecoSalvo);
    }

    /**
     * Encerra um evento.
     * @param idEvento O ID do evento a ser encerrado.
     * @return Um objeto de resposta contendo os detalhes do evento encerrado.
     */
    public EventoResponse encerrarEvento(Integer idEvento) {
        EventoModel evento = eventoDao.procurarPorId(idEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));
        evento.setStatus("CANCELADO");
        EventoModel updatedEvento = eventoDao.atualizarEvento(evento);

        // Envia e-mails de cancelamento para todos os participantes do evento
        List<ParticipacaoModel> participacoes = participacaoDao.localizarPorIdEvento(idEvento);
        participacoes.forEach(participacao -> {
            String email;
            String nomePessoa;
            if (participacao.getCpf() != null) {
                ClienteFisicaModel clienteFisica = clienteFisicaDao.procurarCpf(participacao.getCpf());
                email = clienteFisica.getEmail();
                nomePessoa = clienteFisica.getNome();
            } else {
                ClienteJuridicaModel clienteJuridica = clienteJuridicaDao.procurarCnpj(participacao.getCnpj());
                email = clienteJuridica.getEmail();
                nomePessoa = clienteJuridica.getNome();
            }
            emailService.enviarEmailCancelamento(email, nomePessoa, evento.getNome_evento());
        });

        return mapearEncerramentoEvento(evento);
    }

    /**
     * Mapeia os detalhes do encerramento de um evento para um objeto de resposta.
     * @param evento O objeto evento contendo os detalhes do evento.
     * @return Um objeto de resposta contendo os detalhes do evento encerrado.
     */
    private EventoResponse mapearEncerramentoEvento(EventoModel evento) {
        CategoriaModel categoria = categoriaDao.procurarId(evento.getId_categoria());
        EnderecoModel endereco = enderecoDao.procurarPorIdEvento(evento.getIdEvento())
                .orElseThrow(() -> new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + evento.getNome_evento()));

        return EventoResponse.builder()
                .idEvento(evento.getIdEvento())
                .nome_evento(evento.getNome_evento())
                .dataHora_evento(evento.getDataHora_evento())
                .dataHora_eventofinal(evento.getDataHora_eventofinal())
                .descricao(evento.getDescricao())
                .status(evento.getStatus())
                .categoria(CategoriaEnum.valueOf(categoria.getNomeCategoria()))
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .cep(endereco.getCep())
                .uf(endereco.getUf())
                .build();
    }

    /**
     * Mapeia os detalhes de um evento para um objeto de resposta.
     * @param categoria O objeto categoria contendo os detalhes da categoria do evento.
     * @param eventoSalvo O objeto evento contendo os detalhes do evento salvo.
     * @param enderecoSalvo O objeto endereço contendo os detalhes do endereço salvo.
     * @return Um objeto de resposta contendo os detalhes do evento.
     */
    private EventoResponse mapearEvento(CategoriaModel categoria, EventoModel eventoSalvo, EnderecoModel enderecoSalvo) {
        return EventoResponse.builder()
                .idEvento(eventoSalvo.getIdEvento())
                .nome_evento(eventoSalvo.getNome_evento())
                .dataHora_evento(eventoSalvo.getDataHora_evento())
                .dataHora_eventofinal(eventoSalvo.getDataHora_eventofinal())
                .descricao(eventoSalvo.getDescricao())
                .status(eventoSalvo.getStatus())
                .categoria(CategoriaEnum.valueOf(categoria.getNomeCategoria()))
                .rua(enderecoSalvo.getRua())
                .numero(enderecoSalvo.getNumero())
                .bairro(enderecoSalvo.getBairro())
                .cidade(enderecoSalvo.getCidade())
                .cep(enderecoSalvo.getCep())
                .uf(enderecoSalvo.getUf())
                .build();
    }

    /**
     * Localiza todos os eventos cadastrados.
     * @return Uma lista de objetos de resposta contendo os detalhes dos eventos localizados.
     */
    public List<EventoResponse> localizarEventos() {
        List<EventoModel> eventoModelList;
        List<EventoResponse> eventoResponseList = new ArrayList<>();

        try {
            eventoModelList = eventoDao.localizarEvento();
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_EVENTOS + e.getMessage());
        }

        for (EventoModel eventoModel : eventoModelList) {
            CategoriaModel categoriaModel;
            EnderecoModel enderecoModel;

            try {
                categoriaModel = categoriaDao.procurarId(eventoModel.getId_categoria());
            } catch (Exception e) {
                throw new CustomException(CustomException.ERRO_BUSCAR_CATEGORIA_EVENTO + e.getMessage());
            }

            try {
                enderecoModel = enderecoDao.procurarPorIdEvento(eventoModel.getIdEvento())
                        .orElseThrow(() -> new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + eventoModel.getNome_evento()));
            } catch (Exception e) {
                throw new CustomException(CustomException.ERRO_BUSCAR_ENDERECO_EVENTO + e.getMessage());
            }

            EventoResponse eventoResponse = mapearEvento(categoriaModel, eventoModel, enderecoModel);
            eventoResponseList.add(eventoResponse);
        }
        return eventoResponseList;
    }

    /**
     * Procura um evento pelo nome.
     * @param nomeEvento O nome do evento a ser procurado.
     * @return Um objeto de resposta contendo os detalhes do evento localizado.
     */
    public EventoResponse procurarEventoPorNome(String nomeEvento) {
        EventoModel eventoModel = eventoDao.procurarPorNome(nomeEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));

        CategoriaModel categoriaModel = categoriaDao.procurarId(eventoModel.getId_categoria());
        EnderecoModel enderecoModel = enderecoDao.procurarPorIdEvento(eventoModel.getIdEvento())
                .orElseThrow(() -> new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + nomeEvento));

        return mapearEvento(categoriaModel, eventoModel, enderecoModel);
    }

    /**
     * Atualiza um evento existente.
     * @param nomeEvento O nome do evento a ser atualizado.
     * @param eventoRequest Objeto contendo os novos detalhes do evento.
     * @return Um objeto de resposta contendo os detalhes do evento atualizado.
     */
    public EventoResponse atualizarEvento(String nomeEvento, EventoRequest eventoRequest) {
        EventoModel eventoExistente = eventoDao.procurarPorNome(nomeEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));

        CategoriaModel categoriaModel = categoriaDao.buscarNomeCategoria(eventoRequest.getCategoria().name());

        if (categoriaModel == null) {
            throw new CustomException(CustomException.CATEGORIA_INVALIDA);
        }

        // Validar o CEP
        if (!validacoes.validarCep(eventoRequest.getCep())) {
            throw new CustomException(CustomException.CEP_INVALIDO);
        }

        // Consultar e preencher dados do CEP
        CepResponse cepResponse = cepService.consultarCep(eventoRequest.getCep());
        eventoRequest.setRua(cepResponse.getLogradouro());
        eventoRequest.setBairro(cepResponse.getBairro());
        eventoRequest.setCidade(cepResponse.getLocalidade());
        eventoRequest.setUf(cepResponse.getUf());

        eventoExistente.setNome_evento(eventoRequest.getNome_evento());
        eventoExistente.setDataHora_evento(eventoRequest.getDataHora_evento());
        eventoExistente.setDataHora_eventofinal(eventoRequest.getDataHora_eventofinal());
        eventoExistente.setDescricao(eventoRequest.getDescricao());
        eventoExistente.setId_categoria(categoriaModel.getIdCategoria());

        EventoModel eventoAtualizado = eventoDao.atualizarEvento(eventoExistente);

        EnderecoModel enderecoExistente = enderecoDao.procurarPorIdEvento(eventoExistente.getIdEvento())
                .orElseThrow(() -> new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + nomeEvento));

        enderecoExistente.setRua(eventoRequest.getRua());
        enderecoExistente.setNumero(eventoRequest.getNumero());
        enderecoExistente.setBairro(eventoRequest.getBairro());
        enderecoExistente.setCidade(eventoRequest.getCidade());
        enderecoExistente.setCep(eventoRequest.getCep());
        enderecoExistente.setUf(eventoRequest.getUf());

        EnderecoModel enderecoAtualizado = enderecoDao.atualizarEndereco(enderecoExistente);

        return mapearEvento(categoriaModel, eventoAtualizado, enderecoAtualizado);
    }

    /**
     * Exclui um evento.
     * @param idEvento O ID do evento a ser excluído.
     */
    public void excluirEvento(Integer idEvento) {
        EventoModel eventoExistente = eventoDao.procurarPorId(idEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));

        enderecoDao.deletarPorIdEvento(idEvento);
        eventoDao.deletarPorId(idEvento);
    }
}
