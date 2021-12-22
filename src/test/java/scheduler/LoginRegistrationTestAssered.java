package scheduler;

import SchedulerDto.AuthRequestDto;
import SchedulerDto.AuthResponseDto;
import com.jayway.restassured.RestAssured;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.given;

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
        AuthResponseDto responseDto = given()
                .body(auth)
                .contentType("application/json")
                .when()
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());
        System.out.println();
    }
}
