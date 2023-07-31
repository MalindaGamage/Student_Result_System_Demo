package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Tuple;
import model.Result;
import model.Student;
import util.Constant;

import java.util.logging.Logger;
public class DBVerticle extends AbstractVerticle {
    private JDBCPool jdbcPool;
    private final Logger log = Logger.getLogger(DBVerticle.class.getName());
    @Override
    public void start(Promise<Void> startPromise) {
        // set up the MySQL database connection options
        final JsonObject config = new JsonObject()
                .put("url", "jdbc:mysql://localhost:3306/student_result_system")
                .put("datasourceName", "pool-students")
                .put("driver_class", "com.mysql.cj.jdbc.Driver")
                .put("user", "Malinda")
                .put("password", "Malinda@1997")
                .put("max_pool_size", 16);

        jdbcPool = JDBCPool.pool(vertx, config);

        vertx.eventBus().consumer(Constant.STUDENT_SAVE_EVENT_BUS, message -> {
            Student student = (Student) message.body();
            saveStudent(student)
                    .onSuccess(success -> {
                        message.reply(success);
                    }).onFailure(throwable -> {
                        log.severe(throwable.getMessage());
                        message.fail(500, throwable.getMessage());
                    });
        });

        vertx.eventBus().consumer(Constant.STUDENT_RESULT_SAVE_EVENT_BUS, message ->{
            Result result = (Result) message.body();
            saveResult(result)
                    .onSuccess(success -> {
                        message.reply(success);
                    }).onFailure(throwable -> {
                        log.severe(throwable.getMessage());
                        message.fail(500, throwable.getMessage());
                    });
        });

    }
    public Future<String> saveStudent(Student student) {
        Promise<String> promise = Promise.promise();

        Tuple tuple = Tuple.of(student.getStudentID(), student.getName(), student.getAge());

        // Save to the database
        jdbcPool.preparedQuery(Constant.INSERT_STUDENT_DATA)
                .execute(tuple)
                .onFailure(e ->{
                    promise.fail(e);
                })
                .onSuccess(rows ->{
                    promise.complete("success");
                });
        return promise.future();
    }
    public Future<String> saveResult(Result result){
        Promise<String> promise = Promise.promise();

        Tuple tuple = Tuple.of(result.getStudentID(), result.getSubjectID(), result.getScore());

        // Save to the database
        jdbcPool.preparedQuery(Constant.INSERT_RESULT_DATA)
                .execute(tuple)
                .onFailure(e ->{
                    promise.fail(e);
                })
                .onSuccess(rows ->{
                    promise.complete("Result saved successfully");
                });
        return promise.future();
    }
}
