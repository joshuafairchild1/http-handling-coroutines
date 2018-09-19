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
  private val httpClient = TcpRawHttpClient()
  private val http = RawHttp()
  private var syncServer: ApplicationServer? = null
  private var asyncServer: ApplicationServer? = null

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
  fun `handles requests synchronously and asynchronously (benchmark)`() {
    val requestCount = 1000
    val startA = System.currentTimeMillis()
    repeat(requestCount) {
      val request = http.parseRequest("GET http://localhost:$syncRequestPort")
      httpClient.send(request).eagerly()
    }
    val endA = System.currentTimeMillis()

    println("took ${endA - startA}ms to handle $requestCount requests synchronously")

    val startB = System.currentTimeMillis()
    repeat(requestCount) {
      val request = http.parseRequest("GET http://localhost:$asyncRequestPort")
      httpClient.send(request).eagerly()
    }
    val endB = System.currentTimeMillis()

    println("took ${endB - startB}ms to handle $requestCount requests asynchronously")
  }

}