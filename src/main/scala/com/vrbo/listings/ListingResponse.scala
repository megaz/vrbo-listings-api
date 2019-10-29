package com.vrbo.listings

import com.vrbo.listings.domain.{Address, Contact, Listing, Location}

object ListingResponse {
  def fromDomain(listing: Listing) = {
    ListingResponse(id = listing.id, listing.contact, listing.address, listing.location)
  }
}

case class ListingResponse(id: String, contact: Contact, address: Address, location: Location) {

  def toDomain = {
    Listing(id, contact, address, location)
  }
}