package com.todoseventos.todos_eventos.exception;

public class CustomException extends RuntimeException {

    public static final String EVENTO_NAO_ENCONTRADO = "Evento não encontrado!";
    public static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado: ";
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
    public static final String PARTICIPACAO_NAO_ENCONTRADO_POR_ID = "Erro ao buscar participações por ID:";
    public static final String ERRO_SALVAR_PARTICIPACAO = "Erro ao salvar Participacao:";
    public static final String ERRO_ATUALIZAR_PARTICIPACAO = "Erro ao atualizar participação:";

    public static final String ERRO_BUSCAR_CLIENTE_CPF = "Erro ao buscar cliente por CPF:";

    public static final String ERRO_BUSCAR_CLIENTE_CNPJ = "Erro ao buscar cliente por CNPJ:";

    public static final String ERRO_ATUALIZAR_CLIENTE = "Erro ao atualizar cliente:";

    public static final String ERRO_BUSCAR_USUARIO_POR_EMAIL = "Erro ao buscar usuário por E-MAIL:";

    public static final String ERRO_SALVAR_CLIENTE = "Erro ao salvar cliente:";

    public static final String ERRO_LISTAR_TODOS = "Erro ao listar todos:";

    public static final String ERRO_SALVAR = "Erro ao salvar:";

    public static final String ERRO_ATUALIZAR = "Erro ao atualizar:";

    public static final String ERRO_EXCLUIR = "Erro ao excluir:";

    public static final String ERRO_BUSCAR_POR_ID = "Erro ao buscar por id:";

    public static final String ERRO_BUSCAR_POR_NOME = "Erro ao buscar por nome:";

    public static final String ERRO_ENVIAR_EMAIL = "Erro ao enviar e-mail:";
    public static final String ERRO_ENVIAR_EMAIL_CONFIRMACAO = "Erro ao enviar e-mail de confirmação:";
    public static final String ERRO_ENVIAR_EMAIL_CANCELAMENTO = "Erro ao enviar e-mail de cancelamento:";
    public static final String EMIAL_SENHA = "Email ou senha inválidos!";
    public static final String ERRO_INTERNO = "Ocorreu um erro interno. Por favor, tente novamente mais tarde.";

    public static final String INSCRICAO = "Inscrição realizada com sucesso!";

    public static final String CONFIRMACAO_INSCRICAO = "Participação no evento confirmada com sucesso!";

    public static final String CADASTRO_EVENTO = "Cadastro realizado com sucesso. Seu evento já está em divulgação!";
    public static final String EVENTO_ENCERRADO = "Evento encerrado com sucesso!";
    public static final String LISTA_EVENTO = "Lista de eventos recuperada com sucesso!";
    public static final String EVENTO_ENCONTRADO = "Evento encontrado com sucesso!";
    public static final String EVENTO_ATUALIZADO = "Evento atualizado com sucesso!";
    public static final String EXCLUIR_EVENTO = "Evento excluído com sucesso";

    public static final String CADASTRO_CLIENTE = "Cliente cadastrado com sucesso!";
    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado!";
    public static final String LISTA_CLIENTE = "Lista de clientes recuperada com sucesso!";
    public static final String CLIENTE_ATUALIZADO = "Cliente atualizado com sucesso!";
    public static final String TOKEN = "Não é possível definir a autenticação do usuário: {}!";
    public static final String TOKEN_EMAIL = "Usuário não encontrado com e-mail: ";

    public CustomException(String message){
        super(message);
    }
}
