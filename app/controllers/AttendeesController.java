package controllers;

import static akka.pattern.Patterns.ask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import models.attendees.AttendeesManager;
import models.attendees.StarPlayers.SoccerAttendeeDataCarrier;
import play.data.Form;
import play.data.FormFactory;
import play.db.Database;
import play.libs.Akka;
import play.libs.Json;
import play.libs.concurrent.HttpExecution;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import services.attendees.starPlayers.SoccerAttendeesInfoActor;
import services.attendees.starPlayers.SoccerInfoMessageProtocol;
import views.html.index;
import views.html.attendees.list;
import views.html.attendees.count;
import views.html.attendees.countJson;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.api.libs.concurrent.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AttendeesController extends Controller {
	@Inject
    FormFactory formFactory;
	
	private final ActorRef soccerAttendeesInfoActor;
	private final Database  databaseHandle;
	private final AttendeesManager attendeesManager;
	
	@Inject
	private HttpExecutionContext context;
	
	@Inject
    public AttendeesController(ActorSystem actorSystem, Database database, AttendeesManager attendeesManager) throws SQLException {
	   this.databaseHandle = database;	
       this.soccerAttendeesInfoActor = 
    		   actorSystem
    		   .actorOf(
    				   SoccerAttendeesInfoActor.props(this.databaseHandle),
    				   "Soccer-Players-Actor");
       
       this.attendeesManager = attendeesManager;
    }
	
	
    public Result index() {
        return ok(index.render("Attendees Application is being readied."));
    }
    
    public CompletionStage<Result> getAll() {
    	
    	Function<Object,List<String>> fn = (r) -> {
    		List<String> s = (ArrayList<String>)r;
    		return (s);
    	};
    	
    	
        return( FutureConverters.toJava(ask(soccerAttendeesInfoActor,
			    new SoccerInfoMessageProtocol.GetAllMessage(), 
				1000))
                .thenApply(fn)
                .thenApply(nameList -> ok(list.render(nameList))));
    }
    
    
    
    public Result getBySurname(String surname) {
    	
    	return TODO;
    }
    
    
    public CompletionStage<Result> count() {
    	
    	return(
    			
    			CompletableFuture
    			.supplyAsync(() -> this.attendeesManager.attendeeCount())
    			.thenApply(c -> ok(count.render(c)))
    			
    		  );
    }
     
    
    public Result addAttendee(String surname,String firstname) {
    	return TODO;
    }
    
    public Result addSoccerAttendeeThruForm() {
    	Form<SoccerAttendeeDataCarrier> attendeeForm = formFactory.form(SoccerAttendeeDataCarrier.class);
    	attendeeForm.fill(new SoccerAttendeeDataCarrier("LastName here","Firstname here"));
    	return TODO;
    	
    }
    
    public Result saveSoccerAttendeeThruForm() {
    	Form<SoccerAttendeeDataCarrier> attendeeForm = formFactory.form(SoccerAttendeeDataCarrier.class).bindFromRequest();
    	/*this.attendeesManager
    	    .addNewAttendee(
    	    			attendeeForm.apply("surname").value(), 
    	    			attendeeForm.apply("firstname").value()
    	    		);*/
    	ok(String
    			.format("New Soccer Player %s,%s added", 
    					 attendeeForm.apply("firstName").value(),
    					 attendeeForm.apply("lastName").value()
    				   )
    			 );
    	
    	return TODO;
    }

}
