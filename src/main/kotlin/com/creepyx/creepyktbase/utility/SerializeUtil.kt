package com.creepyx.creepyktbase.utility

import java.io.*
import java.util.*

fun <T> toBase64String(obj: T): String {
    try {
        val outputStream = ByteArrayOutputStream()
        val dataOutput = ObjectOutputStream(outputStream)

        dataOutput.writeObject(obj)
        dataOutput.close()
        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    } catch (e: IOException) {
        throw RuntimeException("Unable to serialize object", e)
    }
}

fun <T> fromBase64String(base64: String?): T? {
    if (base64.isNullOrEmpty()) {
        return null
    }
    try {
        val data = Base64.getDecoder().decode(base64)
        val inputStream = ByteArrayInputStream(data)

        return ObjectInputStream(inputStream).readObject() as T
    } catch (e: IOException) {
        throw RuntimeException("Unable to deserialize object", e)
    } catch (e: ClassNotFoundException) {
        throw RuntimeException("Unable to deserialize object", e)
    }
}