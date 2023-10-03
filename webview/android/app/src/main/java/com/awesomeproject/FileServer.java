package com.awesomeproject;

import android.content.Context;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

public class FileServer extends NanoHTTPD {
  private final Context context;

  public FileServer(Context context, int port) {
    super(port);
    this.context = context;
  }

  @Override
  public Response serve(IHTTPSession session) {
    String uri = session.getUri();
    try {
      InputStream inputStream = context.getAssets().open(uri.substring(1));
      return newChunkedResponse(Response.Status.OK, getMimeType(uri), inputStream);
    } catch (IOException e) {
      String str = new String();
      try {
        for (int i = 0; i < context.getAssets().list("").length; i++) {
          str += context.getAssets().list("")[i] + "    ";
        }
      } catch (IOException err) {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "File not found");
      }
      return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT,
          "File not found." + str + ";" + context.getFilesDir());
    }
  }

  private String getMimeType(String filePath) {
    String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
  }
}
