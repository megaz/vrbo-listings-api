package com.vrbo.listings.domain.http

import com.twitter.finatra.request.RouteParam

case class ListingGetRequest(@RouteParam id: String)