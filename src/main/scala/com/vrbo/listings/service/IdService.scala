package com.vrbo.listings.service

import com.google.inject.ImplementedBy
import com.twitter.util.Future

@ImplementedBy(classOf[UUIDGenerationIdService])
trait IdService[T] {

  def getId: Future[T]

}
