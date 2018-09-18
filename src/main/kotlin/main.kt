import server.ApplicationServer



fun main(args: Array<String>) {
  val port = args.firstOrNull()?.toInt()
    ?: throw IllegalArgumentException("no port number provided")
  try {
    println("starting HTTP server on port $port")
    ApplicationServer(port).initialize()
    println("HTTP server started successfully")
  } catch (ex: Exception) {
    println("HTTP server startup on port $port failed")
  }
}