package com.vrbo.listings.controller

import com.twitter.finatra.http.Controller
import com.twitter.util.Future
import com.vrbo.listings.ListingResponse
import com.vrbo.listings.domain.Listing
import com.vrbo.listings.domain.http.{ListingGetRequest, ListingPostRequest}
import com.vrbo.listings.service.ListingsService
import javax.inject.Inject

class ListingsController @Inject()(listingsService: ListingsService[Listing]) extends Controller {

  get("/listings/:id") { listingsGetRequest: ListingGetRequest =>
    listingsService.get(listingsGetRequest.id)
  }

  post("/listings") { listingPostRequest: ListingPostRequest =>

    //    listingsService.save(listingPostRequest).onFailure {

    val futureListingResponse: Future[ListingResponse] = for {
      listing <- listingsService.save(listingPostRequest)
      listingResponse = ListingResponse.fromDomain(listing)
    } yield listingResponse

    futureListingResponse
      .onSuccess(lr => response.ok(lr))
      .onFailure(e => response.conflict(e.getMessage))

  }

  //  delete("/listings/:id") { listingsRequest: ListingsRequest =>
  //    "hello"
  //  }
  //
  //  put("/listings/:id") { listingsRequest: ListingsRequest =>
  //    "hello"
  //  }
}