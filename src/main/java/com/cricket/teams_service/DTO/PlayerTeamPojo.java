package com.cricket.teams_service.DTO;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerTeamPojo {
    private int id;
    private String name;
    List<TeamPlayersPojo> teamPlayers;
}
