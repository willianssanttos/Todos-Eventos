package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.ClienteDao;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetalheUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteDao clienteDao;

    /**
     * Carrega os detalhes do usuário pelo nome de usuário (e-mail).
     * @param username O e-mail do usuário.
     * @return Um objeto UserDetails contendo os detalhes do usuário.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Procura o usuário pelo e-mail
        ClienteModel usuario = clienteDao.procurarPorEmail(username);
        if (usuario == null) {
            // Lança exceção se o usuário não for encontrado
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
        // Constrói e retorna os detalhes do usuário
        return UserDetailsImpl.build(usuario);
    }
}
