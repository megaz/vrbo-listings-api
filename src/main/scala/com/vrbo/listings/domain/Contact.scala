package com.vrbo.listings.domain

import com.twitter.finatra.validation.Size

case class Contact(@Size(min = 5, max = 12) phone: String, @Size(min = 5, max = 16)formattedPhone: String)
