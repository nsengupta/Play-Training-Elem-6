package org.sapient.modelUnitTests;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActor;
import akka.testkit.TestActor.Message;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import services.attendees.starPlayers.AttendeesDB;
import services.attendees.starPlayers.SoccerAttendeesInfoActor;
import services.attendees.starPlayers.SoccerInfoMessageProtocol;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.*;
import static play.test.Helpers.*;

import com.google.inject.Inject;



public class SoccerAttendeesInfoActorTest {
	
	private static ActorSystem system;
	
	
	private static Database databaseHandle;
	
	@BeforeClass
	  public static void setup() {
	    system = ActorSystem.create("myAttendeesActorTestSystem");
		databaseHandle = Databases.inMemory("test");
	  }
	  
	  @AfterClass
	  public static void teardown() {
	    JavaTestKit.shutdownActorSystem(system);
	    databaseHandle.shutdown();
	  }
	
	/*@Test
	public void countTest() {
		System.out.println("CountTest running...");
		
		    final JavaTestKit testDriverEnv = new JavaTestKit(system);
			//final Props props = Props.create(SoccerAttendeesInfoActor.class,databaseHandle);
			final Props props = SoccerAttendeesInfoActor.props(databaseHandle);
			
			final TestActorRef<SoccerAttendeesInfoActor> attendeesRef = 
					TestActorRef.create(system, props, "countTest");
			final SoccerAttendeesInfoActor actor = attendeesRef.underlyingActor();
			
			attendeesRef.tell(new SoccerInfoMessageProtocol.CountMessage(), testDriverEnv.getRef());		
			testDriverEnv.expectMsgEquals(new FiniteDuration(1, TimeUnit.SECONDS),2);
	
	}*/
	
	@Test
	public void testInServer() throws Exception {
	    int testServerPort = play.api.test.Helpers.testServerPort();
	    TestServer server = testServer(testServerPort);
	    running(server, () -> {
	        try {
	            try (WSClient ws = WS.newClient(testServerPort)) {
	                CompletionStage<WSResponse> completionStage = ws.url("/count").get();
	                WSResponse response = completionStage.toCompletableFuture().get();
	                assertEquals(OK, response.getStatus());
	                System.out.println(response.getBody());
	            }
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    });
	}
}
