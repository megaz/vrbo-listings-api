package com.vrbo.listings.service

import java.util

import com.google.inject.ImplementedBy
import com.twitter.util.Future
import com.vrbo.listings.domain.Listing
import com.vrbo.listings.persistance.query.QueryBy
import scalacache.Entry

@ImplementedBy(classOf[VrboListingsService])
trait ListingsService[T, ID] {

  def get(id: ID): Future[Option[T]]
  def save(a: T): Future[T]
  def update(id: ID, t: T): Future[Boolean]
  def delete(id: ID): Future[Boolean]
  def getListingsBy(input : Option[String], queryBy: QueryBy) : Future[Iterable[Listing]]
  def size : Future[Long]

}
