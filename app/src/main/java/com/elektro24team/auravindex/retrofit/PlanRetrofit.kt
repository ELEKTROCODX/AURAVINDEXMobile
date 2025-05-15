package com.elektro24team.auravindex.retrofit
import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//Interface with API calls
interface PlanService{
    @GET("plan")
    suspend fun getPlans(): ApiResponse<List<Plan>>
}
//Object that manage the PlanService, is a singleton instance
object PlanClient{
    val apiService: PlanService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlanService::class.java)
    }
}