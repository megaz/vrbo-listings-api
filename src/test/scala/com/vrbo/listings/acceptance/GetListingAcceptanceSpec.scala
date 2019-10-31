package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status.{NoContent, NotFound}
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTestMixin

class GetListingAcceptanceSpec extends BaseMyServiceFeatureTest with FeatureTestMixin {

  override val server: EmbeddedHttpServer = BaseMyServiceFeatureTest.createOnly

  describe("Get Listing") {

    it("listing that doesn't exist returns 404") {

      server.httpGet(
        path = "/listings/12345",
        andExpect = NotFound)
    }

  }

}
