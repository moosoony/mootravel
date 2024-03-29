package kr.co.mootravel.Like;

import kr.co.mootravel.Travel.Travel;
import kr.co.mootravel.User.SiteUser;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicInsert
@Table(name = "travel_Like")
@Getter
@Setter
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Travel travel;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime createDate;
}
