package com.vrbo.listings

import com.google.inject.Module
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http._
import com.twitter.finatra.http.filters.{CommonFilters, ExceptionMappingFilter, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.vrbo.listings.controller.ListingsController
import com.vrbo.listings.modules.{CustomExceptionMapperModule, CustomJacksonModule}

object ListingsApiServer$ extends ListingsApiServer

class ListingsApiServer extends HttpServer {

  override val defaultHttpPort: String = ":8000"
  override def jacksonModule = new CustomJacksonModule

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[ExceptionMappingFilter[Request]]
      .filter[CommonFilters]
      .add[ListingsController]
  }

  override protected def modules: Seq[Module] = Seq(CustomExceptionMapperModule)
}