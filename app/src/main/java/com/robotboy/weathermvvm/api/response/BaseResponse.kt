package com.robotboy.weathermvvm.api.response

open class BaseResponse(val error: String? = null) {

    val success: Boolean
        get() {
            return error == null
        }

    val errorCode: Int?
        get() {
            return if (error != null && error.isNotEmpty()) {
                error.toInt()
            } else {
                null
            }
        }
}
