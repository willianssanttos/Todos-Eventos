package com.todoseventos.todos_eventos.usecase;


import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClienteModel user = jdbcTemplate.queryForObject("SELECT * FROM pessoa WHERE email = ?",
                new Object[]{email},
                (rs, rowNum) -> {
                    ClienteModel u = new ClienteModel();
                    u.setIdPessoa(rs.getInt("id_pessoa"));
                    u.setEmail(rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    return u;
                });

        if (user == null) {
            throw new CustomException(CustomException.TOKEN_EMAIL + email);
        }

        return UserDetailsImpl.build(user);
    }
}
