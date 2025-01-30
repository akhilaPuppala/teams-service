package com.cricket.teams_service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cricket.teams_service.entity.Team;

@Repository
public interface TeamServiceRepository extends JpaRepository<Team,Integer>{

	List<Team> findByBookingId(int bookingId);

}
