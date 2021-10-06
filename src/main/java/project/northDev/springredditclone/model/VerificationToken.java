package project.northDev.springredditclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant expireTime;
}
