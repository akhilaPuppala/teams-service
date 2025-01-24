package com.demo.TeamMicroService.Service;


import com.demo.TeamMicroService.Entity.Team;
import com.demo.TeamMicroService.Repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found!"));
    }
    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

}
