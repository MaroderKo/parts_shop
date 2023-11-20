package com.autosale.shop.util

import kotlinx.coroutines.*

object CoroutineUtil {
    private suspend fun getData(): String
    {
        delay(5000)
        return "Hello world"
    }
    fun run() = runBlocking {
        launch(Dispatchers.Default) {
            println("Start of the coroutine")
            val data = async { getData() }
            delay(1000)

            println("Data: ${data.await()}")
            println("End of the coroutine")

        }
    }
}