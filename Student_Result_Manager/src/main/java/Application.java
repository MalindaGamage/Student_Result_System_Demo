import io.vertx.core.Vertx;
import model.Result;
import model.Student;
import util.LocalEventBusCodec; // for serialize and deserialize objects when they are sent over the Vert.x event bus
import verticle.DBVerticle;
import verticle.HttpClientVerticle;
import verticle.HttpServerVerticle;

public class Application {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        // Register the custom codec for Student class
        vertx.eventBus().registerDefaultCodec(Student.class, new LocalEventBusCodec<>(Student.class));
        vertx.eventBus().registerDefaultCodec(Result.class, new LocalEventBusCodec<>(Result.class));


        vertx.deployVerticle(new HttpServerVerticle());
        vertx.deployVerticle(new DBVerticle());
        vertx.deployVerticle(new HttpClientVerticle());
    }
}
