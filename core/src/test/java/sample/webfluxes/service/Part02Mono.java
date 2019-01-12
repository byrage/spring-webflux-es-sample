/*
package sample.service;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Part02Mono {

    void expectFooBarComplete(Flux<String> flux) {
        StepVerifier.create(flux).expectNext("foo", "bar").verifyComplete();
        StepVerifier.create(flux).expectError(RuntimeException.class).verify();
        StepVerifier.create(flux).expectError(RuntimeException.class).verify();
    }

    // TODO Use StepVerifier to check that the flux parameter emits a User with "swhite"username
    // and another one with "jpinkman" then completes successfully.


    // TODO Expect 10 elements then complete and notice how long the test takes.
    void expect10Elements(Flux<Long> flux) {

        StepVerifier.create(flux)
                    .expectNextCount(10)
                    .verifyComplete()
                    .getSeconds();
    }

//========================================================================================

    // TODO Return an empty Mono
    Mono<String> emptyMono() {

        return Mono.empty();
    }

//========================================================================================

    // TODO Return a Mono that never emits any signal
    Mono<String> monoWithNoSignal() {

        return Mono.never();
    }

//========================================================================================

    // TODO Return a Mono that contains a "foo" value
    Mono<String> fooMono() {

        return Mono.just("foo");
    }

//========================================================================================

    // TODO Create a Mono that emits an IllegalStateException
    Mono<String> errorMono() {

        return Mono.error(new IllegalStateException());
    }

    // TODO Capitalize the user username, firstname and lastname
    Mono<User> capitalizeOne(Mono<User> mono) {
        return mono.map(u-> {
            u.setFirstname(u.getFirstname().toUpperCase()));
            u.setLastname(u.getLastname().toUpperCase()));
            return u;
        });
    }

    // TODO Capitalize the users username, firstName and lastName using #asyncCapitalizeUser
    Flux<User> asyncCapitalizeMany(Flux<User> flux) {
        return flux.flatMap(user -> asyncCapitalizeUser(user));
    }

    Mono<User> asyncCapitalizeUser(User u) {
        return Mono.just(new User(u.getUsername().toUpperCase(), u.getFirstname().toUpperCase(), u.getLastname().toUpperCase()));
    }

    Flux<User> mergeFluxWithInterleave(Flux<User> flux1, Flux<User> flux2) {
        return Flux.merge(flux1,flux2);
    }

    Flux<User> createFluxFromMultipleMono(Mono<User> mono1, Mono<User> mono2) {
        StepVerifier.create(mono1).expectNextCount(4).verifyComplete();
        StepVerifier.create(flux).thenRequest(1).expectNextCount(4).expectComplete();
        mono1.mergeWith(mono2);
        return null;
    }

    // TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
    Flux<User> fluxWithLog() {
        return Flux.fromIterable(repository.findAll()).log()
    }

    @AllArgsConstructor
    public static class User {
        String username;
        String firstname;
        String lastname;

        public String getUsername() {

            return username;
        }

        public void setUsername(String username) {

            this.username = username;
        }

        public String getFirstname() {

            return firstname;
        }

        public void setFirstname(String firstname) {

            this.firstname = firstname;
        }

        public String getLastname() {

            return lastname;
        }

        public void setLastname(String lastname) {

            this.lastname = lastname;
        }
    }
}*/
