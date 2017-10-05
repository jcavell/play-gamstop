# play-gamstop

A REST API showing Play with a JPA backend.

To run, make sure you have Java 8 and SBT installed.

Run the app with `sbt run`

There are two endpoints, one that works using an in memory Map that you can browse straight away at http://localhost:9000/

The other, under http://localhost:9000/v1/people comes from an in-memory H2 database, accessed via JPA / Hibernate.

To add a new person, simply post to this endpoint with Header Content Type set to application/json and the body of e.g.:
```
 {
   "firstName" : "Jonny",
   "lastName" : "Cavell",
   "dateOfBirth" : "adfasfaf",
   "postcode" : "adfa",
   "email" : "adfasffa"
 }
```
You should receive a 201 reponse status indicating the resource was successfully created.
