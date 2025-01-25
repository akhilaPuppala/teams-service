package com.demo.TeamMicroService.DTO;


import lombok.Data;

@Data
public class TeamDTO {
    private Long id;
    private String name;    // This should match the field name expected by TeamPlayer service
    private Long managerId;
    private Long captainId;
}