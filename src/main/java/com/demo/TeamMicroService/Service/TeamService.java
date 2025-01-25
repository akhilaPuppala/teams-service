package com.demo.TeamMicroService.Service;

import com.demo.TeamMicroService.DTO.PlayerResponseDTO;
import com.demo.TeamMicroService.DTO.TeamDTO;
import com.demo.TeamMicroService.Entity.Team;
import com.demo.TeamMicroService.Repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    public TeamService(TeamRepository teamRepository, WebClient.Builder webClientBuilder) {
        this.teamRepository = teamRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found!"));
        return convertToDTO(team);
    }

    public TeamDTO findByName(String name) {
        Team team = teamRepository.findByNameIgnoreCase(name);
        return team != null ? convertToDTO(team) : null;
    }

    public TeamDTO createTeam(TeamDTO teamDTO) {
        try {
            log.info("Creating new team with name: {}, captainId: {}, managerId: {}",
                    teamDTO.getName(), teamDTO.getCaptainId(), teamDTO.getManagerId());

            // Validate input
            validateTeamInput(teamDTO);

            // Check if team name already exists
            Team existingTeamWithName = teamRepository.findByNameIgnoreCase(teamDTO.getName());
            if (existingTeamWithName != null) {
                throw new RuntimeException("Team with name '" + teamDTO.getName() + "' already exists");
            }

            // Check if captain is already assigned
            if (isCaptainAssignedToAnotherTeam(teamDTO.getCaptainId(), null)) {
                throw new RuntimeException("Captain (ID: " + teamDTO.getCaptainId() +
                        ") is already assigned to another team");
            }

            // Check if manager is already assigned
            if (isManagerAssignedToAnotherTeam(teamDTO.getManagerId(), null)) {
                throw new RuntimeException("Manager (ID: " + teamDTO.getManagerId() +
                        ") is already assigned to another team");
            }

            Team team = convertToEntity(teamDTO);
            Team savedTeam = teamRepository.save(team);
            log.info("Successfully created team with ID: {}", savedTeam.getId());

            return convertToDTO(savedTeam);
        } catch (RuntimeException e) {
            log.error("Error creating team: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating team: {}", e.getMessage());
            throw new RuntimeException("Internal server error while creating team");
        }
    }

    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        try {
            Team existingTeam = teamRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Team not found!"));

            // Validate input
            validateTeamInput(teamDTO);

            // Check if new name conflicts with existing team (excluding current team)
            if (!teamDTO.getName().equals(existingTeam.getName())) {
                Team teamWithSameName = teamRepository.findByNameIgnoreCase(teamDTO.getName());
                if (teamWithSameName != null && !teamWithSameName.getId().equals(id)) {
                    throw new RuntimeException("Team with name '" + teamDTO.getName() + "' already exists");
                }
            }

            // Check if captain is already assigned to another team
            if (!teamDTO.getCaptainId().equals(existingTeam.getCaptainId())) {
                if (isCaptainAssignedToAnotherTeam(teamDTO.getCaptainId(), id)) {
                    throw new RuntimeException("Captain is already assigned to another team");
                }
            }

            // Check if manager is already assigned to another team
            if (!teamDTO.getManagerId().equals(existingTeam.getManagerId())) {
                if (isManagerAssignedToAnotherTeam(teamDTO.getManagerId(), id)) {
                    throw new RuntimeException("Manager is already assigned to another team");
                }
            }

            updateTeamFields(existingTeam, teamDTO);
            Team savedTeam = teamRepository.save(existingTeam);
            return convertToDTO(savedTeam);
        } catch (RuntimeException e) {
            log.error("Error updating team: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    private void validateTeamInput(TeamDTO teamDTO) {
        if (teamDTO == null) {
            throw new RuntimeException("Team data cannot be null");
        }
        if (teamDTO.getName() == null || teamDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("Team name is required");
        }
        if (teamDTO.getCaptainId() == null) {
            throw new RuntimeException("Captain ID is required");
        }
        if (teamDTO.getManagerId() == null) {
            throw new RuntimeException("Manager ID is required");
        }
        if (!verifyPlayerExists(teamDTO.getCaptainId())) {
            throw new RuntimeException("Captain not found in Player service");
        }
        if (!verifyPlayerExists(teamDTO.getManagerId())) {
            throw new RuntimeException("Manager not found in Player service");
        }
    }


    private boolean isCaptainAssignedToAnotherTeam(Long captainId, Long excludeTeamId) {
        List<Team> teamsWithCaptain = teamRepository.findByCaptainId(captainId);
        if (excludeTeamId != null) {
            teamsWithCaptain = teamsWithCaptain.stream()
                    .filter(team -> !team.getId().equals(excludeTeamId))
                    .collect(Collectors.toList());
        }
        return !teamsWithCaptain.isEmpty();
    }

    private boolean isManagerAssignedToAnotherTeam(Long managerId, Long excludeTeamId) {
        List<Team> teamsWithManager = teamRepository.findByManagerId(managerId);
        if (excludeTeamId != null) {
            teamsWithManager = teamsWithManager.stream()
                    .filter(team -> !team.getId().equals(excludeTeamId))
                    .collect(Collectors.toList());
        }
        return !teamsWithManager.isEmpty();
    }

    private TeamDTO convertToDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setManagerId(team.getManagerId());
        dto.setCaptainId(team.getCaptainId());
        return dto;
    }

    private Team convertToEntity(TeamDTO dto) {
        Team team = new Team();
        team.setName(dto.getName());
        team.setManagerId(dto.getManagerId());
        team.setCaptainId(dto.getCaptainId());
        return team;
    }

    private void updateTeamFields(Team existingTeam, TeamDTO teamDTO) {
        existingTeam.setName(teamDTO.getName());
        existingTeam.setManagerId(teamDTO.getManagerId());
        existingTeam.setCaptainId(teamDTO.getCaptainId());
    }
    private boolean verifyPlayerExists(Long playerId) {
        try {
            log.info("Verifying player with ID: {}", playerId);
            String url = String.format("%s/%d", playerServiceUrl, playerId);
            log.debug("Making request to: {}", url);

            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(PlayerResponseDTO.class)  // Change to proper DTO
                    .map(response -> true)
                    .onErrorResume(WebClientResponseException.NotFound.class, error -> {
                        log.warn("Player {} not found", playerId);
                        return Mono.just(false);
                    })
                    .block();

        } catch (Exception e) {
            log.error("Error verifying player {}: {}", playerId, e.getMessage());
            return false;
        }
    }
}