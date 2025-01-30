package com.cricket.teams_service.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamDTO {
	
	private int id;
    private String name;
    private int bookingId;

}
