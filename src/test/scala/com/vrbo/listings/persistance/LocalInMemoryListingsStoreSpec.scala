package com.vrbo.listings.persistance

import com.twitter.util.{Future => TwitterFuture}
import com.vrbo.listings.TwitterFutureAsyncSpec
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import com.vrbo.listings.persistance.query.QueryBy
import org.scalatest.Assertion

import scala.concurrent.Future

class LocalInMemoryListingsStoreSpec extends TwitterFutureAsyncSpec {

  def assertAll(futures: List[Future[Assertion]]): Future[Assertion] =
    Future.sequence(futures) map (_.foreach(a => a)) map (_ => succeed)

  def assert(future: Future[Assertion]): Future[Assertion] = future map (_ => succeed)

  "LocalInMemoryListingsStore" should {

    "store and retrieve a listing from the cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- cache.store(listing)
        r <- cache.get(UUID(l.id.get))
      } yield r

      val futures = List(fromTwitter(future)
        .map { l => l.get.shouldEqual(listing) },
        fromTwitter(cache.size).map { s => s.shouldEqual(1) })

      assertAll(futures)
    }

    "delete a listing from the cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- cache.store(listing)
        _ <- cache.delete(UUID(l.id.get))
        r <- cache.get(UUID(l.id.get))
      } yield r

      val futures = List(fromTwitter(future)
        .map { l => l.shouldEqual(None) },
        fromTwitter(cache.size).map { s => s.shouldEqual(0) })

      assertAll(futures)
    }

    "return a listing by address from the cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- cache.store(listing)
        r <- cache.get(l.address)
      } yield r

      assert(fromTwitter(future).map { res => res.get.shouldEqual(listing) })
    }

    "update an existing listing and retrieve updated listing from the cache store" in {

      val cache = new LocalInMemoryListingsStore

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

      val futures = List(fromTwitter(future)
        .map { l => l.get.shouldEqual(updated) },
        fromTwitter(cache.size).map { s => s.shouldEqual(1) })

      assertAll(futures)
    }

    "not return listing that does not exist in the cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future = cache.get(UUID(listing.id.get))
      assert(fromTwitter(future).map { res => res.shouldEqual(None) })

    }

    "not return listing when querying by listing address" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("645 Elliott Ave W", Some("1011"), "US", "Seattle", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future = cache.get(listing.address)
      assert(fromTwitter(future).map { res => res.shouldEqual(None) })

    }

    "return false for updating listing that does not exist" in {

      val cache = new LocalInMemoryListingsStore

      val id = uuid
      val listing = Listing(Some(id), Contact("15126841100", "+1 512-684-1100"),
        Address("645 Elliott Ave W", Some("1011"), "US", "Seattle", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future = cache.update(UUID(id), listing)
      assert(fromTwitter(future).map { res => res.shouldEqual(false) })
    }

    "return correct size of listings cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val listing2 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2012 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Long] = for {
        _ <- cache.store(listing)
        _ <- cache.store(listing2)
        s <- cache.size
      } yield s

      assert(fromTwitter(future).map { l => l shouldEqual 2 })
    }

    "return all listings in cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val listing2 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2012 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Set[Listing]] = for {
        _ <- cache.store(listing)
        _ <- cache.store(listing2)
        s <- cache.getListingsBy(None, QueryBy.ALL)
      } yield s

      assert(fromTwitter(future).map { l =>
        assert(l.size == 2)
        assert(l.contains(listing))
        assert(l.contains(listing2))
      })
    }

    "return all Austin listings in cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing1 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val listing2 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("4325 E 2th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val listing3 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2012 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Set[Listing]] = for {
        _ <- cache.store(listing1)
        _ <- cache.store(listing2)
        _ <- cache.store(listing3)
        s <- cache.getListingsBy(Some("Austin"), QueryBy.CITY)
      } yield s

      assert(fromTwitter(future).map { l =>
        assert(l.size == 2)
        assert(l.contains(listing1))
        assert(l.contains(listing2))
      })
    }

    "return no matching listings in cache store" in {

      val cache = new LocalInMemoryListingsStore

      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Set[Listing]] = for {
        _ <- cache.store(listing)
        s <- cache.getListingsBy(Some("London"), QueryBy.CITY)
      } yield s

      assert(fromTwitter(future).map { l => assert(l.isEmpty) })
    }

  }

}
