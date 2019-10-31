package com.vrbo.listings.domain.id

trait Id[T] {
  def id(): T
}
