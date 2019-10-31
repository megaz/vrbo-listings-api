package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status.BadRequest
import com.twitter.finatra.http.EmbeddedHttpServer
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import io.circe.syntax._

class CreateListingValidationAcceptanceSpec extends TestServerHelper {

  override val server:  EmbeddedHttpServer = createOnly

  test("listing is not created due to invalid country code") {

    Given("A listing that has invalid countryCode")
    val countryCode = "USAA"
    val listing = Listing(None, Contact("15126841100", "+1 512-684-1100"),
      Address("1011 W 5th St", Some("1011"), countryCode, "Austin", Some("TX"), "United States"),
      Location(40.4255485534668, -3.7075681686401367))

    When("A request is sent to create the listing")
    val response = server.httpPost(
      path = "/listings",
      postBody = listing.asJson.spaces2)

    Then("The listing is successfully created with HTTP status 201")
    response.status.shouldBe(BadRequest)
    assert(response.contentString ==
      """{"errors":["listing.address.countryCode: [USAA] is not a valid country code"]}""")
  }

}
