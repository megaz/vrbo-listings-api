package com.vrbo.listings.service

import com.twitter.util.Future
import com.vrbo.listings.domain.id.UUID
import javax.inject.Singleton

@Singleton
class UUIDGenerationIdService extends IdService[UUID] {

  def generateId: Future[UUID] = {
    Future.value(UUID(java.util.UUID.randomUUID.toString))
  }
}