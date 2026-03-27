package com.dormfix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*What each annotation does — read this once, you'll see these everywhere:

@Entity — tells JPA "this class is a database table"
@Table(name = "users") — the table will be named users in MySQL
@Data — Lombok generates getters, setters, toString automatically (saves ~50 lines)
@Builder — lets you create objects like User.builder().name("Rahul").build()
@NoArgsConstructor / @AllArgsConstructor — generates constructors
@Id + @GeneratedValue — this is the primary key, MySQL auto-increments it
@Column(unique = true) — no two users can have the same email
@Enumerated(EnumType.STRING) — stores "STUDENT" or "ADMIN" as text, not a number

 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String hostelBlock;

    private String roomNumber;
}
