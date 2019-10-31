package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTestMixin
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import io.circe.syntax._

class CreateListingAcceptanceSpec extends BaseMyServiceFeatureTest with FeatureTestMixin {

  override val server: EmbeddedHttpServer = BaseMyServiceFeatureTest.createOnly

  describe("Creating Listing") {

    it("listing is successfully created") {

      Given("A listing that is valid")
      val listing = Listing(None, Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      When("A request is sent to create the listing")
      val response = server.httpPost(
        path = "/listings",
        postBody = listing.asJson.spaces2)

      Then("The listing is successfully created with HTTP status 201")
      response.status.shouldBe(Created)
    }

    it("listing with a duplicate address is not created") {

      Given("A listing that is valid")
      val listing = Listing(None, Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      When("Two requests with duplicate addresses are sent")
      server.httpPost(
        path = "/listings",
        postBody = listing.asJson.spaces2)

      val response = server.httpPost(
        path = "/listings",
        postBody = listing.asJson.spaces2)

      Then("The response should be HTTP status 409")
      response.status.shouldBe(Conflict)
      assert(response.contentString ==
        """{"type":"duplicate_address","message":"Cannot create duplicate listing on address that already exists"}""")
    }


  }
}
