package com.demo.TeamMicroService.DTO;

import lombok.Data;

@Data
public class PlayerResponseDTO {
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private String country;
}