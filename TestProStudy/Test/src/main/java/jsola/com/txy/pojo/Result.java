package jsola.com.txy.pojo;


import lombok.Data;


@Data
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public Result(String message){
        this.message = message;
    }


    public Result(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }
    public static Result success(){
        return new Result(200,null);
    }

    public static Result success(Object data){
        return new Result(200,null,data);
    }

    public static Result success(Integer code,String message) {
        return new Result(code,message);
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }
    public static Result error(String msg) {
        return new Result( msg);
    }
}
