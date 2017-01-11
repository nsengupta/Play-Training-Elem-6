package services.attendees.starPlayers;


import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import play.db.Database;
import static services.attendees.starPlayers.SoccerInfoMessageProtocol.*;

public class SoccerAttendeesInfoActor extends UntypedActor {
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	private Database databaseHandle;

	@Override
	public void onReceive(Object arg0) throws Throwable {
		
		if (arg0 instanceof CountMessage) {
			
			Connection dbConnection = this.databaseHandle.getConnection();
			ResultSet rs = dbConnection.prepareStatement("SELECT COUNT(*) FROM StarPlayers").executeQuery();
			int count = (rs.next() ? rs.getInt(1) : 0);
			dbConnection.close();
			getSender().tell(count, getSelf());
		}
		else
	    if (arg0 instanceof GetAllMessage) {
	    	List<String> allPlayers = new ArrayList<String>();
	    	getSender().tell(allPlayers,getSelf());
	    }
		
	}
	
	public static Props props(final Database database) {
	    return Props.create(new Creator<SoccerAttendeesInfoActor>() {
	      private static final long serialVersionUID = 1L;
	 
	      public SoccerAttendeesInfoActor create() throws Exception {
	        return new SoccerAttendeesInfoActor(database);
	      }
	    });
	} 


	public SoccerAttendeesInfoActor(final Database database) {
		this.databaseHandle = database;
	}

}