package com.vrbo.listings.domain.http

import com.vrbo.listings.domain.Listing

case class ListingPostRequest(listing: Listing) {

  def toDomain(id: String): Listing = {
    Listing(id = id, listing.contact, listing.address, listing.location)
  }

}