package com.vrbo.listings.persistance

import com.github.benmanes.caffeine.cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.twitter.util.Future
import com.twitter.util.logging.Logging
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.domain.{Address, Listing}
import com.vrbo.listings.persistance.query.QueryBy
import com.vrbo.listings.util.Pattern
import scalacache._
import scalacache.caffeine.CaffeineCache
import scalacache.modes.try_._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success}

class LocalInMemoryListingsStore extends ListingsStore[Listing, UUID] with Logging {

  private val underlyingListingsCache: cache.Cache[String, Entry[Listing]] = Caffeine.newBuilder().build[String, Entry[Listing]]
  private val underlyingAddressCache: cache.Cache[String, Entry[String]] = Caffeine.newBuilder().build[String, Entry[String]]
  private val underlyingCityCache: cache.Cache[String, Entry[List[String]]] = Caffeine.newBuilder().build[String, Entry[List[String]]]

  implicit val listingsCache: Cache[Listing] = CaffeineCache(underlyingListingsCache)
  implicit val addressToListingIdCache: Cache[String] = CaffeineCache(underlyingAddressCache)
  implicit val cityToListingIdCache: Cache[List[String]] = CaffeineCache(underlyingCityCache)

  override def store(listing: Listing): Future[Listing] = {
    listingsCache.put(listing.id.get)(listing, ttl = None) match {
      case Success(_) =>
        addressToListingIdCache.put(removeWhiteSpace(listing.address.address))(listing.id.get, ttl = None)
        val cityKey = removeWhiteSpace(listing.address.city)

        val entry: Option[Entry[List[String]]] = Option(underlyingCityCache.getIfPresent(listing.address.city))

        entry match {
          case Some(_) => val listingIds = entry.get.value ::: List(listing.id.get)
            cityToListingIdCache.put(cityKey)(listingIds, ttl = None)
          case None => cityToListingIdCache.put(cityKey)(List(listing.id.get), ttl = None)
        }
      case Failure(_) => throw new RuntimeException("Could not store listing.")
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
    addressToListingIdCache.get(removeWhiteSpace(addr.address)) match {
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

  private def removeWhiteSpace(input: String): String = Pattern.removeWhitespace(input)

  override def getListingsBy(input: Option[String], queryBy: QueryBy): Future[Set[Listing]] = {
    queryBy match {
      case QueryBy.ALL =>
        Future.value(underlyingListingsCache.asMap().values().asScala.toSet.map { item: Entry[Listing] => item.value })
      case QueryBy.CITY =>
        val cacheEntry = Option(underlyingCityCache.asMap().get(input.get))
        cacheEntry match {
          case Some(_) => Future.value(underlyingListingsCache
            .getAllPresent(underlyingCityCache.asMap().get(input.get).value.asJava).values()
            .asScala.toSet.map { item: Entry[Listing] => item.value })
          case None => Future.value(Set())
        }
    }
  }

  override def size: Future[Long] = Future.value(underlyingListingsCache.estimatedSize())
}
