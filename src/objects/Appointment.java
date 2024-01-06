package objects;

import java.time.LocalDateTime;

/**
 * class for representing appointment data structure
 */
public class Appointment {

    private int appointmentId;

    private String title;

    private String description;

    private String location;

    private String type;

    private LocalDateTime start;

    private LocalDateTime end;

    private int customerId;

    private int userId;

    private int contactId;

    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;

    }

    public int getAppointmentId() {return appointmentId; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getContactId() {
        return contactId;
    }



}
