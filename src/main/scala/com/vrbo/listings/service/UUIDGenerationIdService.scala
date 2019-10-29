package com.vrbo.listings.service

import java.util.UUID

import com.twitter.util.Future
import javax.inject.Singleton

@Singleton
class UUIDGenerationIdService extends IdService[String] {

  def getId: Future[String] = {
    Future.value(UUID.randomUUID.toString)
  }

}
