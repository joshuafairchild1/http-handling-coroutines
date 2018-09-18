@file:Suppress("FunctionName")

package server

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import rawhttp.core.RawHttp
import rawhttp.core.client.TcpRawHttpClient

internal class ApplicationServerTest {

  private val httpClient = TcpRawHttpClient()
  private val http = RawHttp()

  @Test
  fun `responds to request with html document of the request headers`() {
    val port = 1234
    val server = ApplicationServer(port).initialize()
    val request = http.parseRequest("GET http://localhost:$port")
    val response = httpClient.send(request).eagerly()
    val responseBody = response.body.get().toString()
    assertEquals("<!DOCTYPE html><html><pre>${request.headers}</pre></html>", responseBody)
    server.stop()
  }

}