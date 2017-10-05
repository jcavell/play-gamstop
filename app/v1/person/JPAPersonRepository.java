package v1.person;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPAPersonRepository implements PersonRepository {

    private final JPAApi jpaApi;
    private final PersonExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAPersonRepository(JPAApi api, PersonExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<PersonData>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<PersonData> create(PersonData personData) {
        return supplyAsync(() -> wrap(em -> insert(em, personData)), ec);
    }

    @Override
    public CompletionStage<Optional<PersonData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<PersonData>> update(Long id, PersonData personData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, personData))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<PersonData> lookup(EntityManager em, Long id) throws SQLException {
        throw new SQLException("Call this to cause the circuit breaker to trip");
        //return Optional.ofNullable(em.find(PersonData.class, id));
    }

    private Stream<PersonData> select(EntityManager em) {
        TypedQuery<PersonData> query = em.createQuery("SELECT p FROM PersonData p", PersonData.class);
        return query.getResultList().stream();
    }

    private Optional<PersonData> modify(EntityManager em, Long id, PersonData personData) throws InterruptedException {
        final PersonData data = em.find(PersonData.class, id);
        if (data != null) {
            data.firstName = personData.firstName;
            data.lastName = personData.lastName;
            data.dateOfBirth = personData.dateOfBirth;
            data.email= personData.email;
            data.postcode= personData.postcode;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private PersonData insert(EntityManager em, PersonData postData) {
        return em.merge(postData);
    }
}
