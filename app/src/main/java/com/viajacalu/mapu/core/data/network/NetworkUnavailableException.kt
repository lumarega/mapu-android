package com.viajacalu.mapu.core.data.network

// Thrown by data sources when a call fails because the server can't be reached
class NetworkUnavailableException(cause: Throwable? = null) : Exception("Network unavailable", cause)
