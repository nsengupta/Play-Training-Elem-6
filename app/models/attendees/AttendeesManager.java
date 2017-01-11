package models.attendees;

import java.util.List;

import com.google.inject.ImplementedBy;


public interface AttendeesManager {
	
	public List<String> getAll();
	public String       getBySurname(String surname);
	public Integer      attendeeCount();
	public Integer      addNewAttendee(String surname,String firstname);

}
