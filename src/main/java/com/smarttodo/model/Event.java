package com.smarttodo.model;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * Created by kpfromer on 4/19/17.
 */


@Embeddable
public class Event implements Comparable {

    @Type(type = "java.time.LocalDate")
    private LocalDate currentSetDate;

    @Type(type = "java.time.LocalDate")
    private LocalDate startDate;

    @Type(type = "java.time.LocalDate")
    private LocalDate endDate;

    @NotNull
    @Column
    private boolean recurring;

    @NotNull
    @Column
    private boolean completed;


    public Event() {
        completed = false;
        recurring = false;
    }

    public LocalDate getCurrentSetDate() {
        return currentSetDate;
    }

    public void setCurrentSetDate(LocalDate currentSetDate) {
        this.currentSetDate = currentSetDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getFormattedEvent(){

        //todo: add test
        if (getCurrentSetDate() == null) {
            return "";
        }

        if (!isCompleted()) {
            if (isRecurring()){
                if (getEndDate() != null){//todo: better message (include current set date)
                    return String.format("every %s until %s", getCurrentSetDate().getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase(), getEndDate().format(ISO_LOCAL_DATE));
                } else {
                    return String.format("every %s", getCurrentSetDate().getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())).toLowerCase();
                }
            } else {//todo: better date format
                return String.format("%s", getCurrentSetDate().format(ISO_LOCAL_DATE));
            }
        } else {
            return "";
        }
    }

    @Override
    public int compareTo(Object o) {
        LocalDate otherDate = (LocalDate) o;
        return this.currentSetDate.compareTo(otherDate);
    }


    public static final class EventBuilder {
        private LocalDate currentSetDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean recurring;
        private boolean completed;

        public EventBuilder() {
        }

        public static EventBuilder anEvent() {
            return new EventBuilder();
        }

        public EventBuilder withCurrentSetDate(LocalDate currentSetDate) {
            this.currentSetDate = currentSetDate;
            return this;
        }

        public EventBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public EventBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public EventBuilder withRecurring(boolean recurring) {
            this.recurring = recurring;
            return this;
        }

        public EventBuilder withCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setCurrentSetDate(currentSetDate);
            event.setStartDate(startDate);
            event.setEndDate(endDate);
            event.setRecurring(recurring);
            event.completed = this.completed;
            return event;
        }
    }


    //todo: allow for multiple days such as everyday
    private LocalDate getNextOccurrence(LocalDate date){

        date = date.with(TemporalAdjusters.next(date.getDayOfWeek()));

        return date;
    }

    protected void complete(){

        if (isRecurring()) {

            LocalDate nextOccurrence = getNextOccurrence(getCurrentSetDate());
            if (getEndDate() != null && nextOccurrence.isAfter(getEndDate())) {
                setCompleted(true);
            } else {
                currentSetDate = nextOccurrence;
            }
        } else {
            setCompleted(true);
        }
    }
}
