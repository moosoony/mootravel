package kr.co.mootravle.Voter;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.User.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "travel_voter")
@Getter
@Setter
@Entity
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Travel travel;

    @ManyToOne
    private SiteUser author;
}
