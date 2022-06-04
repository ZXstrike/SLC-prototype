package com.slc.prototype

class ClassData(
    var className : String,
    var classImage : String,
    var classID : String
){
    init {
        this.className = className
        this.classImage = classImage
        this.classID = classID
    }

    fun getName():String?{
        return className
    }

    fun getImage():String?{
        return classImage
    }

    fun getID():String?{
        return classID
    }
}


