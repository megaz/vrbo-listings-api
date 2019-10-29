package com.vrbo.listings.persistance

import com.twitter.util.Future
import com.twitter.util.logging.Logging
import com.vrbo.listings.domain.{Address, Listing}
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.util.{Failure, Success}

class LocalInMemoryListingsCacheStore extends ListingsStore[Listing] with Logging {

  implicit val listingsCache: Cache[Listing] = CaffeineCache[Listing]
  implicit val addressToListingIdCache: Cache[String] = CaffeineCache[String]

  override def store(listing: Listing): Future[Listing] = {
    listingsCache.put(listing.id)(listing, ttl = None)
    addressToListingIdCache.put(listing.address.address)(listing.id, ttl = None)
    Future.value(listing)
  }

  override def get(id: String): Future[Option[Listing]] = {
    val listing: Option[Listing] = listingsCache.get(id) match {
      case Success(value) => value
      case Failure(e) =>
        logger.info("caught error fetching listing from in memory cache", e)
        None
    }
    Future.value(listing)
  }

  override def delete(id: String): Future[Boolean] = {
    val result = listingsCache.remove(id) match {
      case Success(_) => true
      case Failure(e) =>
        logger.info("caught error removing listing from in memory cache", e)
        false
    }
    Future.value(result)
  }

  override def update(listing: Listing): Future[Listing] = {
    listingsCache.put(listing.id)(listing, ttl = None)
    Future.value(listing)
  }

  override def get(address: Address): Future[Option[Listing]] = {
    addressToListingIdCache.get(address.address) match {
      case Success(listingId) =>
        listingId match {
          case Some(listing) => get(listing)
          case None => Future.value(None)
        }
      case Failure(e) =>
        logger.info("caught error fetching listing from in memory cache", e)
        Future.value(None)
    }
  }
}
