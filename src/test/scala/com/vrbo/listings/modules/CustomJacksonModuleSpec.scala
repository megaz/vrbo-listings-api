package com.vrbo.listings.modules

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.scalatest.{Matchers, WordSpec}

class CustomJacksonModuleSpec extends WordSpec with Matchers {

  "CustomJacksonModule" should {

    "define propertyNamingStrategy as lower camel case" in {
      val customJacksonModule = new CustomJacksonModule
      customJacksonModule.propertyNamingStrategy.shouldEqual(PropertyNamingStrategy.LOWER_CAMEL_CASE)
    }

  }

}
