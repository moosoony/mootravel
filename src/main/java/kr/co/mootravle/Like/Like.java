package kr.co.mootravle.Like;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.User.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "travel_Like")
@Getter
@Setter
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Travel travel;

    @ManyToOne

    private SiteUser author;
}
