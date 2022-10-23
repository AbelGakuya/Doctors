package com.hfad.daktari1

data class Doctor(var title: String?= null,
                  var firstName: String?= null,
                  var lastName: String?= null,
                  var name: String? = title + "" + firstName + "" + lastName,
                  var Bio: String?= null,
                  var uid: String? = null,
                  var imageUrl: String? = null)
