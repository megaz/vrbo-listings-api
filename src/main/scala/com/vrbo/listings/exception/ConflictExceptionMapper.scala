package com.vrbo.listings.exception

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder
import com.vrbo.listings.http.response.ConflictResponse
import javax.inject.{Inject, Singleton}

@Singleton
class ConflictExceptionMapper @Inject()(response: ResponseBuilder) extends ExceptionMapper[ConflictException] {

  override def toResponse(request: Request, exception: ConflictException): Response =
    response.conflict(ConflictResponse("duplicate_address", exception.message))
}