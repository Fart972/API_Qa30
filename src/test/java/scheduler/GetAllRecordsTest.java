package scheduler;

import SchedulerDto.GetAllRecordsDto;
import SchedulerDto.GetRecordRequestDto;
import SchedulerDto.RecordDto;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

public class GetAllRecordsTest {
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5vYUBnbWFpbC5jb20ifQ.G_wfK7FRQLRTPu9bs2iDi2fcs69FHmW-0dTY4v8o5Eo";

    @BeforeMethod
    public void precondition() {
        RestAssured.baseURI = "https://super-scheduler-app.herokuapp.com/";
        RestAssured.basePath = "api";
    }

    @Test
    public void getAllRecords() {
        GetRecordRequestDto requestDto = GetRecordRequestDto.builder()
                .monthFrom(5)
                .monthTo(12)
                .yearFrom(2021)
                .yearTo(2022)
                .build();
        GetAllRecordsDto records = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("records")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(GetAllRecordsDto.class);
        for (RecordDto record : records.getRecords()) {
            System.out.println(record.getId());
            System.out.println("*******");
        }

    }
}


