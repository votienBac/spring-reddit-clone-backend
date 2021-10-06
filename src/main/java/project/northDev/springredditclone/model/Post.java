package project.northDev.springredditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;
    @NotBlank(message = "Post name cannot be empty or null")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob //  map Binary OR large character objects
    private String description;
    private Integer voteCount;
    @ManyToOne(fetch = FetchType.LAZY) // when find post, dont get related post object
    @JoinColumn(name = "userId", referencedColumnName = "userId") // "referencedColumnName" property is the name of the column in the table that you are making reference with the column you are anotating
    private User user;
    private Instant createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Subreddit subreddit;

}
