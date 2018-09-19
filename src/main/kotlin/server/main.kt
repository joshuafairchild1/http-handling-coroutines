package server

import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) = runBlocking {
  val port = args.firstOrNull()?.toInt()
    ?: throw IllegalArgumentException("no port number provided")
  val handleRequestsAsync = args.getOrNull(1).let {
    it?.toBoolean()
  } ?: true
  try {
    println("starting HTTP server on port $port with " +
      "${if (handleRequestsAsync) "async" else "synchronous"} request handling")
    ApplicationServer(port).initialize(handleRequestsAsync)
    println("HTTP server started successfully")
  } catch (ex: Exception) {
    println("HTTP server startup on port $port failed")
  }
}