package com.vrbo.listings.controller

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.vrbo.listings.domain.Listing
import com.vrbo.listings.domain.id.UUID
import com.vrbo.listings.http.request.ListingRequest.{ListingDeleteRequest, ListingGetRequest, ListingPostRequest, ListingPutRequest}
import com.vrbo.listings.http.response.{ListingNotFound, ListingResponse}
import com.vrbo.listings.persistance.query.QueryBy
import com.vrbo.listings.service.ListingsService
import javax.inject.Inject

import scala.collection.JavaConverters._

class ListingsController @Inject()(listingsService: ListingsService[Listing, UUID]) extends Controller {


  private def getQueryType(params: List[java.util.Map.Entry[String, String]]): (Option[String], QueryBy) = {

    if (params.isEmpty) {
      (None,QueryBy.ALL)
    }
    else {
      params.head.getKey match {
        case "city" => (Some(params.head.getValue), QueryBy.CITY)
        case _ => (None, QueryBy.ALL)
      }
    }
  }

  get("/listings/:id") { listingsGetRequest: ListingGetRequest =>
    listingsService.get(UUID(listingsGetRequest.id)) map {
      case Some(listing) => response.ok(ListingResponse(listing))
      case None => response.notFound(ListingNotFound.get)
    }
  }

  get("/listings") { request: Request =>
    val query = getQueryType(request.getParams().asScala.toList)
    listingsService.getListingsBy(query._1, query._2).map(listings => listings map { listing => ListingResponse(listing)})
  }

  post("/listings") { listingPostRequest: ListingPostRequest =>
    listingsService.save(listingPostRequest.toDomain).map(listing => response.created(ListingResponse(listing)))
  }

  delete("/listings/:id") { listingDeleteRequest: ListingDeleteRequest =>
    listingsService.delete(UUID(listingDeleteRequest.id)) map (_ => response.noContent)
  }

  put("/listings/:id") { listingPutRequest: ListingPutRequest =>
    listingsService.update(UUID(listingPutRequest.id), listingPutRequest.toDomain).map(_ => response.noContent)
  }
}