package com.vrbo.listings.domain

case class Listing(id: Option[String], contact: Contact, address: Address, location: Location) {

  def withId(id: String): Listing = {
    Listing(Some(id), contact, address, location)
  }
}