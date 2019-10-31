package com.vrbo.listings.exception

class ConflictException(val message: String, val detail: String, val throwable: Throwable)
  extends Exception(message, throwable) {
  def this(message: String, detail: String) = this(message, detail, null)
  def this(message: String) = this(message, null, null)
}
