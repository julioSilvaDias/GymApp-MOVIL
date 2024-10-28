package com.example.fittrack

import java.io.Serializable
import java.util.Date

class Historic : Serializable {
    var completedExercises: String = ""
    var date: Date = Date()
    var expectedTime: String = ""
    var level: String = ""
    var nameWorkout: String = ""
    var totalTime: String = ""

    constructor(completedExercises: String, date: Date, expectedTime: String, level: String, nameWorkout: String, totalTime: String) {
        this.completedExercises = completedExercises
        this.date = date
        this.expectedTime = expectedTime
        this.level = level
        this.nameWorkout = nameWorkout
        this.totalTime = totalTime
    }

    constructor()
}

