package com.studentapp.studentinfo;

import com.studentapp.testbase.TestBase;
import com.studentapp.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class StudentCRUDTestRevised extends TestBase {

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

    @Steps
    StudentSteps studentSteps;

    @Title ("Create Student Record")
    @Test
    public void test001(){
        List<String> courseList = new ArrayList<>();
        courseList.add("API Testing");
        courseList.add("Selenium");

        ValidatableResponse response = studentSteps.createStudent(firstName,lastName,email,programme,courseList);
        response.log().all().statusCode(201);

    }


    @Title ("Verfiy that new student record has been added to the application")
    @Test
    public void test002(){

        HashMap<String,Object> studentRecord = studentSteps.getStudentInfoByName(firstName);
        Assert.assertThat(studentRecord,hasValue(firstName));
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

        ValidatableResponse response = studentSteps.changeStudentInformation(studentID,firstName,lastName,email,programme,courseList);
        response.log().all().statusCode(200);

        HashMap<String,Object> studentRecord = studentSteps.getStudentInfoByName(firstName);

        Assert.assertThat(studentRecord,hasValue(firstName));

    }

    @Title("Delete student record and verify that record has been deleted")
    @Test
    public void test004(){

        studentSteps.deleteStudent(studentID).log().all().statusCode(204);
        studentSteps.getStudentByID(studentID).log().all().statusCode(404);


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
