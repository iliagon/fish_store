package com.example.fishstore.mapper

import org.springframework.stereotype.Component
import java.net.URI

@Component
class UriToStringMapper {
    fun fromStringToUri(string: String): URI {
        return URI(string)
    }

    fun fromUriToString(uri: URI): String {
        return uri.toString()
    }
}