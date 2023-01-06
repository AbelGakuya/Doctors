package com.hfad.daktari1.roomdatabase

class DoctorData {


        var title: String? = null
        var firstName: String? = null
        var lastName: String? = null
        var bio:String? = null
        var uid:String? = null
        var token: String? = null
        var imageUrl: String? = null
        var name: String? = null

        constructor(){}

        constructor(
            title: String?,
        firstName: String?,
        lastName: String?,
        name: String,
        bio: String?,
        uid: String?,
        token: String?,
        imageUrl: String
        ){
            this.title = title
            this.firstName = firstName
            this.lastName = lastName
            this.name = title + "" + firstName + "" + lastName
            this.bio = bio
            this.uid = uid
            this.token = token
            this.imageUrl = imageUrl
        }


}