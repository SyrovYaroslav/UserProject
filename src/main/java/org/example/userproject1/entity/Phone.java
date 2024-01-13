package org.example.userproject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "phone_number")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "phone")
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
