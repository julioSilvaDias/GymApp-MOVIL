package com.example.fittrack

import java.io.Serializable
import java.util.Date

class Workout : Serializable {
    var name : String = ""
    var level : Long = 0
    var exercises : Long = 0
    var url : String = ""

    constructor(name : String, level : Long, exercises : Long, url : String) {
        this.name = name
        this.level = level
        this.exercises = exercises
        this.url = url
    }

    constructor()
}

