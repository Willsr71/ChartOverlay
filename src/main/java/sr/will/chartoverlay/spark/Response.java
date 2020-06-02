package sr.will.chartoverlay.spark;

public class Response {
    public Status status = Status.SUCCESS;

    public Response(Status status) {
        this.status = status;
    }

    public Response() {

    }
}
