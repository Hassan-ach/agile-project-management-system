package com.ensa.agile.infrastructure.persistence.jpa.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class UserJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    @Column(nullable = false) private String firstName;

    @Column(nullable = false) private String lastName;

    @Column(nullable = false, unique = true) private String email;

    @Column(nullable = false) private String password;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean emailVerified;

    @Column(name = "is_enabled", nullable = false) private Boolean enabled;

    @Column(name = "is_account_locked", nullable = false)
    private Boolean locked;

    @Column(name = "is_crendetial_expired", nullable = false)
    private Boolean credentialsExpired;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDate createdDate;

    public UserJpaEntity(String firstName, String lastName, String email,
                         String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.emailVerified = false;
        this.enabled = true;
        this.locked = false;
        this.credentialsExpired = false;
        this.createdDate = LocalDate.now();
    }
    public UserJpaEntity(UUID id, String firstName, String lastName,
                         String email, String password, Boolean emailVerified,
                         Boolean enabled, Boolean locked,
                         Boolean credentialsExpired, LocalDate createdDate) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified == null ? false : emailVerified;
        this.enabled = enabled == null ? false : enabled;
        this.locked = locked == null ? true : locked;
        this.credentialsExpired =
            credentialsExpired == null ? true : credentialsExpired;
        this.createdDate = createdDate;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
