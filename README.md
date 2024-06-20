# Final Assignment for the Software Engineering II Course - Subscriptions System

## Scope

The assignment consists in simple web system developed with Java Spring and structured as microsservices that can be used to manage subscriptions of subscription-based applications, such as Netflix, Spotify, Disney+ etc. More information on the three microsservices can be found in the `docs/enunciado.pdf` file, but, basically, they are:
- **servico-cadastramento**: The main module of the system. Maintains a database of the applications registered in the system, as well as the users and subscriptions.
- **servico-assinaturas-validas**: Maintains a cache of the subscriptions in order to make the system more responsible. When a cache hit occurs, it immediately returns the entry for that microsservice; if a cache miss occurs, it communicates with the *servico-cadastramento* microsservice in order to find out whether or not the requests subscription is active. When this receives a subscription payment event, it removes the entry corresponding to the subscription that was payed from its cache, in order to maintain cache consistency.
- **servico-pagamentos**: This is notified through its single endpoint when a subscription payment occurs. It then stores the information on that payment in its database and generates an event to the other microsservices notifying them of the payment of a subscription.

All communication is made through a RabbitMQ instance hosted on CloudAMQP.

## Folder structure

- `docs`: All documentation on the system, including the final report of the assignment, diagrams (database ER, components diagram, deployment diagram etc), instructions on how to deploy the system in AWS, the tasks developed for the project, the full definition of the assignment, a Postman collection for the system etc.
- `src`: The source code for the microsservices of the system. It contains three subfolders, one for each microsservice. Each of these subfolders follow the default Maven folder structure and contain a Dockerfile to build their containers.

## Framework used

The microsservices have been developed with the Spring framework and all dependencies are managed by Maven.

## Running the system

The system can be run locally by building and executing the Docker containers for each of the three microsservices locally, or you can follow the tutorial on how to deploy the system to AWS available at `docs/deployment-instruction.md`.