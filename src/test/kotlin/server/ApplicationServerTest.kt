@file:Suppress("FunctionName")

package server

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import rawhttp.core.RawHttp
import rawhttp.core.client.TcpRawHttpClient

internal class ApplicationServerTest {

  private val syncRequestPort = 4000
  private val asyncRequestPort = 3000
  private val requestCount = 500

  private val httpClient = TcpRawHttpClient()
  private val http = RawHttp()
  private var syncServer: ApplicationServer? = null
  private var asyncServer: ApplicationServer? = null

  private fun makeRequests(count: Int, port: Int) {
    repeat(count) {
      val request = http.parseRequest("GET http://localhost:$port")
      httpClient.send(request).eagerly()
    }
  }

  @BeforeEach
  fun setup() {
    syncServer = ApplicationServer(syncRequestPort).initialize(false)
    asyncServer = ApplicationServer(asyncRequestPort).initialize()
  }

  @AfterEach
  fun teardown() {
    syncServer?.stop()
    asyncServer?.stop()
  }

  @Test
  fun `responds to request with html document of the request headers`() {
    val request = http.parseRequest("GET http://localhost:$asyncRequestPort")
    val response = httpClient.send(request).eagerly()
    val responseBody = response.body.get().toString()
    assertEquals("<!DOCTYPE html><html><pre>${request.headers}</pre></html>", responseBody)
  }

  @Test
  fun `handles requests synchronously and asynchronously - benchmark`() {
    val startA = System.currentTimeMillis()
    makeRequests(requestCount, syncRequestPort)
    val endA = System.currentTimeMillis()

    println("took ${endA - startA} ms to handle $requestCount requests synchronously")

    val startB = System.currentTimeMillis()
    makeRequests(requestCount, asyncRequestPort)
    val endB = System.currentTimeMillis()

    println("took ${endB - startB} ms to handle $requestCount requests asynchronously")
  }

  @Test
  fun `memory usage - synchronous`() {
    makeRequests(requestCount, syncRequestPort)
    val usageKb = syncServer!!.memoryUsage() / 1000
    println("Memory used after handling $requestCount requests synchronously: $usageKb kb")
  }

  @Test
  fun `memory usage - asynchronous`() {
    makeRequests(requestCount, asyncRequestPort)
    val usageKb = asyncServer!!.memoryUsage() / 1000
    println("Memory used after handling $requestCount requests asynchronously: $usageKb kb")
  }

}