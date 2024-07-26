package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.ClienteFisicaDao;
import com.todoseventos.todos_eventos.dao.ClienteJuridicaDao;
import com.todoseventos.todos_eventos.dao.EventoDao;
import com.todoseventos.todos_eventos.dao.ParticipacaoDao;
import com.todoseventos.todos_eventos.dto.ParticipacaoRequest;
import com.todoseventos.todos_eventos.dto.ParticipacaoResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteFisicaModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteJuridicaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ParticipacaoService {

    @Autowired
    private ParticipacaoDao participacaoDao;

    @Autowired
    private EventoDao eventoDao;

    @Autowired
    private ClienteFisicaDao clienteFisicaDao;

    @Autowired
    private ClienteJuridicaDao clienteJuridicaDao;

    @Autowired
    private EmailService emailService;

    public ParticipacaoResponse inscreverParticipante(ParticipacaoRequest request) {
        EventoModel evento = eventoDao.procurarPorId(request.getIdEvento());
        if (Objects.isNull(evento)) {
            throw new CustomException("Evento não encontrado!");
        }

        if (request.getCpf() != null) {
            ClienteFisicaModel pessoaFisica = clienteFisicaDao.findByCpf(request.getCpf());
            if (Objects.isNull(pessoaFisica)) {
                throw new CustomException("Pessoa Física não encontrada!");
            }
            ParticipacaoModel participacao = ParticipacaoModel.builder()
                    .cpf(request.getCpf())
                    .idEvento(request.getIdEvento())
                    .status("PENDENTE")
                    .build();
            ParticipacaoModel savedParticipacao = participacaoDao.save(participacao);

          emailService.enviarEmail(pessoaFisica.getEmail(), "Inscrição Confirmada", "Sua inscrição no evento " + evento.getNome_evento() + " foi confirmada.");
            return mapToResponse(savedParticipacao, pessoaFisica.getNome(), pessoaFisica.getEmail(), evento);
        } else if (request.getCnpj() != null) {
            ClienteJuridicaModel pessoaJuridica = clienteJuridicaDao.findByCnpj(request.getCnpj());
            if (Objects.isNull(pessoaJuridica)) {
                throw new CustomException("Pessoa Jurídica não encontrada!");
            }
            ParticipacaoModel participacao = ParticipacaoModel.builder()
                    .cnpj(request.getCnpj())
                    .idEvento(request.getIdEvento())
                    .status("PENDENTE")
                    .build();
            ParticipacaoModel savedParticipacao = participacaoDao.save(participacao);

          emailService.enviarEmail(pessoaJuridica.getEmail(), "Inscrição Confirmada", "Sua inscrição no evento " + evento.getNome_evento() + " foi confirmada.");
            return mapToResponse(savedParticipacao, pessoaJuridica.getNome(), pessoaJuridica.getEmail(), evento);
        } else {
            throw new CustomException("CPF ou CNPJ devem ser informados!");
        }
    }

    public List<ParticipacaoResponse> listarParticipacoes() {
        List<ParticipacaoModel> participacoes = participacaoDao.findAll();
        return participacoes.stream().map(participacao -> {
            EventoModel evento = eventoDao.procurarPorId(participacao.getIdEvento());
            if (participacao.getCpf() != null) {
                ClienteFisicaModel pessoaFisica = clienteFisicaDao.findByCpf(participacao.getCpf());
                return mapToResponse(participacao, pessoaFisica.getNome(), pessoaFisica.getEmail(), evento);
            } else {
                ClienteJuridicaModel pessoaJuridica = clienteJuridicaDao.findByCnpj(participacao.getCnpj());
                return mapToResponse(participacao, pessoaJuridica.getNome(), pessoaJuridica.getEmail(), evento);
            }
        }).collect(Collectors.toList());
    }

    public ParticipacaoResponse atualizarStatusParticipacao(Long idParticipacao, String status) {
        ParticipacaoModel participacao = participacaoDao.findById(idParticipacao);
        if (Objects.isNull(participacao)) {
            throw new CustomException("Participação não encontrada!");
        }

        participacao.setStatus(status);
        ParticipacaoModel updatedParticipacao = participacaoDao.update(participacao);

        EventoModel evento = eventoDao.procurarPorId(updatedParticipacao.getIdEvento());

        if (updatedParticipacao.getCpf() != null) {
            ClienteFisicaModel pessoaFisica = clienteFisicaDao.findByCpf(updatedParticipacao.getCpf());
            return mapToResponse(updatedParticipacao, pessoaFisica.getNome(), pessoaFisica.getEmail(), evento);
        } else {
            ClienteJuridicaModel pessoaJuridica = clienteJuridicaDao.findByCnpj(updatedParticipacao.getCnpj());
            return mapToResponse(updatedParticipacao, pessoaJuridica.getNome(), pessoaJuridica.getEmail(), evento);
        }
    }

    private ParticipacaoResponse mapToResponse(ParticipacaoModel participacao, String nome, String email, EventoModel evento) {
        return ParticipacaoResponse.builder()
                .idParticipacao(participacao.getIdParticipacao())
                .nomePessoa(nome)
                .emailPessoa(email)
                .cpfPessoa(participacao.getCpf())
                .cnpjPessoa(participacao.getCnpj())
                .nomeEvento(evento.getNome_evento())
                .dataHoraEvento(evento.getDataHora_evento())
                .dataHoraEventoFinal(evento.getDataHora_eventofinal())
                .status(participacao.getStatus())
                .build();
    }
}
