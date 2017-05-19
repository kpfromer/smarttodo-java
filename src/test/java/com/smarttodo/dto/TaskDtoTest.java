package com.smarttodo.dto;

import com.smarttodo.model.Event;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

import java.time.DayOfWeek;

/**
 * Created by kpfromer on 5/18/17.
 */

public class TaskDtoTest {

    private TaskDto dto;

    @Before
    public void setUp() throws Exception {
        dto = new TaskDto();
    }

    @Test
    public void getTextAndEvent_ShouldUseEventTextInsteadOfDescriptionForEvent() throws Exception {
        dto.setDescription("hello monday");
        dto.setEventText("tue");

        EditedTextAndEvent editedTextAndEvent = dto.getTextAndEvent();

        assertThat(editedTextAndEvent.getEditedText(), equalTo("hello monday"));
        assertThat(editedTextAndEvent.getEvent().getCurrentSetDate().getDayOfWeek(), equalTo(DayOfWeek.TUESDAY));
    }

    @Test
    public void getTextAndEvent_ShouldUseDescriptionForEvent() throws Exception {
        dto.setDescription("hello monday");
        dto.setEventText("");

        EditedTextAndEvent editedTextAndEvent = dto.getTextAndEvent();

        assertThat(editedTextAndEvent.getEditedText(), equalTo("hello"));
        assertThat(editedTextAndEvent.getEvent().getCurrentSetDate().getDayOfWeek(), equalTo(DayOfWeek.MONDAY));
    }

    @Test
    public void getTextAndEvent_ShouldNotHaveAnEventIfDescriptionIsJustDate() throws Exception {
        dto.setDescription("monday");
        dto.setEventText("");

        EditedTextAndEvent editedTextAndEvent = dto.getTextAndEvent();

        assertThat(editedTextAndEvent.getEditedText(), equalTo("monday"));
        assertThat(editedTextAndEvent.getEvent(), instanceOf(Event.class));
    }
}