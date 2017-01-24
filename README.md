# HEIG-AMT-Gamification
Gamification API platform for AMT course at HEIG-VD.
The application has been made with [Spring boot](https://projects.spring.io/spring-boot/)

The team is composed by [Bastien Clément](https://github.com/galedric), [Julien Leroy](https://github.com/limayankee), [Matthieu Villard](https://github.com/matthieuVillard) and [Loïc Serafin](https://github.com/pikkle).

## Requirements
- Docker **1.13.0**
- Docker-compose **1.10.0**

## Instructions
- Clone the repo
- Run the following command: `docker-compose up`
- The app is now running on [http://localhost:8080](http://localhost:8080)

## JavaScript APIs

Rules and Triggers are defined using JavaScript code. This section describes variables and functions exposed to user code.

In both Rule and Trigger API, the user code can use any standard object defined by ECMAScript standard like `String`, `Math`, `Array`, `Date`, etc. In addition, the `trace(text: string)` function can be used to add a line in the `trace` array returned by the `/events` endpoint.

### Rule API

Rules have access to the `payload` variable containing the same value that the `payload` field of the given event object.

Additonnaly, the following functions are available:
 * `reset(criterion: string, value: number)`: Resets a criterion to the given value
 * `increment(criterion: string, delta: number)`: Increments the criterion by the given amount of units
 * `decrement(criterion: string, delta: number)`: Same as `increment`, but using negative `delta`
 * `award(badge: string, count: number)`: Bypass the trigger mechanism and awards `count` instances of the badge to the user.
