package com.vrbo.listings.http.request

import com.twitter.finatra.request.RouteParam
import com.vrbo.listings.domain.Listing

object ListingRequest {

  case class ListingPutRequest(@RouteParam id: String, listing: Listing) {
    def toDomain: Listing = {
      Listing(Some(id), listing.contact, listing.address, listing.location)
    }
  }

  case class ListingDeleteRequest(@RouteParam id: String)

  case class ListingGetRequest(@RouteParam id: String)

  case class ListingPostRequest(listing: Listing) {
    def toDomain: Listing = {
      Listing(None, listing.contact, listing.address, listing.location)
    }
  }
}