package com.example.onlineStore.entity;

import com.example.onlineStore.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    @Enumerated(EnumType.STRING)
    private Gender gender; //MALE, FEMALE, OTHER
    private LocalDate birthDate;
    private String phone;
    private String email;

    @Basic(fetch = FetchType.LAZY)
    @ToString.Exclude
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // orphanRemoval = true to ensure addresses are deleted if they are removed from the user's address list.
    @ToString.Exclude
    private List<AddressEntity> addressList = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private CartEntity cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderEntity> orderList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
    EnumType.STRING stores values like "MALE", "FEMALE" in the database.
    Better than EnumType.ORDINAL, which stores numbers (0, 1, 2), making the DB less readable.


    User - Address (One-to-Many)
    A user can have multiple addresses (home, work, etc.).
    One address belongs to only one user.


    User - Order (One-to-Many)
    A user can place multiple orders.
    Each order belongs to one user.

    The field verificationCode stores a random, unique String which is generated in the registration process and will be used in the verification process. Once registered, the enabled status of a user is false (disabled) so the user can’t login if he has not activated account by checking email and click on the verification link embedded in the email.
 */