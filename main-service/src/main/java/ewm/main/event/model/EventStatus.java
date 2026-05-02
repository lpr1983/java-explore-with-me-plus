package ewm.main.event.model;

import ewm.main.exception.ValidationException;

public enum EventStatus {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static EventStatus parse(String state) {
        try {
            return EventStatus.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid EventStatus: " + state);
        }
    }
}
