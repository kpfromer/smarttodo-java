package com.smarttodo.model;

import com.smarttodo.core.BaseEntity;
import com.smarttodo.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kpfromer on 4/25/17.
 */

@Entity
public class VerificationToken extends BaseEntity {

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    //todo: property source
    @Transient
    private int timeFrame = 1440;//24 hours

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void newExpiryDate(){
        this.expiryDate = calculateExpiryDate(timeFrame);
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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
