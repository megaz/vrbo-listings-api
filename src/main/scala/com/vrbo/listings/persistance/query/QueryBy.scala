package com.vrbo.listings.persistance.query

sealed trait QueryBy

object QueryBy {
  case object CITY extends QueryBy
  case object ALL extends QueryBy
}
