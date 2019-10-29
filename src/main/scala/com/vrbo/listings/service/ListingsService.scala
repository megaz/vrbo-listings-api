package com.vrbo.listings.service

import com.google.inject.ImplementedBy
import com.twitter.util.Future
import com.vrbo.listings.domain.http.ListingPostRequest

@ImplementedBy(classOf[VrboListingsService])
trait ListingsService[T] {

  def get(id: String): Future[Option[T]]
  def save(listing : ListingPostRequest): Future[T]
  def update(t : T): Future[Option[T]]
  def delete(id: String): Unit

}
