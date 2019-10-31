package com.vrbo.listings.acceptance

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTestMixin

class ListingsApiServerAcceptanceSpec extends BaseMyServiceFeatureTest with FeatureTestMixin{

  override val server: EmbeddedHttpServer = BaseMyServiceFeatureTest.createOnly

  it("Server is up and running") {
    server.isHealthy.shouldBe(true)
  }
}