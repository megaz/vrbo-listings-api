package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status.NotFound
import com.twitter.finatra.http.EmbeddedHttpServer
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import io.circe.syntax._

class GetListingAcceptanceSpec extends TestServerHelper {

  override val server: EmbeddedHttpServer = createOnly

  test("listing that doesn't exist returns 404") {
    server.httpGet(
      path = "/listings/12345",
      andExpect = NotFound)
  }

  test("Get all two listings created") {

    val listingAustin = Listing(None, Contact("15126841100", "+1 512-684-1100"),
      Address("1011 Collins Ave", Some("1011"), "US", "Miami", Some("FL"), "United States"),
      Location(40.4255485534668, -3.7075681686401367))

    val listingBellevue = Listing(None, Contact("15126841100", "+1 512-684-1100"),
      Address("2012 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
      Location(40.4255485534668, -3.7075681686401367))

    When("A request is sent to create two unique listings")
    server.httpPost(
      path = "/listings",
      postBody = listingAustin.asJson.spaces2)

    server.httpPost(
      path = "/listings",
      postBody = listingBellevue.asJson.spaces2)

    Then("A request to retrieve all listings")

    val response = server.httpGet(
      path = "/listings")

    val mapper = new com.fasterxml.jackson.databind.ObjectMapper
    val list = mapper.readValue(response.contentString, classOf[java.util.ArrayList[java.util.Map[String, Object]]])
    assert(list.size() == 2)
  }

  test("Get listing in Austin") {

    val listingAustin = Listing(None, Contact("15126841100", "+1 512-684-1100"),
      Address("1013 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
      Location(40.4255485534668, -3.7075681686401367))

    val listingBellevue = Listing(None, Contact("15126841100", "+1 512-684-1100"),
      Address("2014 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
      Location(40.4255485534668, -3.7075681686401367))

    When("A request is sent to create two unique listings")
    val austinResp = server.httpPost(
      path = "/listings",
      postBody = listingAustin.asJson.spaces2)

    server.httpPost(
      path = "/listings",
      postBody = listingBellevue.asJson.spaces2)

    Then("A request to retrieve listing in Austin")

    val response = server.httpGet(
      path = "/listings?city=Austin")

    assert(response.contentString.contains(listingAustin.address.address))
  }

}
