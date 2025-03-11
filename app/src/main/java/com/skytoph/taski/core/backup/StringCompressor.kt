package com.skytoph.taski.core.backup

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

interface StringCompressor {
    fun compressString(data: String): ByteArray
    fun decompressString(data: ByteArray): String

    class Base : StringCompressor {

        override fun compressString(data: String): ByteArray {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val gzipOutputStream = GzipCompressorOutputStream(byteArrayOutputStream)
            gzipOutputStream.write(data.toByteArray())
            gzipOutputStream.close()
            return byteArrayOutputStream.toByteArray()
        }

        override fun decompressString(data: ByteArray): String {
            val gzipInputStream = GzipCompressorInputStream(ByteArrayInputStream(data))
            val byteArrayOutputStream = ByteArrayOutputStream()
            gzipInputStream.copyTo(byteArrayOutputStream)
            gzipInputStream.close()
            return byteArrayOutputStream.toString()
        }
    }
}