package com.demo.TeamMicroService.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", unique = true, nullable = false)
    private String name;

    @Column(name = "manager_id", nullable = false)
    private Long managerId; // Refers to Player ID

    @Column(name = "captain_id", nullable = false)
    private Long captainId; // Refers to Player ID
}

