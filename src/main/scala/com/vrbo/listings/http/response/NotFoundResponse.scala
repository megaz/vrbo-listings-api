package com.vrbo.listings.http.response

abstract class NotFoundResponse {
  def `type`: String
  def `message`: String
}

object ListingNotFound {
  case class ListingNotFound(`type`: String, message: String) extends NotFoundResponse
  def get = ListingNotFound("listing.not_found", "No listing was found for the requested.")
}
