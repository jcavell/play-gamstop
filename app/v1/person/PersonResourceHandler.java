package v1.person;

import com.palominolabs.http.url.UrlBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Post resources, which map to JSON.
 */
public class PersonResourceHandler {

    private final PersonRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public PersonResourceHandler(PersonRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Stream<PersonResource>> find() {
        return repository.list().thenApplyAsync(postDataStream -> {
            return postDataStream.map(data -> new PersonResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<PersonResource> create(PersonResource resource) {
        final PersonData data = new PersonData(resource.getFirstName(), resource.getLastName(), resource.getEmail(), resource.getPostcode(), resource.getDateOfBirth());
        return repository.create(data).thenApplyAsync(savedData -> {
            return new PersonResource(savedData, link(savedData));
        }, ec.current());
    }

    public CompletionStage<Optional<PersonResource>> lookup(String id) {
        return repository.get(Long.parseLong(id)).thenApplyAsync(optionalData -> {
            return optionalData.map(data -> new PersonResource(data, link(data)));
        }, ec.current());
    }

    public CompletionStage<Optional<PersonResource>> update(String id, PersonResource resource) {
        final PersonData data = new PersonData(resource.getFirstName(), resource.getLastName(), resource.getEmail(), resource.getPostcode(), resource.getDateOfBirth());
        return repository.update(Long.parseLong(id), data).thenApplyAsync(optionalData -> {
            return optionalData.map(op -> new PersonResource(op, link(op)));
        }, ec.current());
    }

    private String link(PersonData data) {
        // Make a point of using request context here, even if it's a bit strange
        final Http.Request request = Http.Context.current().request();
        final String[] hostPort = request.host().split(":");
        String host = hostPort[0];
        int port = (hostPort.length == 2) ? Integer.parseInt(hostPort[1]) : -1;
        final String scheme = request.secure() ? "https" : "http";
        try {
            return UrlBuilder.forHost(scheme, host, port)
                    .pathSegments("v1", "posts", data.id.toString())
                    .toUrlString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
