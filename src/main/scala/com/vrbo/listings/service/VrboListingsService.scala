package com.vrbo.listings.service

import com.twitter.util.Future
import com.vrbo.listings.domain.Listing
import com.vrbo.listings.domain.id.{Id, UUID}
import com.vrbo.listings.exception.ConflictException
import com.vrbo.listings.persistance.ListingsStore
import com.vrbo.listings.persistance.query.QueryBy
import javax.inject.{Inject, Singleton}

@Singleton
class VrboListingsService @Inject()(idService: UUIDGenerationIdService, listingsStore: ListingsStore[Listing, UUID]) extends ListingsService[Listing, UUID] {

  override def get(id: UUID): Future[Option[Listing]] = listingsStore.get(id)
  override def getListingsBy(input: Option[String], queryBy: QueryBy): Future[Iterable[Listing]] = listingsStore.getListingsBy(input, queryBy)
  override def update(id: UUID, listing: Listing): Future[Boolean] = listingsStore.update(id, listing)
  override def delete(id: UUID): Future[Boolean] = listingsStore.delete(id)
  override def size: Future[Long] = listingsStore.size
  override def save(listing: Listing): Future[Listing] = {
    for {
      _ <- listingExists(listing)
      id <- idService.generateId
      listing <- listingsStore.store(listing.withId(id.id()))
    } yield listing
  }

  def listingExists(listing: Listing): Future[Boolean] = {
    listingsStore.get(listing.address) map {
      case Some(_) => throw new ConflictException("Cannot create duplicate listing on address that already exists")
      case None => false
    }
  }

}
