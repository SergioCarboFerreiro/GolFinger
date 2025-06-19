package com.gousto.kmm.data.remote.firebase.courseRepository

interface CourseRepository {

    suspend fun getDomaioCourse(): CourseModel

    suspend fun getAllCourses(): List<CourseModel>
}