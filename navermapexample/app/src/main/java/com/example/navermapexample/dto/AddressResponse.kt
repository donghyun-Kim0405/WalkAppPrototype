package com.example.navermapexample

import androidx.annotation.Keep
import java.util.*

data class AddressResponse(
    var status: String,
    var meta: Meta,
    var addresses: List<Address>
)

data class Meta(
    var totalCount: Int,
    var page: Int,
    var count: Int

)

data class Address(
    var roadAddress: String,
    var jibunAddress: String,
    var englishAddress: String,
    var addressElements: List<AddressElement>,
    var x: String,
    var y: String,
    var distance: Double
)

data class AddressElement(
    var types: List<String>,
    var longName: String,
    var shortName: String,
    var code: String
)