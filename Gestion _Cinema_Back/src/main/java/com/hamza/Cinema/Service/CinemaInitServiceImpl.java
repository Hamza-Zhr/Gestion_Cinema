package com.hamza.Cinema.Service;

import com.hamza.Cinema.Entities.*;
import com.hamza.Cinema.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProgectionRepository progectionRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(v->{
            Ville ville=new Ville();
            ville.setName(v);
            villeRepository.save(ville);
        });

    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v->{
            Stream.of("MegaRama","IMAX","FOUNOUN","CHAHRAZAD","DAOULIZ").forEach(c->{
                Cinema cinema=new Cinema();
                cinema.setName(c);
                cinema.setNombreSalles((int) (3+Math.random()*7));
                cinema.setVille(v);
                cinemaRepository.save(cinema);
            });
        });

    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(c->{
            for(int i=0;i<c.getNombreSalles();i++){
                Salle salle=new Salle();
                salle.setName("Salle"+(i+1));
                salle.setCinema(c);
                salle.setNombrePlaces((int) (15+Math.random()*20));
                salleRepository.save(salle);
            };
        });

    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(s->{
            for(int i=0;i<s.getNombrePlaces();i++){
                Place place=new Place();
                place.setNumero((i+1));
                place.setSalle(s);
                placeRepository.save(place);
            };
        });

    }

    @Override
    public void initSeances() {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(sc->{
            Seance seance=new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(sc));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            seanceRepository.save(seance);
        });

    }

    @Override
    public void initCategories() {
        Stream.of("Action","Histoire","Drama","Fiction").forEach(cat->{
            Categorie categorie=new Categorie();
            categorie.setName(cat);
            categorieRepository.save(categorie);
        });

    }

    @Override
    public void initFilms() {
        double[] durees =new double[]{1,1.5,2,2.5,3};
        List<Categorie> categories=categorieRepository.findAll();
        Stream.of("Game of Thrones","Spider man","Vakings","Prison Break","Lost").forEach(f->{
            Film film=new Film();
            film.setTitre(f);
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setPhoto(f.replaceAll(" ","")+".jpg");
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            filmRepository.save(film);
        });

    }


    @Override
    public void initProjections() {
        double[] prices =new double[]{30,50,60,70,90,100};
        List<Film>films=filmRepository.findAll();
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    int index=new Random().nextInt(films.size());
                    Film film=films.get(index);
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection=new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            progectionRepository.save(projection);
                        });
                });
            });
        });


    }

    @Override
    public void initTickets() {
        progectionRepository.findAll().forEach(p->{
            p.getSalle().getPlaces().forEach(place -> {
                Ticket ticket=new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(p.getPrix());
                ticket.setProjection(p);
                ticket.setReservee(false);
                ticketRepository.save(ticket);
            });
        });
    }
}
