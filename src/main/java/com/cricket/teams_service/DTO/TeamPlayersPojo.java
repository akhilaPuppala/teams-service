package com.cricket.teams_service.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamPlayersPojo {
    private int tPlayerId;
    private int teamId; // Foreign Key referencing Teams.id
    private int playerId; // Foreign Key referencing Players.id
    private int playerRuns;
    private int playerWickets;
    private int playerCatches;

}
