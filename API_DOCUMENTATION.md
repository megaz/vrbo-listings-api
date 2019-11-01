## Listings API Documentation

The REST API for Listings is described below.

## Create a new Listing

Creates a new Listing consisting of Contact details, Vacation Rental address and location of vacation rental.

### Request

`POST /listings`

    {
      "listing": {
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


### Response

###### Response Headers

    HTTP/1.1 200 OK
    Date: Fri, 01 Nov 2019 18:09:19 GMT
    Server: Finatra
    Content-Type: application/json; charset=utf-8
    content-encoding: gzip
    content-length: 241
    
###### HTTP 201 Created response JSON for creating a listing
    {
        "id": "ce0d9905-d867-423d-a53b-aff5ab64cb7b",
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

###### HTTP 409 Conflict response JSON for creating a listing for a address assocaited to another listing
  
      {
          "type": "duplicate_address",
          "message": "Cannot create duplicate listing on address that already exists"
      }
    
    
## Delete a Listing

### Request

`DELETE /listings/id`

### Response

###### Response Headers

    HTTP/1.1 204 No Content
    Date: Fri, 01 Nov 2019 18:11:19 GMT
    Server: Finatra

## Updating a Listing

### Request

`PUT /listings/id`

    {
      "listing": {
        "id": "5e22a83a-6f4f-11e6-8b77-86f30ca893d3",
        "contact": {
          "phone": "15126841100",
          "formattedPhone": "+1 512-684-1100"
        },
        "address": {
          "address": "2011 E 7th St",
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

### Response

###### Response Headers

    HTTP/1.1 204 No Content
    Date: Fri, 01 Nov 2019 18:11:19 GMT
    Server: Finatra

## Retrieving a Listing

### Request By ID

`GET /listings/id`


### Response

###### Response Headers

    HTTP/1.1 200 OK
    Date:  Fri, 01 Nov 2019 19:25:13 GMT
    Server:  Finatra
    Content-Type:  application/json; charset=utf-8
    content-encoding:  gzip
    content-length:  300

###### Successful Response Json  
    
    {
      "listing": {
        "id": "5e22a83a-6f4f-11e6-8b77-86f30ca893d3",
        "contact": {
          "phone": "15126841100",
          "formattedPhone": "+1 512-684-1100"
        },
        "address": {
          "address": "1011 dW 5th St",
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

###### Retrieving a Listing that does not exist Response Json  
    
    {
        "type": "listing.not_found",
        "message": "No listing was found for the requested."
    }

### Request All Listings

`GET /listings`


### Response

###### Response Headers

    Date:  Fri, 01 Nov 2019 19:35:13 GMT
    Server:  Finatra
    Content-Type:  application/json; charset=utf-8
    content-encoding:  gzip
    content-length:  300

###### Response Json  
    
    [
        {
            "listing": {
                "id": "e39fddfc-9c59-4350-b0c5-914765f1c6d3",
                "contact": {
                    "phone": "15126841100",
                    "formattedPhone": "+1 512-684-1100"
                },
                "address": {
                    "address": "1011 5th St",
                    "postalCode": "1011",
                    "countryCode": "US",
                    "city": "Miami",
                    "state": "FL",
                    "country": "United States"
                },
                "location": {
                    "lat": 40.4255485534668,
                    "lng": -3.7075681686401367
                }
            }
        },
        {
            "listing": {
                "id": "e8053cde-f960-4aed-ba89-b57417a73f49",
                "contact": {
                    "phone": "15126841100",
                    "formattedPhone": "+1 512-684-1100"
                },
                "address": {
                    "address": "1531 6th St",
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
    ]
    
### Request All Listings By City

`GET /listings?city=Austin`


### Response

###### Response Headers

    Date:  Fri, 01 Nov 2019 19:16:33 GMT
    Server:  Finatra
    Content-Type:  application/json; charset=utf-8
    content-encoding:  gzip
    content-length:  320

###### Response Json  
    
    [
        {
            "listing": {
                "id": "e8053cde-f960-4aed-ba89-b57417a73f49",
                "contact": {
                    "phone": "15126841100",
                    "formattedPhone": "+1 512-684-1100"
                },
                "address": {
                    "address": "1531 6th St",
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
    ]
             
               