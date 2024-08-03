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

    public EventoResponse cadastrarNovoEvento(EventoRequest eventoRequest) {

        if (eventoRequest.getCategoria() == null) {
            throw new CustomException(CustomException.TIPO_CATEGORIA_INVALIDO);
        }
        // Verificar se a categoria é válida
        CategoriaModel categoriaModel = categoriaDao.findNomeCategoria(eventoRequest.getCategoria().name());

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

        EventoModel eventoSalvo = eventoDao.save(evento);

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

        EnderecoModel enderecoSalvo = enderecoDao.save(endereco);

        return mapearEvento(categoriaModel, eventoSalvo, enderecoSalvo);
    }

    public EventoResponse encerrarEvento(Integer idEvento) {
        EventoModel evento = eventoDao.procurarPorId(idEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));
        evento.setStatus("CANCELADO");
        EventoModel updatedEvento = eventoDao.update(evento);

        List<ParticipacaoModel> participacoes = participacaoDao.findByIdEvento(idEvento);
        participacoes.forEach(participacao -> {
            String email;
            String nomePessoa;
            if (participacao.getCpf() != null) {
                ClienteFisicaModel clienteFisica = clienteFisicaDao.findByCpf(participacao.getCpf());
                email = clienteFisica.getEmail();
                nomePessoa = clienteFisica.getNome();
            } else {
                ClienteJuridicaModel clienteJuridica = clienteJuridicaDao.findByCnpj(participacao.getCnpj());
                email = clienteJuridica.getEmail();
                nomePessoa = clienteJuridica.getNome();
            }
            emailService.enviarEmailCancelamento(email, nomePessoa, evento.getNome_evento());
        });

        return mapearEncerramentoEvento(evento);
    }

    private EventoResponse mapearEncerramentoEvento(EventoModel evento) {
        CategoriaModel categoria = categoriaDao.findById(evento.getId_categoria());
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
                categoriaModel = categoriaDao.findById(eventoModel.getId_categoria());
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

    public EventoResponse procurarEventoPorNome(String nomeEvento) {
        EventoModel eventoModel = eventoDao.procurarPorNome(nomeEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));

        CategoriaModel categoriaModel = categoriaDao.findById(eventoModel.getId_categoria());
        EnderecoModel enderecoModel = enderecoDao.procurarPorIdEvento(eventoModel.getIdEvento())
                .orElseThrow(() -> new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + nomeEvento));

        return mapearEvento(categoriaModel, eventoModel, enderecoModel);
    }

    public EventoResponse atualizarEvento(String nomeEvento, EventoRequest eventoRequest) {
        EventoModel eventoExistente = eventoDao.procurarPorNome(nomeEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));

        CategoriaModel categoriaModel = categoriaDao.findNomeCategoria(eventoRequest.getCategoria().name());

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

        EventoModel eventoAtualizado = eventoDao.update(eventoExistente);

        EnderecoModel enderecoExistente = enderecoDao.procurarPorIdEvento(eventoExistente.getIdEvento())
                .orElseThrow(() -> new CustomException(CustomException.ENDERECO_NAO_ENCONTRADO + nomeEvento));

        enderecoExistente.setRua(eventoRequest.getRua());
        enderecoExistente.setNumero(eventoRequest.getNumero());
        enderecoExistente.setBairro(eventoRequest.getBairro());
        enderecoExistente.setCidade(eventoRequest.getCidade());
        enderecoExistente.setCep(eventoRequest.getCep());
        enderecoExistente.setUf(eventoRequest.getUf());

        EnderecoModel enderecoAtualizado = enderecoDao.update(enderecoExistente);

        return mapearEvento(categoriaModel, eventoAtualizado, enderecoAtualizado);
    }

    public void excluirEvento(Integer idEvento) {
        EventoModel eventoExistente = eventoDao.procurarPorId(idEvento)
                .orElseThrow(() -> new CustomException(CustomException.EVENTO_NAO_ENCONTRADO));

        enderecoDao.deleteByIdEvento(idEvento);
        eventoDao.deleteById(idEvento);
    }
}
