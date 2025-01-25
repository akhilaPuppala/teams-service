package com.demo.TeamMicroService.Repository;


import com.demo.TeamMicroService.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByNameIgnoreCase(String name);
    List<Team> findByCaptainId(Long captainId);
    List<Team> findByManagerId(Long managerId);

}

