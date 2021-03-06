package id.ergun.mymoviedb.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.ergun.mymoviedb.core.BuildConfig.BASE_URL
import id.ergun.mymoviedb.core.BuildConfig.HOST_NAME
import id.ergun.mymoviedb.core.data.remote.ApiService
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by alfacart on 20/11/20.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(client)
      .build()

  @Provides
  @Singleton
  fun provideOkHttp(@ApplicationContext context: Context): OkHttpClient {
    val okBuilder = OkHttpClient.Builder()

    val httpLogging = HttpLoggingInterceptor()
    httpLogging.level = HttpLoggingInterceptor.Level.BODY

    val certificatePinner = CertificatePinner.Builder()
        .add(HOST_NAME, "sha256/+vqZVAzTqUP8BGkfl88yU7SQ3C8J2uNEa55B7RZjEg0=")
        .add(HOST_NAME, "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA=")
        .add(HOST_NAME, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
        .build()

    okBuilder
        .addInterceptor(httpLogging)
        .addInterceptor(ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
        )
        .certificatePinner(certificatePinner)
    return okBuilder.build()
  }

  @Provides
  @Singleton
  fun provideGson(): Gson = GsonBuilder().create()

  @Provides
  @Singleton
  fun getService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
  }
}