package com.example.kamalsweets.Model

data class AddProductModel (
    val productCategory:String?="",
    val productCoverImage:String?="",
    val productDiscription:String?="",
    val productImages:ArrayList<String>?= ArrayList(),
    val productMRP:String?="",
    val productName:String?="",
    val productSp:String?="",
    val produductID:String?="",
    val productUnit:String?="",
    val stockStatus:String?=""

    )