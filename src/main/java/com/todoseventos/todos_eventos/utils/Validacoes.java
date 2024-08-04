package com.todoseventos.todos_eventos.utils;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Validacoes {

    /**
     * Valida um CPF.
     * @param cpf O CPF a ser validado.
     * @return true se o CPF for válido, caso contrário false.
     */
    public boolean isCpfValid(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", ""); // Remove todos os caracteres não numéricos

        // Verifica se o CPF tem um formato inválido ou se todos os dígitos são iguais
        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") ||
                cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") ||
                cpf.equals("99999999999") || cpf.length() != 11)
            return false;

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            // Cálculo do primeiro dígito verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm += (num * peso);
                peso--;
            }
            r = 11 - (sm % 11);
            dig10 = (r == 10 || r == 11) ? '0' : (char) (r + 48);

            // Cálculo do segundo dígito verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm += (num * peso);
                peso--;
            }
            r = 11 - (sm % 11);
            dig11 = (r == 10 || r == 11) ? '0' : (char) (r + 48);

            // Verifica se os dígitos calculados correspondem aos dígitos verificadores do CPF
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida um CNPJ.
     * @param cnpj O CNPJ a ser validado.
     * @return true se o CNPJ for válido, caso contrário false.
     */
    public boolean isCnpjValid(String cnpj) {
        cnpj = cnpj.replaceAll("[^\\d]", ""); // Remove todos os caracteres não numéricos

        // Verifica se o CNPJ tem um formato inválido ou se todos os dígitos são iguais
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222") ||
                cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888") ||
                cnpj.equals("99999999999999") || cnpj.length() != 14)
            return false;

        char dig13, dig14;
        int sm, i, r, num, peso;

        try {
            // Cálculo do primeiro dígito verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm += (num * peso);
                peso++;
                if (peso == 10) peso = 2;
            }
            r = sm % 11;
            dig13 = (r == 0 || r == 1) ? '0' : (char) ((11 - r) + 48);

            // Cálculo do segundo dígito verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm += (num * peso);
                peso++;
                if (peso == 10) peso = 2;
            }
            r = sm % 11;
            dig14 = (r == 0 || r == 1) ? '0' : (char) ((11 - r) + 48);

            // Verifica se os dígitos calculados correspondem aos dígitos verificadores do CNPJ
            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida um endereço de e-mail.
     * @param email O e-mail a ser validado.
     * @return true se o e-mail for válido, caso contrário false.
     */
    public boolean validarEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9_+&*-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Formata um número de telefone celular.
     * @param numeroCelular O número de telefone celular a ser formatado.
     * @return O número de telefone celular formatado.
     */
    public String formatarNumeroTelefone(String numeroCelular) {
        String numeroApenasDigitos = numeroCelular.replaceAll("\\D", "");
        if (numeroApenasDigitos.length() == 10 || numeroApenasDigitos.length() == 11) {
            return MessageFormat.format("({0}) {1}-{2}",
                    numeroApenasDigitos.substring(0, 2),
                    numeroApenasDigitos.substring(2, 7),
                    numeroApenasDigitos.substring(7));
        }
        return numeroCelular;
    }

    /**
     * Valida um número de telefone celular.
     * @param numeroCelular O número de telefone celular a ser validado.
     * @return true se o número de telefone for válido, caso contrário false.
     */
    public boolean validarNumeroTelefone(String numeroCelular) {
        String regexNumeroCelular = "(\\(\\d{2}\\)\\d{4,5}-\\d{4})|\\d{11}|\\d{10}";
        Pattern pattern = Pattern.compile(regexNumeroCelular);
        Matcher matcher = pattern.matcher(numeroCelular);
        return matcher.matches();
    }

    /**
     * Valida uma data de nascimento.
     * @param dataNascimento A data de nascimento a ser validada.
     * @return true se a data de nascimento for válida, caso contrário false.
     */
    public boolean validarDataNascimento(String dataNascimento) {
        String regexDataNascimento = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d\\d$";
        Pattern pattern = Pattern.compile(regexDataNascimento);
        Matcher matcher = pattern.matcher(dataNascimento);
        return matcher.matches();
    }

    /**
     * Valida um CEP.
     * @param cep O CEP a ser validado.
     * @return true se o CEP for válido, caso contrário false.
     */
    public boolean validarCep(String cep) {
        String regex = "^[0-9]{5}-[0-9]{3}$";
        return cep.matches(regex);
    }
}