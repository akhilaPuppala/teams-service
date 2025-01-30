package com.cricket.teams_service.controller;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cricket.teams_service.DTO.TeamDTO;
import com.cricket.teams_service.entity.Team;
import com.cricket.teams_service.service.TeamService;

@CrossOrigin("*")
@RestController
@RequestMapping("/teams")

public class TeamController {
	
	@Autowired
	private TeamService teamService;
	
	@GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeam1ById(@PathVariable int teamId) {
        return teamService.getTeamById(teamId)
                .map(team -> ResponseEntity.ok(new TeamDTO(team.getId(), team.getName(), team.getBookingId())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Fetch all teams by booking ID and return DTO list
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<TeamDTO>> getTeamsByBookingId(@PathVariable int bookingId) {
        List<TeamDTO> teamDTOList = teamService.getTeamsByBookingId(bookingId).stream()
                .map(team -> new TeamDTO(team.getId(), team.getName(), team.getBookingId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(teamDTOList);
    }

	@PostMapping
    public ResponseEntity<Team> createOrUpdateTeam(@RequestBody Team team) {
        Team savedTeam = teamService.saveTeam(team);
        return ResponseEntity.ok(savedTeam);
    }

    // Get all teams
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // Get a team by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable int id) {
        return teamService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a team by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable int id) {
        teamService.deleteTeamById(id);
        return ResponseEntity.noContent().build();
   
    }
}