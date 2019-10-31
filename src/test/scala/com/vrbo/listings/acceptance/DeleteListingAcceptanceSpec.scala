package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

class DeleteListingAcceptanceSpec extends TestServerHelper {

  override val server:  EmbeddedHttpServer = createOnly

    test("deletes a listing that was created") {

      Given("A listing that is valid")
      val listing = Listing(None, Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      When("A requests is sent to create a listing")
      val postRes = server.httpPost(
        path = "/listings",
        postBody = listing.asJson.spaces2)

      val json: Json = parse(postRes.contentString).getOrElse(Json.Null)
      val id = json.asObject.get.toMap("id").toString.replace("\"", "")

      And("A requests is sent to delete the listing")
      val deleteResp = server.httpDelete(
        path = "/listings/" + id)

      Then("The response should be HTTP status 204")
      deleteResp.status.shouldBe(NoContent)

      And("Verify listing has been deleted")

      server.httpGet(
        path = "/listings/" + id,
        andExpect = NotFound)

    }

    test("returns no content for deleting a listing that does not exist") {

      val deleteResp = server.httpDelete(
        path = "/listings/someId")

      deleteResp.status.shouldBe(NoContent)
    }

}
