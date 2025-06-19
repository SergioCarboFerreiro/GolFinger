package com.gousto.kmm.domain

import com.gousto.kmm.data.remote.firebase.courseRepository.CourseRepository

class GetAllCoursesUseCase(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke() = courseRepository.getAllCourses()
}