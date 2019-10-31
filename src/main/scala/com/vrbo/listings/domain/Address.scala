package com.vrbo.listings.domain

import com.twitter.finatra.validation.{CountryCode, Size}

case class Address(@Size(min = 2, max = 150) address: String, postalCode: Option[String], @CountryCode countryCode: String, @Size(min = 2, max = 100) city: String, state: Option[String], @Size(min = 4, max = 74) country: String)
