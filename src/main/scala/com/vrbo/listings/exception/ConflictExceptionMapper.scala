package com.vrbo.listings.exception

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder
import javax.inject.{Inject, Singleton}

@Singleton
class ConflictExceptionMapper @Inject()(response: ResponseBuilder) extends ExceptionMapper[AlreadyExistsException] {

  override def toResponse(request: Request, exception: AlreadyExistsException): Response =
    response.conflict(exception, request)
}