package com.todoseventos.todos_eventos.exception;

public class CustomException extends RuntimeException {

    public static final String EVENTO_NAO_ENCONTRADO = "Evento não encontrado!";
    public static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado para o evento: ";
    public static final String PESSOA_FISICA_NAO_ENCONTRADA = "Pessoa Física não encontrada!";
    public static final String PESSOA_JURIDICA_NAO_ENCONTRADA = "Pessoa Jurídica não encontrada!";
    public static final String CPF_OU_CNPJ_NAO_INFORMADOS = "CPF ou CNPJ devem ser informados!";
    public static final String PARTICIPACAO_NAO_ENCONTRADA = "Participação não encontrada!";
    public static final String TIPO_CATEGORIA_INVALIDO = "Tipo de categoria inválido!";
    public static final String CATEGORIA_INVALIDA = "Categoria Inválida!";
    public static final String CEP_INVALIDO = "CEP inválido!";
    public static final String ERRO_BUSCAR_EVENTOS = "Erro ao buscar eventos: ";
    public static final String ERRO_BUSCAR_CATEGORIA_EVENTO = "Erro ao buscar categoria do evento: ";
    public static final String ERRO_BUSCAR_ENDERECO_EVENTO = "Erro ao buscar endereço do evento: ";
    public static final String EMAIL_INVALIDO = "Email inválido!";
    public static final String TELEFONE_INVALIDO = "Número de celular inválido!";
    public static final String DATA_NASCIMENTO_INVALIDA = "Data de nascimento inválida!";
    public static final String CPF_INVALIDO = "CPF inválido! Verifiquei o dado informado.";
    public static final String CPF_JA_CADASTRADO = "CPF já cadastrado!";
    public static final String CNPJ_INVALIDO = "CNPJ inválido! Verifiquei o dado informado.";
    public static final String CNPJ_JA_CADASTRADO = "CNPJ já cadastrado!";
    public static final String IDENTIFICADOR_INVALIDO = "Identificador inválido!";
    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado!";

    public CustomException(String message){
        super(message);
    }
}
