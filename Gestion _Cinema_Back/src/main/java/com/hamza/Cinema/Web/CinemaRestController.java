package com.hamza.Cinema.Web;

import com.hamza.Cinema.Entities.Film;
import com.hamza.Cinema.Entities.Ticket;
import com.hamza.Cinema.Repository.FilmRepository;
import com.hamza.Cinema.Repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaRestController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping(path="/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable Long id) throws IOException {
        Film f=filmRepository.findById(id).get();
        String photoName=f.getPhoto();
        File file=new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
        Path path= Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }

    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
        List<Ticket> listTickets =new ArrayList<>();
        ticketForm.getTickets().forEach(idTicket->{
            Ticket ticket= ticketRepository.findById(idTicket).get();
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setCodePayment(ticketForm.getCodePayment());
            ticket.setReservee(true);
            ticketRepository.save(ticket);
            listTickets.add(ticket);
        });
        return listTickets;
    }

}
@Data
class TicketForm{
    private String nomClient;
    private int codePayment;
    private List<Long> tickets=new ArrayList<>();

}