package com.lunatech.bean

import com.lunatech.model.Country

case class CountrySearchResult(result: Option[Country], exact: Boolean)
