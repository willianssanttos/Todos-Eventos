package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private Integer id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Construtor da classe UserDetailsImpl.
     * @param id O ID do usuário.
     * @param email O e-mail do usuário.
     * @param password A senha do usuário.
     * @param authorities As autoridades (roles) do usuário.
     */

    public UserDetailsImpl(Integer id, String email, String password, Collection<? extends GrantedAuthority> authorities) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Cria uma instância de UserDetailsImpl a partir de um objeto ClienteModel.
     * @param usuario O objeto ClienteModel contendo os detalhes do usuário.
     * @return Uma instância de UserDetailsImpl.
     */
    public static UserDetailsImpl build(ClienteModel usuario) {
        return new UserDetailsImpl(
                usuario.getIdPessoa(),
                usuario.getEmail(),
                usuario.getSenha(),
                new ArrayList<>());
    }

    /**
     * Retorna as autoridades (roles) do usuário.
     * @return Uma coleção de GrantedAuthority representando as autoridades do usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Retorna a senha do usuário.
     * @return A senha do usuário.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retorna o nome de usuário (e-mail).
     * @return O e-mail do usuário.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica se a conta do usuário está expirada.
     * @return true, se a conta não estiver expirada; false, caso contrário.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário está bloqueada.
     * @return true, se a conta não estiver bloqueada; false, caso contrário.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais do usuário estão expiradas.
     * @return true, se as credenciais não estiverem expiradas; false, caso contrário.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário está habilitada.
     * @return true, se a conta estiver habilitada; false, caso contrário.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
