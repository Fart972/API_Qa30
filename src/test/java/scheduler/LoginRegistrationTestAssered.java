package scheduler;

import SchedulerDto.AuthRequestDto;
import SchedulerDto.AuthResponseDto;
import SchedulerDto.ErrorDto;
import com.jayway.restassured.RestAssured;


import com.jayway.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginRegistrationTestAssered {

    @BeforeMethod
    public void precondition(){
        RestAssured.baseURI = "https://super-scheduler-app.herokuapp.com/";
        RestAssured.basePath = "api";
    }

    @Test
    public void loginSuccess(){
        AuthRequestDto auth = AuthRequestDto.builder()
                .email("noa@gmail.com")
                .password("Nnoa12345$")
                .build();

        AuthResponseDto responseDto = given().body(auth)
                .contentType("application/json")
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);

        System.out.println( responseDto.getToken());
        System.out.println(responseDto.getStatus());
        System.out.println(responseDto.isRegistration());

    }

    @Test
    public void wrongPasswordTest(){
        AuthRequestDto auth = AuthRequestDto.builder()
                .email("noa@gmail.com")
                .password("Nnoa12345")
                .build();

        ErrorDto errorDto = given().body(auth).contentType("application/json")
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(401)
                .extract().response().as(ErrorDto.class);
        System.out.println( errorDto.toString());
        Assert.assertEquals(errorDto.getMessage(),"Wrong email or password");

    }

    @Test
    public void wrongPasswordTest2(){
        AuthRequestDto auth = AuthRequestDto.builder()
                .email("noa@gmail.com")
                .password("Nnoa12345")
                .build();

        String message = given().body(auth).contentType("application/json")
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(401)
                .extract().path("message");

        Assert.assertEquals(message,"Wrong email or password");

    }

    @Test
    public void registrationTest(){
        int index = (int)(System.currentTimeMillis()/1000)%3600;
        AuthRequestDto auth = AuthRequestDto.builder()
                .email("soll"+index+"@mail.com")
                .password("Dd12345~")
                .build();
        String token = given().contentType(ContentType.JSON).body(auth)
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("status",containsString("Registration success"))
                .assertThat().body("registration", equalTo(true))
                .extract().path("token");

        System.out.println( token);

    }

    @Test
    public void logTest(){
        int index = (int)(System.currentTimeMillis()/1000)%3600;
        AuthRequestDto auth = AuthRequestDto.builder()
                .email("noa@gmail.com")
                .password("Nnoa12345$")
                .build();
        String token = given().contentType(ContentType.JSON).body(auth)
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("status",containsString("Login success"))
                .assertThat().body("registration", equalTo(false))
                .extract().path("token");

        System.out.println( token);
}

}
