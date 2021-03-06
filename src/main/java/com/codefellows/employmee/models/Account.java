package com.codefellows.employmee.models;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean isBusiness;

    @ManyToMany(mappedBy = "candidates")
    Set<Account> businesses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="businesses_to_candidates",
            joinColumns={@JoinColumn(name="businesses")},
            inverseJoinColumns={@JoinColumn(name="candidate")}
    )
    Set<Account> candidates;

    protected Account(){}

    public Account(String username, String password, String email, String phone, boolean isBusiness) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.isBusiness = isBusiness;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<Account> getCandidates() {
        return candidates;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Long getId() {
        return id;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
