package com.demo.TeamMicroService.Controller;

import com.demo.TeamMicroService.Service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.TeamMicroService.DTO.TeamDTO;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/teams")
@Slf4j
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        try {
            TeamDTO team = teamService.getTeamById(id);
            if (team != null) {
                return ResponseEntity.ok(team);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching team: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamDTO teamDTO) {
        try {
            log.info("Received request to create team: {}", teamDTO);
            TeamDTO createdTeam = teamService.createTeam(teamDTO);
            return ResponseEntity.ok(createdTeam);
        } catch (RuntimeException e) {
            log.error("Error creating team: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error creating team: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long id, @RequestBody TeamDTO teamDTO) {
        try {
            TeamDTO updatedTeam = teamService.updateTeam(id, teamDTO);
            return ResponseEntity.ok(updatedTeam);
        } catch (RuntimeException e) {
            log.error("Error updating team: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        try {
            teamService.deleteTeam(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting team: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<TeamDTO> getTeamByName(@RequestParam String name) {
        try {
            TeamDTO team = teamService.findByName(name);
            if (team != null) {
                return ResponseEntity.ok(team);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error searching team by name: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}