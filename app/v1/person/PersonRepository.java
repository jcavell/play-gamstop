package v1.person;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface PersonRepository {

    CompletionStage<Stream<PersonData>> list();

    CompletionStage<PersonData> create(PersonData personData);

    CompletionStage<Optional<PersonData>> get(Long id);

    CompletionStage<Optional<PersonData>> update(Long id, PersonData personData);
}

