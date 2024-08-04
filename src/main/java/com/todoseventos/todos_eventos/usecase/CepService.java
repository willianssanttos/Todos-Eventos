package com.todoseventos.todos_eventos.usecase;

import com.google.gson.Gson;
import com.todoseventos.todos_eventos.dto.CepResponse;
import com.todoseventos.todos_eventos.exception.CustomException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Service
public class CepService {

    /**
     * Consulta o CEP fornecido e retorna as informações de endereço correspondentes.
     * @param cep O CEP a ser consultado.
     * @return Um objeto CepResponse contendo os detalhes do endereço.
     * @throws CustomException se ocorrer um erro durante a consulta do CEP.
     */
    public CepResponse consultarCep(String cep) {
        try {
            // Cria a URL para a consulta do CEP
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            URLConnection connection = url.openConnection();

            // Abre a conexão e lê os dados do CEP
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            // Lê a resposta JSON linha por linha
            StringBuilder jsonCep = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                jsonCep.append(linha);
            }

            // Converte a resposta JSON para um objeto CepResponse usando Gson
            return new Gson().fromJson(jsonCep.toString(), CepResponse.class);
        } catch (Exception e) {
            // Lança uma exceção personalizada se ocorrer um erro
            throw new CustomException(CustomException.ERRO_BUSCAR_ENDERECO_EVENTO + e.getMessage());
        }
    }
}
