package com.vrbo.listings.persistance

import com.twitter.util.Future
import com.twitter.util.logging.Logging
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.domain.{Address, Listing}
import com.vrbo.listings.util.Pattern
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.util.{Failure, Success}

class LocalInMemoryListingsStore extends ListingsStore[Listing, UUID] with Logging {

  implicit val listingsCache: Cache[Listing] = CaffeineCache[Listing]
  implicit val addressToListingIdCache: Cache[String] = CaffeineCache[String]

  override def store(listing: Listing): Future[Listing] = {
    listingsCache.put(listing.id.get)(listing, ttl = None) match {
      case Success(_) =>
        addressToListingIdCache.put(formatAddressStr(listing.address))(listing.id.get, ttl = None)
      case Failure(e) => throw new RuntimeException("Could not store listing.")
    }
    Future.value(listing)
  }

  override def get(id: UUID): Future[Option[Listing]] = {
    val listing: Option[Listing] = listingsCache.get(id.id()) match {
      case Success(value) => value
      case Failure(e) =>
        logger.info("caught error fetching listing from in memory cache", e)
        None
    }
    Future.value(listing)
  }

  override def delete(id: UUID): Future[Boolean] = {
    get(id) map {
      case Some(_) =>
        listingsCache.remove(id.id())
        addressToListingIdCache.remove(id.id())
        true
      case None => false
    }
  }

  override def update(id: UUID, listing: Listing): Future[Boolean] = {
    get(id) map {
      case Some(_) =>
        listingsCache.put(id.id())(listing, ttl = None)
        true
      case None => false
    }
  }

  override def get(addr: Address): Future[Option[Listing]] = {
    addressToListingIdCache.get(formatAddressStr(addr)) match {
      case Success(listingId) =>
        listingId match {
          case Some(listing) => get(UUID(listing))
          case None => Future.value(None)
        }
      case Failure(e) =>
        logger.info("caught error fetching listing from in memory cache", e)
        Future.value(None)
    }
  }

  private def formatAddressStr(add: Address): String = Pattern.removeWhitespace(add.address)

}
