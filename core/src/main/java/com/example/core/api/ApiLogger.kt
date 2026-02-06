package com.example.core.api

import android.util.Log
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class ApiLogger(
    private val tag: String = "API_LOGGER",
    private val showRequest: Boolean = true,
    private val showRequestHeader: Boolean = false,
    private val showRequestBody: Boolean = true,
    private val showResponseBody: Boolean = true,
    private val showResponseHeader: Boolean = false,
    private val showError: Boolean = true,
    private val maxWidth: Int = 110,
    private val compact: Boolean = true
) : Interceptor {

    companion object {
        private const val UTF8 = "UTF-8"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.nanoTime()

        return try {
            val response = chain.proceed(request)
            val endTime = System.nanoTime()
            val duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)

            if (showRequest) {
                logRequest(request)
            }

            logResponse(response, duration)
            response
        } catch (e: Exception) {
            val endTime = System.nanoTime()
            val duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)

            if (showError) {
                logError(request, e, duration)
            }
            throw e
        }
    }

    private fun logRequest(request: Request) {
        printBoxed(
            header = "Request ║ ${request.method}",
            text = request.url.toString()
        )

        if (showRequestHeader) {
            val headers = mutableMapOf<String, String>()
            request.headers.forEach { (name, value) ->
                headers[name] = value
            }
            printMapAsTable(headers, "Headers")
        }

        if (showRequestBody && request.method != "GET") {
            request.body?.let { body ->
                try {
                    val buffer = Buffer()
                    body.writeTo(buffer)
                    val charset = body.contentType()?.charset(Charset.forName(UTF8)) ?: Charsets.UTF_8
                    val bodyString = buffer.readString(charset)

                    if (bodyString.isEmpty()) return
                    if (body.contentType()?.subtype == "x-www-form-urlencoded") {
                        Log.d(tag, "╔ Body (Form)")
                        Log.d(tag, "║")
                        bodyString.split("&").forEach { pair ->
                            val parts = pair.split("=")
                            if (parts.size == 2) {
                                Log.d(tag, "║ ${parts[0]} = ${parts[1]}")
                            } else {
                                Log.d(tag, "║ $pair")
                            }
                        }
                        Log.d(tag, "║")
                        printLine("╚")
                    } else if (bodyString.startsWith("{") || bodyString.startsWith("[")) {
                        // JSON
                        try {
                            val pretty = if (bodyString.startsWith("{"))
                                JSONObject(bodyString).toString(2)
                            else
                                JSONArray(bodyString).toString(2)
                            Log.d(tag, "╔ Body (JSON)")
                            pretty.split("\n").forEach { line ->
                                Log.d(tag, "║ $line")
                            }
                            printLine("╚")
                        } catch (_: Exception) {
                            printBlock(bodyString, "Body")
                        }
                    } else {
                        // Plain text
                        printBlock(bodyString, "Body")
                    }
                } catch (e: Exception) {
                    Log.d(tag, "║ [Request body could not be logged: ${e.message}]")
                }
            }
        }
    }

    private fun logResponse(response: Response, duration: Long) {
        printBoxed(
            header = "Response ║ ${response.request.method} ║ Status: ${response.code} ${response.message} ║ Time: ${duration}ms",
            text = response.request.url.toString()
        )

        if (showResponseHeader) {
            val headers = mutableMapOf<String, String>()
            response.headers.forEach { (name, value) ->
                headers[name] = value
            }
            printMapAsTable(headers, "Headers")
        }

        if (showResponseBody) {
            val responseBody = response.peekBody(Long.MAX_VALUE)
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer

            val contentType = responseBody.contentType()
            val charset = contentType?.charset(Charset.forName(UTF8)) ?: Charset.forName(UTF8)

            if (isPlainText(contentType)) {
                val bodyString = buffer.clone().readString(charset)
                if (bodyString.isNotEmpty()) {
                    Log.d(tag, "╔ Data")
                    Log.d(tag, "║")
                    try {
                        when {
                            bodyString.startsWith("{") -> {
                                val jsonObject = JSONObject(bodyString)
                                printPrettyJson(jsonObject.toString(if (compact) 0 else 2))
                            }
                            bodyString.startsWith("[") -> {
                                val jsonArray = JSONArray(bodyString)
                                printPrettyJson(jsonArray.toString(if (compact) 0 else 2))
                            }
                            else -> {
                                printBlock(bodyString)
                            }
                        }
                    } catch (_: Exception) {
                        printBlock(bodyString)
                    }
                    Log.d(tag, "║")
                    printLine("╚")
                }
            } else {
                Log.d(tag, "╔ Data")
                Log.d(tag, "║ [Binary data - ${responseBody.contentLength()} bytes]")
                Log.d(tag, "║")
                printLine("╚")
            }
        }
    }

    private fun logError(request: Request, error: Exception, duration: Long) {
        printBoxed(
            header = "Error ║ ${request.method} ║ Time: ${duration}ms",
            text = "${request.url} - ${error.message}"
        )
    }

    private fun printBoxed(header: String, text: String) {
        Log.d(tag, "")
        Log.d(tag, "╔╣ $header")
        Log.d(tag, "║  $text")
        printLine("╚")
    }

    private fun printLine(pre: String = "", suf: String = "╝") {
        Log.d(tag, "$pre${"═".repeat(maxWidth)}$suf")
    }

    private fun printBlock(msg: String, header: String? = null) {
        if (header != null) {
            Log.d(tag, "╔ $header")
            Log.d(tag, "║")
        }

        val lines = (msg.length.toDouble() / maxWidth).toInt() + 1
        for (i in 0 until lines) {
            val start = i * maxWidth
            val end = minOf(start + maxWidth, msg.length)
            if (start < msg.length) {
                Log.d(tag, "║ ${msg.substring(start, end)}")
            }
        }

        if (header != null) {
            Log.d(tag, "║")
            printLine("╚")
        }
    }

    private fun printPrettyJson(jsonString: String) {
        val lines = jsonString.split("\n")
        lines.forEach { line ->
            Log.d(tag, "║ $line")
        }
    }

    private fun printMapAsTable(map: Map<String, String>, header: String) {
        if (map.isEmpty()) return

        Log.d(tag, "╔ $header")
        map.forEach { (key, value) ->
            printKV(key, value)
        }
        printLine("╚")
    }

    private fun printKV(key: String, value: String) {
        val pre = "╟ $key: "

        if (pre.length + value.length > maxWidth) {
            Log.d(tag, pre)
            printBlock(value)
        } else {
            Log.d(tag, "$pre$value")
        }
    }

    private fun isPlainText(mediaType: MediaType?): Boolean {
        if (mediaType == null) return false

        return when {
            mediaType.type == "text" -> true
            mediaType.subtype == "json" -> true
            mediaType.subtype == "xml" -> true
            mediaType.subtype == "html" -> true
            mediaType.subtype.endsWith("json") -> true
            mediaType.subtype.endsWith("xml") -> true
            else -> false
        }
    }
}