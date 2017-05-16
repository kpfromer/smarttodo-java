package com.smarttodo.dto;

import com.smarttodo.model.Event;

/**
 * Created by kpfromer on 5/7/17.
 */
public class EditedTextAndEvent {
    private final String editedText;
    private final Event event;

    public EditedTextAndEvent(String editedText, Event event) {
        this.editedText = editedText;
        this.event = event;
    }

    public String getEditedText() {
        return editedText;
    }

    public Event getEvent() {
        return event;
    }
}
