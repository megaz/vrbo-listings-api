package com.vrbo.listings.domain.id

case class UUID(generatedId: String) extends Id[String] {
  override def id(): String = generatedId
}
