package com.example.democorekotlin.model.response

data class ModelCurrentLocation(
    val latitude: Double?,
    val lookupSource: String?,
    val longitude: Double?,
    val localityLanguageRequested: String?,
    val continent: String?,
    val continentCode: String?,
    val countryName: String?,
    val countryCode: String?,
    val principalSubdivision: String?,
    val principalSubdivisionCode: String?,
    val city: String?,
    val locality: String?,
    val postcode: String?,
    val plusCode: String?,
    val localityInfo: LocalityInfo?
)

data class LocalityInfo(
    val administrative: List<Administrative>?,
    val informative: List<Informative>?
)

data class Administrative(
    val name: String?,
    val description: String?,
    val isoName: String?,
    val order: Int?,
    val adminLevel: Int?,
    val isoCode: String?,
    val wikidataId: String?,
    val geonameId: Int?
)

data class Informative(
    val name: String?,
    val description: String?,
    val isoName: String?,
    val order: Int?,
    val isoCode: String?,
    val wikidataId: String?,
    val geonameId: Int?
)