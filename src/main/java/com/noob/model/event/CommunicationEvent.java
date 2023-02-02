package com.noob.model.event;

import com.noob.model.bo.SystemNormalFile;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * <a href="https://stackoverflow.com/questions/27416758/how-to-emit-and-handle-custom-events">...</a>
 */
public class CommunicationEvent extends Event {

    public static final EventType<CommunicationEvent> ALL = new EventType<>(EventType.ROOT, "ALL");
    public static final EventType<CommunicationEvent> SCENE_CLOSE = new EventType<>(ALL, "SCENE_CLOSE");
    public static final EventType<CommunicationEvent> MANAGE_SUCCESS = new EventType<>(ALL, "MANAGE_SUCCESS");
    public static final EventType<CommunicationEvent> RELATE_FINISH = new EventType<>(ALL, "RELATE_FINISH");


    private final SystemNormalFile file;

    public CommunicationEvent(EventType<? extends Event> eventType, SystemNormalFile file) {
        super(eventType);
        this.file = file;
    }

    public SystemNormalFile getFile() {
        return file;
    }
}
