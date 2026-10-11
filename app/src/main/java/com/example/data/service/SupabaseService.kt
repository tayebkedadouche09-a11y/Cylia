package com.example.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Service to communicate with Supabase PostgREST and Realtime endpoints.
 * Supports secure operations using user-configured or build-configured anon key.
 */
class SupabaseService(
    private var supabaseUrl: String = "",
    private var anonKey: String = ""
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    fun isConfigured(): Boolean {
        return supabaseUrl.isNotBlank() && anonKey.isNotBlank() && supabaseUrl.startsWith("http")
    }

    fun updateConfig(url: String, key: String) {
        this.supabaseUrl = url.trim().removeSuffix("/")
        this.anonKey = key.trim()
    }

    fun getSupabaseUrl(): String = supabaseUrl
    fun getAnonKey(): String = anonKey

    suspend fun verifyRoom(roomCode: String): Result<JSONObject?> = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext Result.failure(Exception("Supabase credentials not configured"))
        }

        try {
            val endpoint = "$supabaseUrl/rest/v1/rooms?code=eq.$roomCode&select=*&limit=1"
            val request = Request.Builder()
                .url(endpoint)
                .addHeader("apikey", anonKey)
                .addHeader("Authorization", "Bearer $anonKey")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
                }
                val body = response.body?.string() ?: "[]"
                val jsonArray = JSONArray(body)
                if (jsonArray.length() > 0) {
                    Result.success(jsonArray.getJSONObject(0))
                } else {
                    Result.success(null)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRoom(roomCode: String, hostRole: String): Result<Boolean> = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext Result.failure(Exception("Supabase credentials not configured"))
        }

        try {
            val payload = JSONObject().apply {
                put("code", roomCode)
                put("host_role", hostRole)
                put("status", "waiting")
            }
            val requestBody = payload.toString().toRequestBody("application/json".toMediaType())
            val endpoint = "$supabaseUrl/rest/v1/rooms"
            val request = Request.Builder()
                .url(endpoint)
                .addHeader("apikey", anonKey)
                .addHeader("Authorization", "Bearer $anonKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful || response.code == 201) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to create room: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
