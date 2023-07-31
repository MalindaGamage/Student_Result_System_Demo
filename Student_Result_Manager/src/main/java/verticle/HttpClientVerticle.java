package verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import model.Result;
import util.Constant;

public class HttpClientVerticle extends AbstractVerticle {
    private WebClient client;
    @Override
    public void start(Promise<Void> promise) {
        client = WebClient.create(vertx);

        vertx.eventBus().consumer(Constant.STUDENT_RESULT_FETCH_EVENT_BUS, request -> {
            JsonObject bodyJson = (JsonObject) request.body();
            int studentID = bodyJson.getInteger("studentID");

            // Fetch student result from third-party API
            client.getAbs("https://api.mockfly.dev/mocks/015d217e-d0fe-4aa8-a578-04a03e46d7a6/result")
                    .send()
                    .onSuccess(res -> {
                        JsonObject resultJson = res.bodyAsJsonObject();

                        // Calculate pass/fail based on the score
                        int score = Integer.parseInt(resultJson.getString("score"));
                        String passFailStatus = (score >= Constant.SUBJECT_MARKS_THRESHOLD) ? "Pass" : "Fail";
                        resultJson.put("passFailStatus", passFailStatus);
                        resultJson.put("studentId", studentID);
                        Result result = new Result(res.bodyAsJsonObject().put("studentId", studentID));

                        // Save the result to the database
                        vertx.eventBus().request(Constant.STUDENT_RESULT_SAVE_EVENT_BUS, result, ar -> {
                            if (ar.succeeded()) {
                                request.reply(resultJson);
                            } else {
                                request.fail(500, ar.cause().getMessage());
                            }
                        });
                    })
                    .onFailure(error -> request.fail(HttpResponseStatus.BAD_GATEWAY.code(), error.getMessage()));
        });


            /*client
                    .getAbs("http://third-party-api.com/getStudentResult?studentID=").send()
                    .onSuccess(res -> {
                        request.reply(res.body());
                    })
                    .onFailure(error -> request.fail(HttpResponseStatus.BAD_GATEWAY.code(), error.getMessage()));
        });*/
    }
}
