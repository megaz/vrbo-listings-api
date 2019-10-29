package com.vrbo.listings.service

import com.twitter.util.Future
import com.vrbo.listings.domain.Listing
import com.vrbo.listings.domain.http.ListingPostRequest
import com.vrbo.listings.exception.AlreadyExistsException
import com.vrbo.listings.persistance.ListingsStore
import javax.inject.{Inject, Singleton}

@Singleton
class VrboListingsService @Inject()(idService: UUIDGenerationIdService, listingsStore: ListingsStore[Listing]) extends ListingsService[Listing] {

  override def get(id: String): Future[Option[Listing]] = {
    listingsStore.get(id)
  }

  // TODO: Handle collisions in addresses
  override def save(listingPostRequest: ListingPostRequest): Future[Listing] = {

    //    listingsStore.get(listingPostRequest.listing.address) map {
    //      case Some(x) => Right(handleConflict)
    //      case None => Left(createListing(listingPostRequest))
    //    }

    val future: Future[Listing] = for {
      _ <- listingExists(listingPostRequest)
      id <- idService.getId
      listing <- listingsStore.store(listingPostRequest.toDomain(id))
    } yield listing

    future
  }

  def listingExists(listingPostRequest: ListingPostRequest): Future[Boolean] = {
    listingsStore.get(listingPostRequest.listing.address) map {
      case Some(_) => throw new AlreadyExistsException("Listing already exists", null, null)
      case None => true
    }
  }

  def createListing(listingPostRequest: ListingPostRequest): Future[Listing] = {
    for {
      id <- idService.getId
      listing <- listingsStore.store(listingPostRequest.toDomain(id))
    } yield listing
  }

  override def update(listing: Listing): Future[Option[Listing]] = Future.value(None)

  override def delete(id: String): Unit = listingsStore.delete(id)
}
