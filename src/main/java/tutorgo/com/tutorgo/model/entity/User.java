package tutorgo.com.tutorgo.model.entity;

import tutorgo.com.tutorgo.model.enums.AccountRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
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

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String surename;

    @Column(nullable = false, length = 55)
    private String university;

    @Column(nullable = false, length = 55)
    private String password;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_usuario_rol"))
    private Rol rol;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}