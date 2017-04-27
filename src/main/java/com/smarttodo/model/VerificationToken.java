package com.smarttodo.model;

import com.smarttodo.core.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by kpfromer on 4/25/17.
 */

@Entity
public class VerificationToken extends BaseEntity {

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    //todo: property source
    @Transient
    private int timeFrame = 1440;//24 hours

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    public void newExpiryDate(){
        this.expiryDate = calculateExpiryDate(this.timeFrame);
    }

    public VerificationToken() {
        super();
    }

    public VerificationToken(Long id) {
        super(id);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }


    public static final class VerificationTokenBuilder {
        private String token;
        private Long id;
        private User user;
        private LocalDateTime expiryDate;

        //todo: property source
        private int timeFrame = 1440;//24 hours

        public VerificationTokenBuilder() {
        }

        public static VerificationTokenBuilder aVerificationToken() {
            return new VerificationTokenBuilder();
        }

        public VerificationTokenBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public VerificationTokenBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public VerificationTokenBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public VerificationTokenBuilder withExpiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public VerificationTokenBuilder withTimeFrame(int timeFrame) {
            this.timeFrame = timeFrame;
            return this;
        }

        public VerificationToken build() {
            VerificationToken verificationToken = new VerificationToken(id);
            verificationToken.setToken(token);
            verificationToken.setUser(user);
            verificationToken.setExpiryDate(expiryDate);
            verificationToken.timeFrame = this.timeFrame;
            return verificationToken;
        }
    }
}
