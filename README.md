# HEIG-AMT-Gamification
Gamification API platform for AMT course at HEIG-VD.
The application has been made with [Spring boot](https://projects.spring.io/spring-boot/)

The team is composed by [Bastien Clément](https://github.com/galedric), [Julien Leroy](https://github.com/limayankee), [Matthieu Villard](https://github.com/matthieuVillard) and [Loïc Serafin](https://github.com/pikkle).

## Requirements
- Docker **1.13.0**
- Docker-compose **1.10.0**

## Instructions
- Clone the repo and cd into it
- Build the docker image: `docker build -t gamecat .`
- Run the docker container: `docker run -p 8080:8080 gamecat` 
 (adapt the port binding if needed, the internal port is *8080*)
- The app is now running on [http://localhost:8080](http://localhost:8080)

## Example of gamified application
We gamified the Poll-Cat application (made within the TWEB course). You can find the code on the gamification branch of the repository, [here](https://github.com/pikkle/poll-cat/tree/gamification).

## JavaScript APIs

Rules and Triggers are defined using JavaScript code. This section describes variables and functions exposed to user code.

In both Rules and Triggers API, the user code can use any standard object defined by ECMAScript standard like `String`, `Math`, `Array`, `Date`, etc. 

In addition, the following functions are available:

 * `trace(text: string)`: Appends an entry to the `trace` array returned by the `/events` endpoint.
 * `award(badge: string, count: number)`: Bypass the trigger mechanism and awards `count` instances of the badge to the user.

### Rules API

Rules have access to the `payload` variable containing the same value that the `payload` field of the given event object.

Additionally, the following functions are available:

 * `reset(criterion: string, value: number)`: Resets a criterion to the given value
 * `increment(criterion: string, delta: number)`: Increments the criterion by the given amount of units
 * `decrement(criterion: string, delta: number)`: Same as `increment`, but using negative `delta`

 
 ### Triggers API
 
Triggers can use the `criteria` variable to access criteria values and decide which actions to perform.

For each criterion registered with the trigger, an object describing changes made to the criterion will be avaible in the `criteria` object. Each entry will contain the following fields:

 * `old`: the old value of the criterion, before rules executions.
 * `value`: the current value of the criterion
 * `delta`: `value - old`

For example, `criteria["foo"].value` is the current value of the "foo" criterion.

The Triggers API does not define any additionnal functions. The shared `award` function can be used to award badges to users.
