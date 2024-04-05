package server.handler;


import com.google.gson.Gson;
import dataAccess.*;
import server.request.InvalidRequestException;
import server.result.Result;
import service.*;
import spark.Request;
import spark.Response;

public abstract class Handler {
    protected static MemoryDatabase db = new MemoryDatabase();
    protected static AuthDAO authDAO = new MemoryAuthDAO();
    protected static GameDAO gameDAO = new MemoryGameDAO();
    protected static UserDAO userDAO = new MemoryUserDAO();
    protected static DevService dev = new DevService(authDAO, gameDAO, userDAO);
    protected static AuthService auth = new AuthService(authDAO);
    protected static GameService games = new GameService(gameDAO, auth);
    protected static UserService users = new UserService(userDAO, auth);
    protected static Gson serializer = new Gson();

    protected static void setStatusAndBody(Response res, int status, String body) {
        res.status(status);
        res.body(body);
    }

    protected static String success(Response res, String jsonBody) {
        res.status(200);
        res.body(jsonBody);
        return jsonBody;
    }

    protected static String unauthorized(Response res, String message) {
        res.status(401);
        String body = serializer.toJson(new Result("Error: unauthorized" + message));
        res.body(body);
        return  body;
    }
    protected static String unauthorized(Response res) {
        res.status(401);
        String body = serializer.toJson(new Result("Error: unauthorized"));
        res.body(body);
        return  body;
    }

    protected static String badRequest(Response res) {
        res.status(400);
        String body = serializer.toJson(new Result("Error: badRequest"));
        res.body(body);
        return  body;
    }

    protected static String alreadyTaken(Response res) {
        res.status(403);
        String body = serializer.toJson(new Result("Error: already taken"));
        res.body(body);
        return  body;
    }
    protected static String failure(Response res, String message) {
        res.status(500);
        String body = serializer.toJson(new Result("Error: " + message));
        res.body(body);
        return  body;
    }

    protected static String safeHandleRequest(Request req, Response res)
            throws AlreadyTakenException, DataAccessException, UnauthorizedException, InvalidRequestException {
        return null;
    }

    public static String handleRequest(Request req, Response res) {
             try {
                 // in each method
                 return safeHandleRequest(req, res);

             } catch (UnauthorizedException e) {
                 return unauthorized(res);
             } catch (AlreadyTakenException e) {
                 return alreadyTaken(res);
             } catch (InvalidRequestException e) {
                 return badRequest(res);
             } catch (DataAccessException e) {
                 return unauthorized(res, e.getMessage());
             }catch (Exception e) {
                 return failure(res, e.getMessage());
             }
        }
    }
