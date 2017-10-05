package v1.person;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class PersonExecutionContext extends CustomExecutionContext {

    @Inject
    public PersonExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "post.repository");
    }
}
