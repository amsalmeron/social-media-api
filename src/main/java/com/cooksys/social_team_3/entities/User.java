package com.cooksys.social_team_3.entities;

import com.cooksys.social_team_3.services.Deletables;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User implements Deletables {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded 
    private Credentials credentials;
    
    @Embedded
    private Profile profile;
    
    @Column(nullable= false, updatable = false)
    @CreationTimestamp
    private Timestamp joined;
    
    @Column(nullable = false)
    private boolean deleted = false;
    
    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;
    
    @ManyToMany
    @JoinTable(
    		name = "user_likes",
    		joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private List<Tweet> likedTweets;
    
    @ManyToMany(mappedBy = "followers")
    private List<User> following;
    
    @ManyToMany
    @JoinTable(name = "followers_following")
    private List<User> followers;
    
    @ManyToMany(mappedBy = "usersMentioned")
    private List<Tweet> mentions;

    public void removeFollowing(User following) {
        this.following.remove(following);
        following.getFollowers().remove(this);
    }

    public void addFollowing(User following) {
        this.following.add(following);
        following.getFollowers().add(this);
    }

}