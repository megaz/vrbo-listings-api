package com.vrbo.listings.persistance

import com.google.inject.ImplementedBy
import com.twitter.util.Future
import com.vrbo.listings.domain.Address

@ImplementedBy(classOf[LocalInMemoryListingsCacheStore])
trait ListingsStore[T] {

  def store(t: T): Future[T]

  def get(id: String): Future[Option[T]]

  def get(address: Address): Future[Option[T]]

  def delete(id: String) : Future[Boolean]

  def update(t: T): Future[T]

}
