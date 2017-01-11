package models.attendees.StarPlayers;

import static akka.pattern.Patterns.ask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import scala.compat.java8.FutureConverters;
import services.attendees.starPlayers.AttendeesDB;
import services.attendees.starPlayers.SoccerAttendeesInfoActor;
import services.attendees.starPlayers.SoccerInfoMessageProtocol;
import views.html.attendees.list;
import play.mvc.*;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import play.api.libs.concurrent.*;

import com.google.inject.Inject;

import models.attendees.AttendeesManager;
import models.attendees.AttendeesManagerWithPromise;

public class SoccerAttendeesManagerWithPromise  implements AttendeesManagerWithPromise {
	
	final ActorRef soccerAttendeesInfoActor;

    @Inject 
    public SoccerAttendeesManagerWithPromise(ActorSystem system) {
    	soccerAttendeesInfoActor = system.actorOf(SoccerAttendeesInfoActor.props);
    }

	@Override
	public CompletionStage<List<String>> getAll() { 
		
		Function<Object,List<String>> fn = (r) -> {
    		List<String> s = (ArrayList<String>)r;
    		return (s);
    	};
    	
        return( FutureConverters.toJava(ask(soccerAttendeesInfoActor,
			    new SoccerInfoMessageProtocol.GetAllMessage(), 
				1000))
                .thenApply(fn)
                );
		
	}

}