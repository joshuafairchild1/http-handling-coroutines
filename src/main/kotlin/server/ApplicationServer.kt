package server

import kotlinx.coroutines.experimental.*
import rawhttp.core.body.StringBody
import rawhttp.core.server.TcpRawHttpServer
import rawhttp.core.*
import java.util.*

class ApplicationServer(private val port: Int) {
  private val http = RawHttp()
  private var server: TcpRawHttpServer? = null
  private val threadsUsed = mutableSetOf<String>()

  fun initialize(handleRequestsAsync: Boolean = true): ApplicationServer {
    server = TcpRawHttpServer(port)
    server!!.start { handleRequest(it, handleRequestsAsync) }
    return this
  }

  fun stop() {
    server?.stop()
  }

  fun memoryUsage(): Long {
    val runtime = Runtime.getRuntime()
    val freeSize = runtime.freeMemory()
    val totalSize = runtime.totalMemory()
    return totalSize - freeSize
  }

  private fun handleRequest(request: RawHttpRequest, async: Boolean): Optional<RawHttpResponse<*>> {
    return try {
      if (async) {
        runBlocking(CommonPool) { respondAsync(request) }
      } else respond(request)
    } catch (ex: Exception) {
      ex.printStackTrace()
      return Optional.ofNullable(null)
    }
  }

  private suspend fun respondAsync(request: RawHttpRequest): Optional<RawHttpResponse<*>> =
    withContext(DefaultDispatcher) { respond(request) }

  private fun respond(request: RawHttpRequest): Optional<RawHttpResponse<*>> {
    threadsUsed.add(Thread.currentThread().name)
//    println("handling request on thread ${Thread.currentThread().name}")
    return Optional.ofNullable(http.parseResponse("HTTP/1.1 200 OK\nContent-Type: text/html")
      .withBody(StringBody("<!DOCTYPE html><html><pre>${request.headers}</pre></html>")))
  }
}