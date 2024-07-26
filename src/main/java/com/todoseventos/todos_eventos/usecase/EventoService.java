package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.CategoriaDao;
import com.todoseventos.todos_eventos.dao.EnderecoDao;
import com.todoseventos.todos_eventos.dao.EventoDao;
import com.todoseventos.todos_eventos.dto.CategoriaEnum;
import com.todoseventos.todos_eventos.dto.CepResponse;
import com.todoseventos.todos_eventos.dto.EventoRequest;
import com.todoseventos.todos_eventos.dto.EventoResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import com.todoseventos.todos_eventos.utils.Validacao;
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
    private Validacao validacao;

    @Autowired
    private CepService cepService;

    @Autowired
    private EnderecoDao enderecoDao;

    @Autowired
    private CategoriaDao categoriaDao;

    public EventoResponse cadastrarNovoEvento(EventoRequest eventoRequest) {

        if (eventoRequest.getCategoria() == null) {
            throw new CustomException("Tipo de categoria inválido!");
        }
        // Verificar se a categoria é válida
        CategoriaModel categoriaModel = categoriaDao.findNomeCategoria(eventoRequest.getCategoria().name());

        if (Objects.isNull(categoriaModel)) {
            throw new CustomException("Categoria Inválida!");
        }

        // Validar o CEP
        if (!validacao.validarCep(eventoRequest.getCep())) {
            throw new CustomException("CEP inválido!");
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

    private EventoResponse mapearEvento(CategoriaModel categoria, EventoModel eventoSalvo, EnderecoModel enderecoSalvo) {
        return EventoResponse.builder()
                .idEvento(eventoSalvo.getIdEvento())
                .nome_evento(eventoSalvo.getNome_evento())
                .dataHora_evento(eventoSalvo.getDataHora_evento())
                .dataHora_eventofinal(eventoSalvo.getDataHora_eventofinal())
                .descricao(eventoSalvo.getDescricao())
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
            throw new CustomException("Erro ao buscar eventos: " + e.getMessage());
        }

        for (EventoModel eventoModel : eventoModelList) {
            CategoriaModel categoriaModel;
            EnderecoModel enderecoModel;

            try {
                categoriaModel = categoriaDao.findById(eventoModel.getId_categoria());
            } catch (Exception e) {
                throw new CustomException("Erro ao buscar categoria do evento: " + e.getMessage());
            }

            try {
                enderecoModel = enderecoDao.procurarPorIdEvento(eventoModel.getIdEvento())
                        .orElseThrow(() -> new CustomException("Endereço não encontrado para o evento: " + eventoModel.getNome_evento()));
            } catch (Exception e) {
                throw new CustomException("Erro ao buscar endereço do evento: " + e.getMessage());
            }

            EventoResponse eventoResponse = mapearEvento(categoriaModel, eventoModel, enderecoModel);
            eventoResponseList.add(eventoResponse);
        }
        return eventoResponseList;
    }

    public EventoResponse procurarEventoPorNome(String nomeEvento) {
        EventoModel eventoModel = eventoDao.procurarPorNome(nomeEvento);

        if (eventoModel == null) {
            throw new CustomException("Evento não encontrado!");
        }

        CategoriaModel categoriaModel = categoriaDao.findById(eventoModel.getId_categoria());
        EnderecoModel enderecoModel = enderecoDao.procurarPorIdEvento(eventoModel.getIdEvento())
                .orElseThrow(() -> new CustomException("Endereço não encontrado para o evento: " + nomeEvento));

        return mapearEvento(categoriaModel, eventoModel, enderecoModel);
    }

    public EventoResponse atualizarEvento(String nomeEvento, EventoRequest eventoRequest) {
        EventoModel eventoExistente = eventoDao.procurarPorNome(nomeEvento);

        if (eventoExistente == null) {
            throw new CustomException("Evento não encontrado!");
        }

        CategoriaModel categoriaModel = categoriaDao.findNomeCategoria(eventoRequest.getCategoria().name());

        if (categoriaModel == null) {
            throw new CustomException("Categoria inválida!");
        }

        // Validar o CEP
        if (!validacao.validarCep(eventoRequest.getCep())) {
            throw new CustomException("CEP inválido!");
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
                .orElseThrow(() -> new CustomException("Endereço não encontrado para o evento: " + nomeEvento));

        enderecoExistente.setRua(eventoRequest.getRua());
        enderecoExistente.setNumero(eventoRequest.getNumero());
        enderecoExistente.setBairro(eventoRequest.getBairro());
        enderecoExistente.setCidade(eventoRequest.getCidade());
        enderecoExistente.setCep(eventoRequest.getCep());
        enderecoExistente.setUf(eventoRequest.getUf());

        EnderecoModel enderecoAtualizado = enderecoDao.update(enderecoExistente);

        return mapearEvento(categoriaModel, eventoAtualizado, enderecoAtualizado);
    }

    public void excluirEvento(Long idEvento) {
        EventoModel eventoExistente = eventoDao.procurarPorId(idEvento);

        if (eventoExistente == null) {
            throw new CustomException("Evento não encontrado!");
        }

        enderecoDao.deleteByIdEvento(idEvento);
        eventoDao.deleteById(idEvento);
    }
}
