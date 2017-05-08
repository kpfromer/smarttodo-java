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

    //todo: make sure this are not nullable
    @NotNull(message = "Task must either be completed or not.")
    private boolean complete;

    private String eventText;

    public TaskDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public EditedTextAndEvent getTextAndEvent(){
        if(getEventText() != null && !getEventText().isEmpty()){
            EditedTextAndEvent textAndEvent = new EditedTextAndEvent(getDescription(), parseStringForEvent(getEventText()).getEvent());
            return textAndEvent;
        } else {
            return parseStringForEvent(getDescription());
        }
    }

    private EditedTextAndEvent parseStringForEvent(String text) {

        //todo: add ability to stop dates from beginning used for duedate
        //todo: natty doesn't allow for "everyday" and "every weekday" ADD IT!

        EditedTextAndEvent editedTextAndEvent = null;

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
