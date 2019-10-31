package com.vrbo.listings.controller

import com.twitter.finatra.http.Controller
import com.vrbo.listings.domain.Listing
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.http.request.ListingRequest.{ListingDeleteRequest, ListingGetRequest, ListingPostRequest, ListingPutRequest}
import com.vrbo.listings.http.response.ListingNotFound
import com.vrbo.listings.service.ListingsService
import javax.inject.Inject

class ListingsController @Inject()(listingsService: ListingsService[Listing, UUID]) extends Controller {

  get("/listings/:id") { listingsGetRequest: ListingGetRequest =>
    listingsService.get(UUID(listingsGetRequest.id)) map {
      case Some(value) => response.ok(value)
      case None => response.notFound(ListingNotFound.get)
    }
  }

  post("/listings") { listingPostRequest: ListingPostRequest =>
    listingsService.save(listingPostRequest.toDomain).map(listing => response.created(listing))
  }

  delete("/listings/:id") { listingDeleteRequest: ListingDeleteRequest =>
    listingsService.delete(UUID(listingDeleteRequest.id)) map (_ => response.noContent)
  }

  put("/listings/:id") { listingPutRequest: ListingPutRequest =>
    listingsService.update(UUID(listingPutRequest.id), listingPutRequest.toDomain).map(_ => response.noContent)
  }
}