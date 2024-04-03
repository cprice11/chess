package server;

import spark.*;

public class Server {
//    The Server receives network HTTP requests and sends them to the correct handler for processing. The server should
//    also handle all unhandled exceptions that your application generates and return the appropriate HTTP status code.
//
//    ⚠ For the pass off tests to work properly, your server class must be named Server and provide a run method that
//    has a desired port parameter, and a stop method that shuts your HTTP server down.
//
//    The starter code contains the Server class that you should use as the base for your HTTP server. For the pass off
//    tests to work properly, you must keep the Server class in a folder named server/src/main/java/server, and do not
//    remove the provided code.
//    The Server class provides a run method that attempts to start the HTTP server on a desired port parameter. From
//    your main function you should start the server on port 8080. The unit tests will start the server on port is 0.
//    This directs the Spark code to discover and use a random open port. The port that is actually used is returned by
//    the Spark.port method after initialization has completed. The starter code also provides a stop method that shuts
//    the HTTP server down. This is necessary to control the starting and stopping of your server when running tests.

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}