package com.smarttodo.dto;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.smarttodo.model.Event;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kpfromer on 5/7/17.
 */

public class TaskDto {

    //todo: UI Use chips for duedate in the text field: http://materializecss.com/chips.html

    //todo: add test for non nullables
    //todo: make sure this are not nullable
    @NotNull(message = "Task description can not be null.")
    @NotBlank(message = "Task description can not be nothing.")
    private String description;

    @NotNull(message = "Event text can't be null.")
    private String eventText;

    @NotNull(message = "Task must be completed or not completed.")
    private boolean complete;

    private Long id;

    public TaskDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EditedTextAndEvent getTextAndEvent(){

        EditedTextAndEvent textAndEvent;

        //checks if the eventText field is null or empty
        if(getEventText() != null && !getEventText().isEmpty()){

            //if so, set the TE (textAndEvent) description to the description field
            //also parse the eventText field for a date and set TE to the Event
            textAndEvent = new EditedTextAndEvent(getDescription(), parseStringForEvent(getEventText()).getEvent());

        } else {

            //if not, run parseStringForEvent on the description field
            textAndEvent = parseStringForEvent(getDescription());

        }

        //after all that check if description is null(should never be) or empty
        if (textAndEvent.getEditedText() == null || textAndEvent.getEditedText().isEmpty()) {
            //if so, then don't do anything with the Event and return unaltered description
            return new EditedTextAndEvent(getDescription(), new Event());
        } else {
            //otherwise return TE
            return textAndEvent;
        }

    }

    private EditedTextAndEvent parseStringForEvent(String text) {

        //todo: add ability to stop dates from beginning used for duedate
        //todo: natty doesn't allow for "everyday" and "every weekday" ADD IT!

        EditedTextAndEvent editedTextAndEvent = new EditedTextAndEvent(text, new Event());

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(text);
        for (DateGroup group : groups) {

            List<LocalDate> dates;

            ZoneId timeZone = ZoneId.systemDefault();

            dates = group.getDates().stream().map(date -> date.toInstant().atZone(timeZone).toLocalDate()).collect(Collectors.toList());

            String matchingValue = group.getText();
            String fullText = group.getFullText();

            String editedText = fullText;

            LocalDate duedate;
            LocalDate recursUntil = null;

            //todo: get user time zone (NOT SYSTEM TIME ZONE)

            //todo: allow for choice of date to be used

            duedate = dates.get(0);
            if (group.isRecurring()) {

                if (group.getRecursUntil() != null) {
                    recursUntil = group.getRecursUntil().toInstant().atZone(timeZone).toLocalDate();
                }

                editedText = editedText.replaceFirst("\\s?" + group.getParseLocations().get("recurrence").get(0).getText() + "\\s?", "");

            }


            editedText = editedText.replaceFirst("\\s?" + matchingValue + "\\s?", "");

            Event event = new Event.EventBuilder()
                    .withCurrentSetDate(duedate)
                    .withStartDate((group.isRecurring()  ? duedate : null))
                    .withEndDate(recursUntil)
                    .withRecurring(group.isRecurring())
                    .build();

            editedTextAndEvent = new EditedTextAndEvent(editedText, event);
        }

        return editedTextAndEvent;
    }

}
