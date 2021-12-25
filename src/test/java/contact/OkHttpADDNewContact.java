
package contact;

import com.google.gson.Gson;
import dto.ContactDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class OkHttpADDNewContact {

    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5vYUBnbWFpbC5jb20ifQ.G_wfK7FRQLRTPu9bs2iDi2fcs69FHmW-0dTY4v8o5Eo";
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    @Test
    public void addNewContactTest() throws IOException {
        int index= (int) (System.currentTimeMillis()/1000)%3600;

        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("may"+index+"@gmail.com")
                .phone("3333"+index)
                .address("Haifa")
                .description("friend").build();

        RequestBody requestBody = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact")
                .addHeader("Authorization",token)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();

        ContactDto contactDtoResponse = gson.fromJson(response.body().string(),ContactDto.class);
        Assert.assertTrue(response.isSuccessful());
        System.out.println(contactDtoResponse.getId());
        Assert.assertEquals(contactDtoResponse.getName(),contactDto.getName());
        Assert.assertEquals(contactDtoResponse.getLastName(),contactDto.getLastName());
        Assert.assertEquals(contactDtoResponse.getEmail(),contactDto.getEmail());
        Assert.assertEquals(contactDtoResponse.getPhone(),contactDto.getPhone());

    }
}