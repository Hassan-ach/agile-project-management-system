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
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;

    @Column(nullable = false) private String firstName;

    @Column(nullable = false) private String lastName;

    @Column(nullable = false, unique = true) private String email;

    @Column(nullable = false) private String password;

    @Column(name = "is_email_verified") private boolean emailVerified;

    @Column(name = "is_enabled") private boolean enabled;

    @Column(name = "is_account_locked") private boolean locked;

    @Column(name = "is_crendetial_expired") private boolean credentialsExpired;

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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
