package com.vrbo.listings.acceptance

import com.twitter.finatra.http.EmbeddedHttpServer

class ListingsApiServerAcceptanceSpec extends TestServerHelper {

  override val server:  EmbeddedHttpServer = createOnly

  test("Server is up and running") {
    server.isHealthy.shouldBe(true)
  }
}