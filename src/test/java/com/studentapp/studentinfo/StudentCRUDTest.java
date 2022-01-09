package com.studentapp.studentinfo;

import com.studentapp.constants.EndPoints;
import com.studentapp.model.StudentPojo;
import com.studentapp.testbase.TestBase;
import com.studentapp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

//@RunWith(SerenityRunner.class)
public class StudentCRUDTest extends TestBase {

    /*{
        "id": 1,
            "firstName": "Vernon",
            "lastName": "Harper",
            "email": "egestas.rhoncus.Proin@massaQuisqueporttitor.org",
            "programme": "Financial Analysis",
            "courses": [
        "Accounting",
                "Statistics"
    ]
    }*/

    public static String firstName = "Jim"+ TestUtils.getRandomValue();
    public static String lastName = "Pat"+TestUtils.getRandomValue();
    public static String email = TestUtils.getRandomValue()+"abc@gmail.com";
    public static String programme = "Automation Testing";
    public static int studentID;


    @Title ("Create Student Record")
    @Test
    public void test001(){
        List<String> courseList = new ArrayList<>();
        courseList.add("API Testing");
        courseList.add("Selenium");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courseList);

        SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(studentPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);
    }


    @Title ("Verfiy that new student record has been added to the application")
    @Test
    public void test002(){

        HashMap<String,Object> studentRecord = SerenityRest.given()
                .log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().log().all().statusCode(200)
                .extract()
                .path("findAll{it.name ='"+firstName+"'}.get(0)");

        studentID = (int) studentRecord.get("id");

    }

    @Title ("Change student record and verfiy it change has been reflected")
    @Test
    public void test003(){

        firstName = firstName+TestUtils.getRandomValue();
        email = TestUtils.getRandomValue()+email;

        List<String> courseList = new ArrayList<>();
        courseList.add("API Testing");
        courseList.add("Selenium");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courseList);

        SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("studentID",studentID)
                .body(studentPojo)
                .when()
                .put(EndPoints.CHANGE_STUDENT_RECORD)
                .then().log().all().statusCode(200);

        HashMap<String,Object> studentRecord = SerenityRest.given()
                .log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().log().all().statusCode(200)
                .extract()
                .path("findAll{it.name ='"+firstName+"'}.get(0)");

        Assert.assertThat(studentRecord,hasValue(firstName));

    }

    @Title("Delete student record and verify that record has been deleted")
    @Test
    public void test004(){

        SerenityRest.given()
                .log().all()
                .pathParam("studentID", studentID)
                .when()
                .delete(EndPoints.DELETE_STUDENT_RECORD)
                .then().log().all().statusCode(204);

        SerenityRest.given()
                .log().all()
                .pathParam("studentID",studentID)
                .when()
                .get(EndPoints.SINGLE_STUDENT_BY_ID)
                .then()
                .log().all().statusCode(404);

    }


   /* @Title("Get All Students List")
    @Test
    public void getAllStudents(){
        SerenityRest.given()
                .log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().log().all().statusCode(200);
    }*/
}
