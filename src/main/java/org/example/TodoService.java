package org.example;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TodoService {
    String PATH = "/todos";

    @GET(PATH)
    Call<List<Todo>> getTodos();

    @GET(PATH)
    Call<List<Todo>> getTodosOffset(@Query("offset") Integer offset);

    @GET(PATH)
    Call<List<Todo>> getTodosLimit(@Query("limit") Integer limit);

    @GET(PATH)
    Call<List<Todo>> getTodosOffsetAndLimit(@Query("offset") Integer offset,
                                            @Query("limit") Integer limit);

    @POST(PATH)
    Call<Void> postTodo(@Body Todo todo);

    @PUT(PATH + "/{id}")
    Call<Void> putTodo(@Path("id") Long id,
                       @Body Todo todo);

    @DELETE(PATH + "/{id}")
    Call<Void> deleteTodo(@Path("id") Long id,
                          @Header("Authorization") String auth);
}
