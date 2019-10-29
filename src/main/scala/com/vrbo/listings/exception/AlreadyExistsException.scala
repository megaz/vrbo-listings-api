package com.vrbo.listings.exception

class AlreadyExistsException(val message: String, val detail: String, val throwable: Throwable)
  extends Exception(message, throwable) {
  def this(message: String, detail: String) = this(message, detail, null)

  def toResponse(instance: String): ConflictResponse = ConflictResponse("ok")
}
