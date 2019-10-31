package com.vrbo.listings.acceptance

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.vrbo.listings.ListingsApiServer
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import io.circe.{Encoder, Json}
import org.scalatest.{GivenWhenThen, Matchers}

abstract class TestServerHelper extends FeatureTest with Matchers with GivenWhenThen {

  protected def createOnly(): EmbeddedHttpServer = {
    new EmbeddedHttpServer(new ListingsApiServer,
      disableTestLogging = true,
      flags = Map("log.level" -> "OFF")
    )
  }

  implicit val encodeListing: Encoder[Listing] = (l: Listing) =>
    Json.obj(("listing", Json.obj(
      ("id", Json.fromString(l.id.getOrElse(""))),
      ("address", encodeAddress(l.address)),
      ("contact", encodeContact(l.contact)),
      ("address", encodeAddress(l.address)),
      ("location", encodeLocation(l.location))
    )))

  implicit lazy val encodeContact: Encoder[Contact] = (c: Contact) => Json.obj(
    ("phone", Json.fromString(c.phone)),
    ("formattedPhone", Json.fromString(c.formattedPhone))
  )

  implicit lazy val encodeAddress: Encoder[Address] = (a: Address) => Json.obj(
    ("address", Json.fromString(a.address)),
    ("postalCode", Json.fromString(a.postalCode.getOrElse(""))),
    ("countryCode", Json.fromString(a.countryCode)),
    ("city", Json.fromString(a.city)),
    ("state", Json.fromString(a.state.getOrElse(""))),
    ("country", Json.fromString(a.country))
  )

  implicit lazy val encodeLocation: Encoder[Location] = (l: Location) => Json.obj(
    ("lat", Json.fromDoubleOrString(l.lat)),
    ("lng", Json.fromDoubleOrString(l.lng))
  )


}
