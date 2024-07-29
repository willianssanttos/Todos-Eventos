package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.*;
import com.todoseventos.todos_eventos.dto.ParticipacaoRequest;
import com.todoseventos.todos_eventos.dto.ParticipacaoResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteFisicaModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteJuridicaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ParticipacaoService {

    private static final Logger logger = LoggerFactory.getLogger(ParticipacaoService.class);

    @Autowired
    private ParticipacaoDao participacaoDao;

    @Autowired
    private EventoDao eventoDao;

    @Autowired
    private EnderecoDao enderecoDao;

    @Autowired
    private ClienteFisicaDao clienteFisicaDao;

    @Autowired
    private ClienteJuridicaDao clienteJuridicaDao;

    @Autowired
    private EmailService emailService;

    public ParticipacaoResponse inscreverParticipante(ParticipacaoRequest request) {
        logger.info("Iniciando inscrição do participante: {}", request);

        EventoModel evento = eventoDao.procurarPorId(request.getIdEvento());
        if (Objects.isNull(evento)) {
            throw new CustomException("Evento não encontrado!");
        }

        EnderecoModel endereco = enderecoDao.procurarPorIdEvento(evento.getIdEvento())
                .orElseThrow(() -> new CustomException("Endereço não encontrado para o evento: " + evento.getNome_evento()));

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

            String localEvento = endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getBairro() + ", " + endereco.getCidade() + ", " + endereco.getUf();
            String linkConfirmacao = "http://seusite.com/api/participacao/confirmacao/" + savedParticipacao.getIdParticipacao();

            emailService.enviarEmail(pessoaFisica.getEmail(), "Inscrição Confirmada", pessoaFisica.getNome(), evento.getNome_evento(), evento.getDataHora_evento(), localEvento, linkConfirmacao);
            return PessoaFisica(savedParticipacao, pessoaFisica, evento, endereco);
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
            logger.info("Salvando participação para pessoa jurídica: {}", participacao);
            ParticipacaoModel savedParticipacao = participacaoDao.save(participacao);
            logger.info("Participação salva: {}", savedParticipacao);

            String localEvento = endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getBairro() + ", " + endereco.getCidade() + ", " + endereco.getUf();
            String linkConfirmacao = "http://seusite.com/api/participacao/confirmacao/" + savedParticipacao.getIdParticipacao();

            emailService.enviarEmail(pessoaJuridica.getEmail(), "Inscrição Confirmada", pessoaJuridica.getNome(), evento.getNome_evento(), evento.getDataHora_evento(), localEvento, linkConfirmacao);
            return PessoaJuridica(savedParticipacao, pessoaJuridica, evento, endereco);
        } else {
            throw new CustomException("CPF ou CNPJ devem ser informados!");
        }
    }

    public ParticipacaoResponse confirmarParticipacao(Long idParticipacao) {
        logger.info("Confirmando participação com ID: {}", idParticipacao);
        ParticipacaoModel participacao = participacaoDao.findById(idParticipacao);
        if (Objects.isNull(participacao)) {
            throw new CustomException("Participação não encontrada!");
        }

        participacao.setStatus("CONFIRMADO");
        ParticipacaoModel updatedParticipacao = participacaoDao.update(participacao);
        logger.info("Participação confirmada: {}", updatedParticipacao);

        EventoModel evento = eventoDao.procurarPorId(updatedParticipacao.getIdEvento());
        EnderecoModel endereco = enderecoDao.procurarPorIdEvento(evento.getIdEvento())
                .orElseThrow(() -> new CustomException("Endereço não encontrado para o evento: " + evento.getNome_evento()));

        if (updatedParticipacao.getCpf() != null) {
            ClienteFisicaModel pessoaFisica = clienteFisicaDao.findByCpf(updatedParticipacao.getCpf());
            emailService.enviarEmailConfirmacao(pessoaFisica.getEmail(), "Confirmação de Participação", pessoaFisica.getNome(), evento.getNome_evento(), evento.getDataHora_evento(), endereco);
            return PessoaFisica(updatedParticipacao, pessoaFisica, evento, endereco);
        } else {
            ClienteJuridicaModel pessoaJuridica = clienteJuridicaDao.findByCnpj(updatedParticipacao.getCnpj());
            emailService.enviarEmailConfirmacao(pessoaJuridica.getEmail(), "Confirmação de Participação", pessoaJuridica.getNome(), evento.getNome_evento(), evento.getDataHora_evento(), endereco);
            return PessoaJuridica(updatedParticipacao, pessoaJuridica, evento, endereco);
        }
    }

    public static ParticipacaoResponse PessoaFisica(ParticipacaoModel participacao, ClienteFisicaModel pessoaFisica, EventoModel evento, EnderecoModel endereco) {
        return ParticipacaoResponse.builder()
                .nomePessoa(pessoaFisica.getNome())
                .emailPessoa(pessoaFisica.getEmail())
                .cpfPessoa(participacao.getCpf())
                .nomeEvento(evento.getNome_evento())
                .dataHoraEvento(evento.getDataHora_evento())
                .dataHoraEventoFinal(evento.getDataHora_eventofinal())
                .status(participacao.getStatus())
                .localEvento(endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getBairro() + ", " + endereco.getCidade() + ", " + endereco.getUf())
                .build();
    }

    public static ParticipacaoResponse PessoaJuridica(ParticipacaoModel participacao, ClienteJuridicaModel pessoaJuridica, EventoModel evento, EnderecoModel endereco) {
        return ParticipacaoResponse.builder()
                .nomePessoa(pessoaJuridica.getNome())
                .emailPessoa(pessoaJuridica.getEmail())
                .cnpjPessoa(participacao.getCnpj())
                .nomeEvento(evento.getNome_evento())
                .dataHoraEvento(evento.getDataHora_evento())
                .dataHoraEventoFinal(evento.getDataHora_eventofinal())
                .status(participacao.getStatus())
                .localEvento(endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getBairro() + ", " + endereco.getCidade() + ", " + endereco.getUf())
                .build();
    }
}