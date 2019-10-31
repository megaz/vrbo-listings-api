package com.vrbo.listings

import com.twitter.util.{Return, Throw, Future => TwitterFuture}
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.{Future, Promise}

abstract class TwitterFutureAsyncSpec extends AsyncWordSpec with Matchers {

  def fromTwitter[A](twitterFuture: TwitterFuture[A]): Future[A] = {
    val promise = Promise[A]()
    twitterFuture respond {
      case Return(a) => promise success a
      case Throw(e) => promise failure e
    }
    promise.future
  }

  def uuid = java.util.UUID.randomUUID.toString
}
