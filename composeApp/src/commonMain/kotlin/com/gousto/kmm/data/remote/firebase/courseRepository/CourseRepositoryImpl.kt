package com.gousto.kmm.data.remote.firebase.courseRepository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class CourseRepositoryImpl : CourseRepository {

    override suspend fun getDomaioCourse(): CourseModel {
        val snapshot = Firebase.firestore
            .collection(COURSES)
            .document(DOMAIO_COURSE)
            .get()

        return snapshot.data<CourseModel>()
    }

    override suspend fun getAllCourses(): List<CourseModel> {
        val snapshot = Firebase.firestore
            .collection(COURSES)
            .get()

        return snapshot.documents.mapNotNull { doc ->
            doc.data<CourseModel>()
        }

    }

    companion object {
        const val COURSES = "courses"
        const val DOMAIO_COURSE = "71FVcK8EJaTw3JRg49Hk"
    }
}