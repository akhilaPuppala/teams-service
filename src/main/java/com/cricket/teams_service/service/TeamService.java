package com.cricket.teams_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cricket.teams_service.entity.Team;
import com.cricket.teams_service.repo.TeamServiceRepository;



@Service
public class TeamService {

    @Autowired
	private TeamServiceRepository teamRepository;
    
    public List<Team> getTeamsByBookingId(int bookingId) {
        return teamRepository.findByBookingId(bookingId);
    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(int id) {
        return teamRepository.findById(id);
    }

    public void deleteTeamById(int id) {
        teamRepository.deleteById(id);
    }
}
