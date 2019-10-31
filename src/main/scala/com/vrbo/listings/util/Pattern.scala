package com.vrbo.listings.util

import scala.util.matching.Regex

object Pattern {

  val whitespaceRegex: Regex = "\\s".r
  def removeWhitespace(input: String): String = whitespaceRegex.replaceAllIn(input, "")

}
