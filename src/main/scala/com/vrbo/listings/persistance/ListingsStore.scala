package com.vrbo.listings.persistance

import com.google.inject.ImplementedBy
import com.twitter.util.Future
import com.vrbo.listings.domain.Address

@ImplementedBy(classOf[LocalInMemoryListingsStore])
trait ListingsStore[T, ID] {

  def store(t: T): Future[T]
  def get(id: ID): Future[Option[T]]
  def get(address: Address): Future[Option[T]]
  def delete(id: ID) : Future[Boolean]
  def update(id: ID, t: T): Future[Boolean]

}
