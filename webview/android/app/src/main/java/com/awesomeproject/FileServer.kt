package com.awesomeproject

import android.content.Context
import android.webkit.MimeTypeMap
import fi.iki.elonen.NanoHTTPD
import java.io.IOException

class FileServer(private val context: Context, port: Int) : NanoHTTPD(port) {
    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        return try {
            val inputStream = context.assets.open(uri.substring(1))
            newChunkedResponse(Response.Status.OK, getMimeType(uri), inputStream)
        } catch (e: IOException) {
            val str = StringBuilder()
            try {
                for (i in context.assets.list("")!!.indices) {
                    str.append(context.assets.list("")!![i]).append("    ")
                }
            } catch (err: IOException) {
                return newFixedLengthResponse(
                        Response.Status.NOT_FOUND,
                        NanoHTTPD.MIME_PLAINTEXT,
                        "File not found"
                )
            }
            newFixedLengthResponse(
                    Response.Status.NOT_FOUND,
                    NanoHTTPD.MIME_PLAINTEXT,
                    "File not found.$str;${context.filesDir}"
            )
        }
    }

    private fun getMimeType(filePath: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
}
