package com.gousto.kmm.di

import com.gousto.kmm.domain.AuthUserUseCase
import com.gousto.kmm.domain.GetActiveRoundSessionIdByUserIdUseCase
import com.gousto.kmm.domain.GetAllCoursesUseCase
import com.gousto.kmm.domain.GetAllUsersProfileUseCase
import com.gousto.kmm.domain.GetCurrentUserProfileUseCase
import com.gousto.kmm.domain.GetRoundByIdUseCase
import com.gousto.kmm.domain.GetAllRoundsForUserUseCase
import com.gousto.kmm.domain.RegisterUserUseCase
import com.gousto.kmm.domain.SaveRoundUseCase
import com.gousto.kmm.domain.SignOutUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetCurrentUserProfileUseCase)
    factoryOf(::RegisterUserUseCase)
    factoryOf(::GetAllUsersProfileUseCase)
    factoryOf(::AuthUserUseCase)
    factoryOf(::GetRoundByIdUseCase)
    factoryOf(::SaveRoundUseCase)
    factoryOf(::GetActiveRoundSessionIdByUserIdUseCase)
    factoryOf(::GetAllCoursesUseCase)
    factoryOf(::SignOutUseCase)
    factoryOf(::GetAllRoundsForUserUseCase)
}