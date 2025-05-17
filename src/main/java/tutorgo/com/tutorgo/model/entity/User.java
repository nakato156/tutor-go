package tutorgo.com.tutorgo.model.entity;

import tutorgo.com.tutorgo.model.enums.AccountRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phoneNumber", name = "uk_account_phone_number")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String email;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false, length = 20)
    private String surename;

    @Column(nullable = false, length = 15)
    private String university;

    @Column(nullable = false, length = 15)
    private String password;

    @Column(nullable = false, precision = 19, scale = 4)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_usuario_rol"))
    private Rol rol;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}