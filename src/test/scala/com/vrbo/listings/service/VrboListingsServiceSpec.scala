package com.vrbo.listings.service

import com.twitter.util.{Future => TwitterFuture}
import com.vrbo.listings.TwitterFutureAsyncSpec
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.domain.{Address, Contact, Listing, Location}
import com.vrbo.listings.exception.ConflictException
import com.vrbo.listings.persistance.LocalInMemoryListingsStore
import com.vrbo.listings.persistance.query.QueryBy

class VrboListingsServiceSpec extends TwitterFutureAsyncSpec {

  "VrboListingsService" should {

    "return None for a listing that doesn't exist" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val future: TwitterFuture[Option[Listing]] = vrboListingsService.get(UUID(uuid))
      fromTwitter(future).map { res => res.shouldEqual(None) }
    }

    "return false for updating a listing that does not exist" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val id = uuid
      val listing = Listing(Some(id), Contact("15126841100", "+1 512-684-1100"),
        Address("2044 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Boolean] = vrboListingsService.update(UUID(id), listing)

      fromTwitter(future).map { res => res.shouldEqual(false) }
    }

    "return false for deleting a listing that does not exist" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val future: TwitterFuture[Boolean] = vrboListingsService.delete(UUID(uuid))
      fromTwitter(future).map { res => res.shouldEqual(false) }
    }

    "store and retrieve a listing from the vrbo listing service" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        l <- vrboListingsService.save(listing)
        r <- vrboListingsService.get(UUID(l.id.get))
      } yield r

      fromTwitter(future).map { l =>
        l shouldBe defined
        val res = l.get
        res.id should not be ""
        res.address.shouldEqual(listing.address)
        res.contact.shouldEqual(listing.contact)
        res.location.shouldEqual(listing.location)
      }
    }

    "throw a conflict exception if a listing with a duplicate address already exists" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1011 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Listing] = for {
        l <- vrboListingsService.save(listing)
        r <- vrboListingsService.save(listing)
      } yield r

      recoverToSucceededIf[ConflictException](fromTwitter(future))
    }

    "update a listing that already exists" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val id = uuid
      val listing = Listing(Some(id), Contact("15126841100", "+1 512-684-1100"),
        Address("2044 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val updated = Listing(Some(id), Contact("12343456543", "+1 234-345-6543"),
        Address("2012 N 2th St", Some("1234"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Option[Listing]] = for {
        s <- vrboListingsService.save(listing)
        u <- vrboListingsService.update(UUID(s.id.get), updated)
        g <- vrboListingsService.get(UUID(s.id.get))
      } yield g

      fromTwitter(future).map { res => res.get.shouldEqual(updated) }
    }

    "return correct size of listings" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)
      val listing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2055 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val listing2 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2066 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Long] = for {
        _ <- vrboListingsService.save(listing)
        _ <- vrboListingsService.save(listing2)
        s <- vrboListingsService.size
      } yield s

      fromTwitter(future).map { res => res.shouldEqual(2) }
    }

    "return all listings in Austin" in {

      val vrboListingsService = new VrboListingsService(new UUIDGenerationIdService, new LocalInMemoryListingsStore)

      val austinListing = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2055 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val austinListing2 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("1035 W 5th St", Some("1011"), "US", "Austin", Some("TX"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val listing3 = Listing(Some(uuid), Contact("15126841100", "+1 512-684-1100"),
        Address("2066 E 2th St", Some("1011"), "US", "Bellevue", Some("WA"), "United States"),
        Location(40.4255485534668, -3.7075681686401367))

      val future: TwitterFuture[Set[Listing]] = for {
        _ <- vrboListingsService.save(austinListing)
        _ <- vrboListingsService.save(austinListing2)
        _ <- vrboListingsService.save(listing3)
        s <- vrboListingsService.getListingsBy(Some("Austin"), QueryBy.CITY)
      } yield s.toSet

      fromTwitter(future).map { l =>
        assert(l.size == 2)
        assert(l.head.address == austinListing.address)
        assert(l.last.address == austinListing2.address)
      }
    }

  }

}
