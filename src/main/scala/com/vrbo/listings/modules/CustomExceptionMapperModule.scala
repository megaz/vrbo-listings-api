package com.vrbo.listings.modules

import javax.inject.Singleton
import com.twitter.finatra.http.exceptions.ExceptionManager
import com.twitter.inject.{Injector, TwitterModule}
import com.vrbo.listings.exception.ConflictExceptionMapper

@Singleton
object CustomExceptionMapperModule extends TwitterModule {
  override def singletonStartup(injector: Injector): Unit = {
    val manager = injector.instance[ExceptionManager]
    manager.add[ConflictExceptionMapper]
  }
}