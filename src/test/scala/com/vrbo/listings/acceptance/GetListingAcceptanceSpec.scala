package com.vrbo.listings.acceptance

import com.twitter.finagle.http.Status.NotFound
import com.twitter.finatra.http.EmbeddedHttpServer

class GetListingAcceptanceSpec extends  TestServerHelper {

  override val server:  EmbeddedHttpServer = createOnly

    test("listing that doesn't exist returns 404") {

      server.httpGet(
        path = "/listings/12345",
        andExpect = NotFound)

  }

}
