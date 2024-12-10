package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.exception.*;
import com.todoseventos.todos_eventos.model.cliente.ClienteFisicoModel;
import com.todoseventos.todos_eventos.model.cliente.ClienteJuridicoModel;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import com.todoseventos.todos_eventos.repository.evento.CategoriaRepository;
import com.todoseventos.todos_eventos.repository.cliente.ClienteFisicaRepository;
import com.todoseventos.todos_eventos.repository.cliente.ClienteJuridicaRepository;
import com.todoseventos.todos_eventos.repository.evento.EnderecoRepository;
import com.todoseventos.todos_eventos.repository.evento.EventoRepository;
import com.todoseventos.todos_eventos.repository.evento.ParticipacaoRepository;
import com.todoseventos.todos_eventos.dto.Enuns.CategoriaEnum;
import com.todoseventos.todos_eventos.dto.CepResponse;
import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.utils.Constantes;
import com.todoseventos.todos_eventos.utils.Validacoes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private Validacoes validacoes;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CepService cepService;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    @Autowired
    private ClienteFisicaRepository clienteFisicaRepository;

    @Autowired
    private ClienteJuridicaRepository clienteJuridicaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventoService.class);

    /**
     * Cadastra um novo evento.
     * @param eventoRequest Objeto contendo os detalhes do evento a ser cadastrado.
     * @return Um objeto de resposta contendo os detalhes do evento cadastrado.
     */
    public EventoResponse cadastrarNovoEvento(EventoRequest eventoRequest) {
        logger.info(Constantes.DebugRegistroProcesso);

        try {
            if (eventoRequest.getCategoria() == null) {
                throw new TipoCategoriaValidacaoException();
            }
            // Verificar se a categoria é válida
            CategoriaModel categoriaModel = categoriaRepository.buscarNomeCategoria(eventoRequest.getCategoria().name());

            if (Objects.isNull(categoriaModel)) {
                throw new TipoCategoriaValidacaoException();
            }

            // Validar o CEP
            if (!validacoes.validarCep(eventoRequest.getCep())) {
                throw new CepValidacaoExcecao();
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

            EventoModel eventoSalvo = eventoRepository.salvarEvento(evento);

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

            EnderecoModel enderecoSalvo = enderecoRepository.salverEndereco(endereco);

            return mapearEvento(categoriaModel, eventoSalvo, enderecoSalvo);
        } catch (Exception e){
            logger.error(Constantes.ErroRegistrarNoServidor + e.getMessage());
            throw new EventoException();
        }
    }

    /**
     * Encerra um evento.
     * @param idEvento O ID do evento a ser encerrado.
     * @return Um objeto de resposta contendo os detalhes do evento encerrado.
     */
    public EventoResponse encerrarEvento(Integer idEvento) {
        logger.info(Constantes.DebugRegistroProcesso);

        try {
            EventoModel evento = eventoRepository.procurarPorId(idEvento)
                    .orElseThrow(BuscarEventoNotFoundException::new);
            evento.setStatus("CANCELADO");
            eventoRepository.atualizarEvento(evento);

            // Envia e-mails de cancelamento para todos os participantes do evento
            List<ParticipacaoModel> participacoes = participacaoRepository.localizarPorIdEvento(idEvento);
            participacoes.forEach(participacao -> {
                String email;
                String nomePessoa;
                if (participacao.getCpf() != null) {
                    ClienteFisicoModel clienteFisica = clienteFisicaRepository.procurarCpf(participacao.getCpf());
                    email = clienteFisica.getEmail();
                    nomePessoa = clienteFisica.getNome();
                } else {
                    ClienteJuridicoModel clienteJuridica = clienteJuridicaRepository.procurarCnpj(participacao.getCnpj());
                    email = clienteJuridica.getEmail();
                    nomePessoa = clienteJuridica.getNome();
                }
                emailService.enviarEmailCancelamento(email, nomePessoa, evento.getNome_evento());
            });

            return mapearEncerramentoEvento(evento);
        } catch (Exception e){
            logger.error(Constantes.ErroRegistrarNoServidor + e.getMessage());
            throw new EncerrarEventoException();
        }
    }

    /**
     * Mapeia os detalhes do encerramento de um evento para um objeto de resposta.
     * @param evento O objeto evento contendo os detalhes do evento.
     * @return Um objeto de resposta contendo os detalhes do evento encerrado.
     */
    private EventoResponse mapearEncerramentoEvento(EventoModel evento) {
        logger.info(Constantes.DebugBuscarProcesso);
       try {
           CategoriaModel categoria = categoriaRepository.procurarId(evento.getId_categoria());
           EnderecoModel endereco = enderecoRepository.procurarPorIdEvento(evento.getIdEvento())
                   .orElseThrow(() -> new CustomException(Constantes.ENDERECO_NAO_ENCONTRADO + evento.getNome_evento()));

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
       } catch (Exception e){
           logger.error(Constantes.ErroBuscarRegistroNoServidor + e.getMessage());
           throw new BuscarEventoNotFoundException();
       }
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
        logger.info(Constantes.DebugBuscarProcesso);
       try {
           List<EventoModel> eventoModelList;
           List<EventoResponse> eventoResponseList = new ArrayList<>();
           eventoModelList = eventoRepository.localizarEvento();

           for (EventoModel eventoModel : eventoModelList) {
               CategoriaModel categoriaModel;
               EnderecoModel enderecoModel;
               categoriaModel = categoriaRepository.procurarId(eventoModel.getId_categoria());
               enderecoModel = enderecoRepository.procurarPorIdEvento(eventoModel.getIdEvento())
                    .orElseThrow(() -> new CustomException(Constantes.ENDERECO_NAO_ENCONTRADO + eventoModel.getNome_evento()));
               EventoResponse eventoResponse = mapearEvento(categoriaModel, eventoModel, enderecoModel);
               eventoResponseList.add(eventoResponse);
           }
           return eventoResponseList;
       } catch (Exception e){
           logger.error(Constantes.ErroBuscarRegistroNoServidor + e.getMessage());
           throw new BuscarEventoNotFoundException();
       }
    }

    /**
     * Procura um evento pelo nome.
     * @param nomeEvento O nome do evento a ser procurado.
     * @return Um objeto de resposta contendo os detalhes do evento localizado.
     */
    public EventoResponse procurarEventoPorNome(String nomeEvento) {
        logger.info(Constantes.DebugBuscarProcesso);
       try {
           EventoModel eventoModel = eventoRepository.procurarPorNome(nomeEvento)
                   .orElseThrow(() -> new CustomException(Constantes.EVENTO_NAO_ENCONTRADO));

           CategoriaModel categoriaModel = categoriaRepository.procurarId(eventoModel.getId_categoria());
           EnderecoModel enderecoModel = enderecoRepository.procurarPorIdEvento(eventoModel.getIdEvento())
                   .orElseThrow(() -> new CustomException(Constantes.ENDERECO_NAO_ENCONTRADO + nomeEvento));

           return mapearEvento(categoriaModel, eventoModel, enderecoModel);
       } catch (Exception e){
           logger.error(Constantes.ErroBuscarRegistroNoServidor + e.getMessage());
           throw new BuscarEventoNotFoundException();
       }
    }

    /**
     * Atualiza um evento existente.
     * @param nomeEvento O nome do evento a ser atualizado.
     * @param eventoRequest Objeto contendo os novos detalhes do evento.
     * @return Um objeto de resposta contendo os detalhes do evento atualizado.
     */
    public EventoResponse atualizarEvento(String nomeEvento, EventoRequest eventoRequest) {
        logger.info(Constantes.DebugEditarProcesso);

        try {
            EventoModel eventoExistente = eventoRepository.procurarPorNome(nomeEvento)
                    .orElseThrow(() -> new CustomException(Constantes.EVENTO_NAO_ENCONTRADO));

            CategoriaModel categoriaModel = categoriaRepository.buscarNomeCategoria(eventoRequest.getCategoria().name());

            if (categoriaModel == null) {
                throw new TipoCategoriaValidacaoException();
            }

            // Validar o CEP
            if (!validacoes.validarCep(eventoRequest.getCep())) {
                throw new CepValidacaoExcecao();
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

            EventoModel eventoAtualizado = eventoRepository.atualizarEvento(eventoExistente);

            EnderecoModel enderecoExistente = enderecoRepository.procurarPorIdEvento(eventoExistente.getIdEvento())
                    .orElseThrow(() -> new CustomException(Constantes.ENDERECO_NAO_ENCONTRADO + nomeEvento));

            enderecoExistente.setRua(eventoRequest.getRua());
            enderecoExistente.setNumero(eventoRequest.getNumero());
            enderecoExistente.setBairro(eventoRequest.getBairro());
            enderecoExistente.setCidade(eventoRequest.getCidade());
            enderecoExistente.setCep(eventoRequest.getCep());
            enderecoExistente.setUf(eventoRequest.getUf());

            EnderecoModel enderecoAtualizado = enderecoRepository.atualizarEndereco(enderecoExistente);

            return mapearEvento(categoriaModel, eventoAtualizado, enderecoAtualizado);
        } catch (Exception e){
            logger.error(Constantes.ErroEditarRegistroNoServidor + e.getMessage());
            throw new AtualizarEventoException();
        }
    }

    /**
     * Exclui um evento.
     * @param idEvento O ID do evento a ser excluído.
     */
    public void excluirEvento(Integer idEvento) {
        logger.info(Constantes.DebugDeletarProcesso);
       try {
           eventoRepository.procurarPorId(idEvento)
                   .orElseThrow(() -> new CustomException(Constantes.EVENTO_NAO_ENCONTRADO));
           enderecoRepository.deletarPorIdEvento(idEvento);
           eventoRepository.deletarPorId(idEvento);
       } catch (Exception e){
           logger.error(Constantes.ErroDeletarRegistroNoServidor + e.getMessage());
           throw new DeletarEventoException();
       }
    }
}
