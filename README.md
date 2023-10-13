# Rick And Morty requirements

### Business requirements

- Task: Create API. It should contain two methods:

1. The request randomly generates a wiki about one character in the universe the animated series Rick & Morty.
   Response example:

    ```json
    {
      "id": 1,
      "externalId": "1",
      "name": "Rick Sanchez",
      "status": "Alive",
      "gender": "Male"
    }
    ```
    
    NOTE: `externalId` field should save the original character ID received from the external API. `id` field should
    represent the identifier of entire `Character` entity, that is associated with internal DB.

2. The request takes a string as an argument, and returns a list of all characters whose name contains the search
   string.
   During the application start, the web application downloads data from a third-party service to the internal database.
   Implemented API requests must work with a local database (i.e. fetch data from a database).

- What to use:

1. You must use [public API](https://rickandmortyapi.com/documentation/#rest) (you should use REST API).
2. All data from the public API should be fetched once, and only once, when the Application context is created

### Tech Requirements

- Use MySQL DB in your app.
- Use H2 DB in your test configuration (It is already configured in the `src/test/resources/application.properties`
  file).
- Keep identical set of params in the `src/main/resources/application.properties`
  and `src/test/resources/application.properties` files. In other case you may face a problem with Application Context
  creation during the `mvn test` phase.
- Requests must be documented using Swagger.
