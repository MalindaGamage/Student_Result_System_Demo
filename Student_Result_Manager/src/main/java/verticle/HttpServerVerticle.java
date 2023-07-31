package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import model.Student;
import util.Constant;

public class HttpServerVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void>startPromise){
        // Create a router to handle HTTP requests
        Router router =Router.router(vertx);
        router.route().handler(BodyHandler.create()); // Enable parsing of request bodies

        // Define the route to handle saving student details (POST request)
        router.post("/students").handler(this::saveStudent);

        // Define the route to handle fetching student results (GET request)
        router.get("/students/:studentID/results").handler(this::getStudentResults);

        // Create an HTTP server and pass the router to handle incoming requests
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081)
                .onSuccess(server -> {
                    System.out.println("Server started on port 8081 successfully");
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);

    }
    private void saveStudent(RoutingContext context) {
        JsonObject studentJson = context.getBodyAsJson();
        Student student = new Student(studentJson); // Map JsonObject to Student

        // Send student data to the EventBus for processing in DBVerticle
        vertx.eventBus().request(Constant.STUDENT_SAVE_EVENT_BUS,student, ar -> {
            if(ar.succeeded()){
                context.response().setStatusCode(200).end(ar.result().body().toString());
            }else{
                context.response().setStatusCode(500).end(ar.cause().getMessage());
            }
        });
    }
    private void getStudentResults(RoutingContext context){
        int studentID = Integer.parseInt(context.pathParam("studentID"));

        vertx.eventBus().request(Constant.STUDENT_RESULT_FETCH_EVENT_BUS, new JsonObject()
                .put("studentID", studentID), ar ->{
            if (ar.succeeded()) {
                JsonObject resultJson = (JsonObject) ar.result().body();
                int score = Integer.parseInt(resultJson.getString("score"));

                // Calculate pass/fail based on the score
                String passFailStatus = (score >= Constant.SUBJECT_MARKS_THRESHOLD) ? "Pass":"Fail";
                resultJson.put("passFailStatus", passFailStatus);

                context.response().setStatusCode(200).end(resultJson.encodePrettily());
            }else {
                context.response().setStatusCode(500).end(ar.cause().getMessage());
            }
        });
    }
}
