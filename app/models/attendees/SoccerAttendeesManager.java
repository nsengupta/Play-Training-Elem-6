package models.attendees;

import java.util.ArrayList;
import java.util.List;

import services.attendees.starPlayers.AttendeesDB;

import com.google.inject.Inject;

import models.attendees.AttendeesManager;

public class SoccerAttendeesManager  implements AttendeesManager {
	
	private AttendeesDB attendeesDB;

	@Inject
	public SoccerAttendeesManager(AttendeesDB attendeesDB) {
		this.attendeesDB = attendeesDB;
	}

	@Override
	public List<String> getAll() { return (this.attendeesDB.getAll()); }

	@Override
	public String getBySurname(String surname) { return (this.attendeesDB.getBySurname(surname)); }
		
	@Override
	public Integer attendeeCount() { return (this.attendeesDB.attendeeCount()); }

	@Override
	public Integer addNewAttendee(String surname, String firstname) {
		// TODO Auto-generated method stub
		return null;
	}

}
