package com.vrbo.listings.service

import com.google.inject.ImplementedBy
import com.twitter.util.Future

@ImplementedBy(classOf[VrboListingsService])
trait ListingsService[T, ID] {

  def get(id: ID): Future[Option[T]]
  def save(a: T): Future[T]
  def update(id: ID, t: T): Future[Boolean]
  def delete(id: ID): Future[Boolean]

}
