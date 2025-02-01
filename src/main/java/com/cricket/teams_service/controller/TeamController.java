package com.cricket.teams_service.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.cricket.teams_service.DTO.PlayerTeamPojo;
import com.cricket.teams_service.DTO.TeamDTO;
import com.cricket.teams_service.DTO.TeamPlayersPojo;
import com.cricket.teams_service.entity.Team;
import com.cricket.teams_service.service.TeamService;

import reactor.core.publisher.Mono;

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

    //  @GetMapping("/stadiumslot/{stadiumId}/{date}")
    // public ResponseEntity<StadiumSlotPojo> getByStadiumId(@PathVariable int stadiumId,@PathVariable LocalDate date){
    // 	List<StadiumSlot> stads = service.getByStadiumId(stadiumId).orElse(null);
    // 	StadiumSlotPojo stadiumSlotPojo= new StadiumSlotPojo();
    // 	StadiumSlotPojo stadiumSlotPojoFinal= new StadiumSlotPojo();
    // 	stadiumSlotPojo.setStadiumId(stadiumId);
    // 	stadiumSlotPojoFinal.setStadiumId(stadiumId);
    // 	List<PojoSlotStadium> slots=new ArrayList();
    // 	List<PojoSlotStadium> slotsFinal=new ArrayList();
    //     for(StadiumSlot row:stads) {
    // 		int  id=row.getSlotId();
    // 		PojoSlotStadium ss=new PojoSlotStadium();
    // 		ss.setStadiumSlotId(row.getId());
    // 	    RestClient restClient = RestClient.create();
	// 	    SlotPojo slot = restClient
	// 			.get()
	// 			.uri("http://localhost:1719/slots/"+id)
	// 			.retrieve()
	// 			.body(SlotPojo.class);
	// 	    ss.setSlot(slot);
	// 	    slots.add(ss);
    // 	}
    //     stadiumSlotPojo.setSlots(slots);
    //     WebClient webClient = WebClient.create();
    //     // Assuming BookingPojo is a defined class
    //     Mono<List<BookingPojo>> bookingsMono = webClient
    //             .get()
    //             .uri("http://localhost:1721/bookings/"+stadiumId+"/"+date)
    //             .retrieve()
    //             .bodyToMono(new ParameterizedTypeReference<List<BookingPojo>>() {});
    //     // Blocking for simplicity (avoid in production)
    //     List<BookingPojo> bookings = bookingsMono.block();
	// 	for(PojoSlotStadium stadslot:stadiumSlotPojo.getSlots()) {
	// 		boolean found=false;
	// 		for(BookingPojo booking:bookings) {
	// 			  if(stadslot.getStadiumSlotId()==booking.getStadiumSlotId()) {
	// 				  found=true;
	// 				  break;
	// 			  } 
	// 		}
	// 		if(!found) {
	// 			slotsFinal.add(stadslot);
	// 		}
	// 	}
	// 	stadiumSlotPojoFinal.setSlots(slotsFinal);
    // 	return new ResponseEntity<StadiumSlotPojo>(stadiumSlotPojoFinal,HttpStatus.OK);
    // }
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PlayerTeamPojo>> getTeamPlayersByBookingId(@PathVariable int bookingId) {
        List<Team> teams=new ArrayList<>();
        List<PlayerTeamPojo> allTeamsPlayers=new ArrayList<>();
        teams=teamService.getTeamsByBookingId(bookingId);
        RestClient restClient = RestClient.create();
        for(Team team:teams){
            PlayerTeamPojo Team=new PlayerTeamPojo();
            List<TeamPlayersPojo> teamPlayers=new ArrayList<>();
            teamPlayers=restClient
            .get()
            .uri("http://localhost:2026/team-players/team/"+team.getId())
            .retrieve()
            .body(List.class);
             Team.setId(team.getId());
             Team.setName(team.getName());
             Team.setTeamPlayers(teamPlayers);
             allTeamsPlayers.add(Team);
        }
        return new ResponseEntity<List<PlayerTeamPojo>>(allTeamsPlayers,HttpStatus.OK);
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