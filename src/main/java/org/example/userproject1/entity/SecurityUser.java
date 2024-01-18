package org.example.userproject1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class SecurityUser {
    @Id
    private String userId;
    @NotNull
    @Column(unique = true)
    @Size(min = 1, max = 100)
    private String username;
    @NotNull
    @Size(min = 1, max = 100)
    private String password;
    @Size(min = 1, max = 100)
    private String email;

    @ElementCollection(targetClass = SecurityUserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "security_user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<SecurityUserRole> roles;
}
