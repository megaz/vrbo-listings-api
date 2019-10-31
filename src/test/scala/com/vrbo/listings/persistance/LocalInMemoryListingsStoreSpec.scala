package com.vrbo.listings.persistance

import com.twitter.util.{Future => TwitterFuture}
import com.vrbo.listings.TwitterFutureAsyncSpec
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}

class LocalInMemoryListingsStoreSpec extends TwitterFutureAsyncSpec {

  val cache = new LocalInMemoryListingsStore

  "LocalInMemoryListingsStore" should {

    "store and retrieve a listing from the cache store" in {

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- cache.store(listing)
        r <- cache.get(UUID(l.id.get))
      } yield r

      fromTwitter(future).map { l => l.get.shouldEqual(listing) }
    }

    "delete a listing from the cache store" in {

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- cache.store(listing)
        _ <- cache.delete(UUID(l.id.get))
        r <- cache.get(UUID(l.id.get))
      } yield r

      fromTwitter(future).map { res => res.shouldEqual(None) }
    }

    "return a listing by address from the cache store" in {

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- cache.store(listing)
        r <- cache.get(l.address)
      } yield r

      fromTwitter(future).map { res => res.get.shouldEqual(listing) }
    }

    "update an existing listing and retrieve updated listing from the cache store" in {

      val id = uuid
      val listing = Listing(Some(id), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val updated = Listing(Some(id), Contact("15126841100", "+1 512-684-1100"),
        Address("2012 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        s <- cache.store(listing)
        u <- cache.update(UUID(s.id.get), updated)
        g <- cache.get(UUID(updated.id.get))
      } yield g

      fromTwitter(future).map { res => res.get.shouldEqual(updated) }
    }

    "not return listing that does not exist in the cache store" in {

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future = cache.get(UUID(listing.id.get))
      fromTwitter(future).map { res => res.shouldEqual(None) }

    }

    "not return listing when querying by listing address" in {

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("645 Elliott Ave W", Some("1011"), "US", "Seattle", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future = cache.get(listing.address)
      fromTwitter(future).map { res => res.shouldEqual(None) }

    }

    "return false for updating listing that does not exist" in {

      val id = uuid
      val listing = Listing(Some(id), Contact("15126841100", "+1 512-684-1100"),
        Address("645 Elliott Ave W", Some("1011"), "US", "Seattle", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future = cache.update(UUID(id), listing)
      fromTwitter(future).map { res => res.shouldEqual(false) }
    }

  }

}
