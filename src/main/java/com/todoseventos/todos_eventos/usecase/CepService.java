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

    public CepResponse consultarCep(String cep) {
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder jsonCep = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                jsonCep.append(linha);
            }

            return new Gson().fromJson(jsonCep.toString(), CepResponse.class);
        } catch (Exception e) {
            throw new CustomException("Erro ao consultar CEP: " + e.getMessage());
        }
    }
}