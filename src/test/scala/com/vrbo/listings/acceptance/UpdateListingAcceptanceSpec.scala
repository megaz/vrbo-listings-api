package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status.NoContent
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTestMixin
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import io.circe.Json
import io.circe.parser.parse
import io.circe.syntax._

class UpdateListingAcceptanceSpec extends BaseMyServiceFeatureTest with FeatureTestMixin {

  override val server: EmbeddedHttpServer = BaseMyServiceFeatureTest.createOnly

  describe("Update Listing") {

    it("listing that exists is successfully updated") {

      Given("A listing that is valid")
      val listing = Listing(None, Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val updated = Listing(None, Contact("15126841100", "+1 512-684-1100"),
        Address("2012 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      When("A requests is sent to create a listing")
      val postRes = server.httpPost(
        path = "/listings",
        postBody = listing.asJson.spaces2)

      val json: Json = parse(postRes.contentString).getOrElse(Json.Null)
      val id = json.asObject.get.toMap("id").toString.replace("\"", "")

      And("A requests is sent to update the listing")
      val updateResp = server.httpPut(
        path = "/listings/" + id,
        putBody = updated.asJson.spaces2)

      updateResp.status.shouldBe(NoContent)
    }

  }

}
