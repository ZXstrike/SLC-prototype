package com.slc.prototype

class ClassData(
    var className : String,
    var teacherName : String,
    var classImage : String,
    var classID : String
){
    init {
        this.className = className
        this.teacherName = teacherName
        this.classImage = classImage
        this.classID = classID
    }

    fun getName():String?{
        return className
    }

    fun getTeacher():String?{
        return teacherName
    }

    fun getImage():String?{
        return classImage
    }

    fun getID():String?{
        return classID
    }
}


