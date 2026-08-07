package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Peserta FasBrain",
    val age: Int = 22,
    val totalTestsTaken: Int = 0
)
