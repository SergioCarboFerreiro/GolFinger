package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.courseRepository.CourseModel
import com.gousto.kmm.data.remote.firebase.courseRepository.CourseRepository

class GetDomaioCoursesUseCase(
    private val courseRepository: CourseRepository
) {
    suspend fun get(): CourseModel {
        return courseRepository.getDomaioCourse()
    }
}
