package com.hamza.Cinema.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Ticket {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private double prix;
    private String nomClient ;
    @Column(unique = false,nullable = true)
    private Integer codePayment;
    private boolean reservee ;
    @ManyToOne
    private Place place;
    @ManyToOne
    private Projection projection;

}
