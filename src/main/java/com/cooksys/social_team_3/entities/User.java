package com.cooksys.social_team_3.entities;

import com.cooksys.social_team_3.services.Deletables;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

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

}