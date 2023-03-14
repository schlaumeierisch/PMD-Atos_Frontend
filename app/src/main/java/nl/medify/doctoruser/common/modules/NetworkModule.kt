package nl.medify.doctoruser.common.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.medify.doctoruser.common.api.MedifyAPIService
import nl.medify.doctoruser.common.api.MedifyRetrofitAPI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMedifyAPI(): MedifyAPIService {
        return MedifyRetrofitAPI.apiService
    }
}