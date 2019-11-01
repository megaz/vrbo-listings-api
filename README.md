# Introduction
This is a JSON-based RESTful API for Vacation Rentals properties.  This API attempts to solve the problem statement
described below.

## Problem Statement

Your task is to build a JSON-based RESTful API for a Vacation Rentals properties.
This is an example of what we expect the JSON vacation rental property document to look
like:

```
{
  "listing": {
    "id": "5e22a83a-6f4f-11e6-8b77-86f30ca893d3",
    "contact": {
      "phone": "15126841100",
      "formattedPhone": "+1 512-684-1100"
    },
    "address": {
      "address": "1011 W 5th St",
      "postalCode": "1011",
      "countryCode": "US",
      "city": "Austin",
      "state": "TX",
      "country": "United States"
    },
    "location": {
      "lat": 40.4255485534668,
      "lng": -3.7075681686401367
    }
  }
}
```

Part 1.

The first part of this task is to allow the client to access a listing. It
is up to you to determine the resources and domain objects, but we would
expect that a call such as `GET /listings/5e22a83a-6f4f-11e6-8b77-86f30ca893d3` would return a response relating
to the listing with the id of `5e22a83a-6f4f-11e6-8b77-86f30ca893d3`.

Part 2.

The next part of this task is to provide a mechanism to add new listings to
the dataset. This should be provided as an HTTP POST with a parameter body
containing all required data for the new listing. The response should
contain appropriate headers and content for this method. The server should
assign an id to the new listing.

Considerations

Pay particular attention to concurrency issues. Even though the task itself could be achieved
without, imagine you are building for maximum reuse, extensibility, scalability and performance.
Good maintainability and code cleanliness will also gain you extra points.

## Getting Started

Below are instructions on how to run the Listings API

#### Running the API via Docker

        sbt docker:publishLocal
        docker run -p 8000:8000 vrbo-listings-api:1.0
        
#### Running the API locally via SBT

       ./sbt run
       
##  Decisions

  -  The Listings REST API is built on top of Finatra (Finagle) to benefit from the non blocking and async features.
  -  **PUT** and **DELETE** responses are idempotent - https://tools.ietf.org/html/rfc7231#section-4.2.2.
  -  Implementing extra functionality such as return listings by city that simulates a real life use case.
  -  Service and Store traits defined as returning Futures.  Since fetching from a local in memory cache is very little CPU, 
     returned a `Future.value`. For reference: (http://enear.github.io/2019/01/17/future-workloads/)
  -  Implemented some API validation, due to time limitations I was unable to finish this.
  -  Validation includes creating a listing when there is a listing that contains an already existing address.   

##  API Documentation

For API Documentation, please see [API_DOCUMENTATION.md](API_DOCUMENTATION.md)