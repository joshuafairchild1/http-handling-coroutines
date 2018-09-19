package server

import kotlinx.coroutines.experimental.*
import rawhttp.core.body.StringBody
import rawhttp.core.server.TcpRawHttpServer
import rawhttp.core.*
import java.util.*

class ApplicationServer(private val port: Int) {
  private val http = RawHttp()
  private var server: TcpRawHttpServer? = null

  fun initialize(handleRequestsAsync: Boolean = true): ApplicationServer {
    server = TcpRawHttpServer(port)
    server!!.start {
      // runBlocking is used here to "bridge the gap" between the blocking function
      // that `start` expects will be passed, and the suspended execution of `handleRequest`
      runBlocking {
        val response = handleRequest(it, handleRequestsAsync)
        Optional.ofNullable(response)
      }
    }
    return this
  }

  fun stop() {
    server?.stop()
  }

  private suspend fun handleRequest(
    request: RawHttpRequest,
    async: Boolean
  ): RawHttpResponse<*> = withContext(DefaultDispatcher) {
    if (async) respondAsync(request) else respondWithDocument(request)
  }

  private suspend fun respondAsync(request: RawHttpRequest) =
    withContext(DefaultDispatcher) { respondWithDocument(request) }

  private fun respondWithDocument(request: RawHttpRequest): RawHttpResponse<Void> =
    http.parseResponse("HTTP/1.1 200 OK\nContent-Type: text/html")
      .withBody(StringBody("<!DOCTYPE html><html><pre>${request.headers}</pre></html>"))
}