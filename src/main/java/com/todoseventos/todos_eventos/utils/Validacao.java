package com.todoseventos.todos_eventos.utils;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Validacao {

    public boolean isCpfValid(String cpf) {

        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.equals("00000000000") ||

                cpf.equals("11111111111") ||

                cpf.equals("22222222222") ||

                cpf.equals("33333333333") ||

                cpf.equals("44444444444") ||

                cpf.equals("55555555555") ||

                cpf.equals("66666666666") ||

                cpf.equals("77777777777") ||

                cpf.equals("88888888888") ||

                cpf.equals("99999999999") ||

                cpf.length() != 11)

            return false;

        char dig10, dig11;

        int sm, i, r, num, peso;

        try {

            // Cálculo do primeiro Digito Verificador

            sm = 0;

            peso = 10;

            for (i = 0; i < 9; i++) {

                num = (int) (cpf.charAt(i) - 48);

                sm = sm + (num * peso);

                peso = peso - 1;

            }

            r = 11 - (sm % 11);

            if ((r == 10) || (r == 11))

                dig10 = '0';

            else

                dig10 = (char) (r + 48);

            // Cálculo do segundo Digito Verificador

            sm = 0;

            peso = 11;

            for (i = 0; i < 10; i++) {

                num = (int) (cpf.charAt(i) - 48);

                sm = sm + (num * peso);

                peso = peso - 1;

            }

            r = 11 - (sm % 11);

            if ((r == 10) || (r == 11))

                dig11 = '0';

            else

                dig11 = (char) (r + 48);

            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));

        } catch (Exception e) {

            return false;

        }

    }

    public boolean isCnpjValid(String cnpj) {

        cnpj = cnpj.replaceAll("[^\\d]", "");

        if (cnpj.equals("00000000000000") ||

                cnpj.equals("11111111111111") ||

                cnpj.equals("22222222222222") ||

                cnpj.equals("33333333333333") ||

                cnpj.equals("44444444444444") ||

                cnpj.equals("55555555555555") ||

                cnpj.equals("66666666666666") ||

                cnpj.equals("77777777777777") ||

                cnpj.equals("88888888888888") ||

                cnpj.equals("99999999999999") ||

                cnpj.length() != 14)

            return false;

        char dig13, dig14;

        int sm, i, r, num, peso;

        try {

            // Cálculo do primeiro Digito Verificador

            sm = 0;

            peso = 2;

            for (i = 11; i >= 0; i--) {

                num = (int) (cnpj.charAt(i) - 48);

                sm = sm + (num * peso);

                peso = peso + 1;

                if (peso == 10) peso = 2;

            }

            r = sm % 11;

            if ((r == 0) || (r == 1))

                dig13 = '0';

            else

                dig13 = (char) ((11 - r) + 48);

            // Cálculo do segundo Digito Verificador

            sm = 0;

            peso = 2;

            for (i = 12; i >= 0; i--) {

                num = (int) (cnpj.charAt(i) - 48);

                sm = sm + (num * peso);

                peso = peso + 1;

                if (peso == 10) peso = 2;

            }

            r = sm % 11;

            if ((r == 0) || (r == 1))

                dig14 = '0';

            else

                dig14 = (char) ((11 - r) + 48);

            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));

        } catch (Exception e) {

            return false;
        }
    }

    public boolean validarEmail(String emial) {
        //Define a expressão regular para validar o formato do e-mail
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9_+&*-]+\\.)+[a-zA-Z]{2,7}$";

        //Compila a expressão regular
        Pattern pattern = Pattern.compile(regex);

        //Cria um objeto Matcher para o e-mail forncido
        Matcher matcher = pattern.matcher(emial);

        //Verifica se o e-mail corresponde ao padrão definido
        return matcher.matches();
    }

    // Método para formatar o número de celular
    public String formatarNumeroTelefone(String numeroCelular) {
        // Remove todos os caracteres não numéricos
        String numeroApenasDigitos = numeroCelular.replaceAll("\\D", "");

        // Verifica se o número tem 10 ou 11 dígitos
        if (numeroApenasDigitos.length() == 10 || numeroApenasDigitos.length() == 11) {
            return MessageFormat.format("({0}) {1}-{2}",
                    numeroApenasDigitos.substring(0, 2),
                    numeroApenasDigitos.substring(2, 7),
                    numeroApenasDigitos.substring(7));
        }

        return numeroCelular;
    }

    // Método para validar número de celular
    public boolean validarNumeroTelefone(String numeroCelular) {
        // Formato aceito: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX ou XXXXXXXXXXX
        String regexNumeroCelular = "(\\(\\d{2}\\)\\d{4,5}-\\d{4})|\\d{11}|\\d{10}";

        Pattern pattern = Pattern.compile(regexNumeroCelular);
        Matcher matcher = pattern.matcher(numeroCelular);

        return matcher.matches();
    }

    // Método para validar data de nascimento
    public boolean validarDataNascimento(String dataNascimento) {
        // Formato aceito: DD/MM/YYYY
        String regexDataNascimento = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d\\d$";

        Pattern pattern = Pattern.compile(regexDataNascimento);
        Matcher matcher = pattern.matcher(dataNascimento);

        return matcher.matches();
    }

    // Método para validar o CEP
    public boolean validarCep(String cep) {
        String regex = "^[0-9]{5}-[0-9]{3}$";
        return cep.matches(regex);
    }
}
