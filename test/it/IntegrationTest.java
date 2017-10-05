package it;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import v1.person.PersonData;
import v1.person.PersonRepository;
import v1.person.PersonResource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.*;

public class IntegrationTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testList() {
        PersonRepository repository = app.injector().instanceOf(PersonRepository.class);
        repository.create(new PersonData("title", "body"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/v1/posts");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString("body"));
    }

    @Test
    public void testTimeoutOnUpdate() {
        PersonRepository repository = app.injector().instanceOf(PersonRepository.class);
        repository.create(new PersonData("title", "body"));

        JsonNode json = Json.toJson(new PersonResource("1", "http://localhost:9000/v1/posts/1", "some title", "somebody"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/v1/posts/1");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(GATEWAY_TIMEOUT));
    }

    @Test
    public void testCircuitBreakerOnShow() {
        PersonRepository repository = app.injector().instanceOf(PersonRepository.class);
        repository.create(new PersonData("title", "body"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/v1/posts/1");

        Result result = route(app, request);
        assertThat(result.status(), equalTo(SERVICE_UNAVAILABLE));
    }


}
