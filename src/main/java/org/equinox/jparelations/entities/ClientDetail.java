package org.equinox.jparelations.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "client_details")
public class ClientDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean premium;
    private Integer points;

    @OneToOne
    private Client client;

    public ClientDetail() {
    }

    public ClientDetail(Boolean premium, Integer points) {
        this.premium = premium;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", premium=" + premium +
                ", points=" + points +
                '}';
    }
}
