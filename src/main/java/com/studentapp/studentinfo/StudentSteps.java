package com.studentapp.studentinfo;

import com.studentapp.constants.EndPoints;
import com.studentapp.model.StudentPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;
import java.util.List;

public class StudentSteps {

    @Step ("Creatiing student with firstName : {0}, lastName : {1}, email : {2}, programme : {3} and courses : {4}")
    public ValidatableResponse createStudent (String firstName, String lastName, String email, String programme, List<String> courses){

        StudentPojo studentPojo = StudentPojo.getStudentPojo(firstName, lastName, email, programme, courses);

         return SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(studentPojo)
                .when()
                .post()
                .then();


    }

    @Step ("Get student information with firstName : {0}")
    public HashMap<String, Object> getStudentInfoByName(String firstName){
        return SerenityRest.given()
                .log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().log().all().statusCode(200)
                .extract()
                .path("findAll{it.name ='"+firstName+"'}.get(0)");
    }

    @Step ("Change student information with studentID : {0}, firstName : {1}, lastName : {2}, email : {3}, programme : {4} and courses : {5}")
    public ValidatableResponse changeStudentInformation(int studentID, String firstName, String lastName, String email, String programme, List<String> courses){

        StudentPojo studentPojo = StudentPojo.getStudentPojo(firstName, lastName, email, programme, courses);

       return SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("studentID",studentID)
                .body(studentPojo)
                .when()
                .put(EndPoints.CHANGE_STUDENT_RECORD)
                .then();

    }

    @Step ("Delete student record with studentID : {0}")
    public ValidatableResponse deleteStudent(int studentID){
        return  SerenityRest.given()
                .log().all()
                .pathParam("studentID", studentID)
                .when()
                .delete(EndPoints.DELETE_STUDENT_RECORD)
                .then();
    }

    @Step ("Get student record with studentID : {0}")
    public ValidatableResponse getStudentByID(int studentID){
        return SerenityRest.given()
                .log().all()
                .pathParam("studentID",studentID)
                .when()
                .get(EndPoints.SINGLE_STUDENT_BY_ID)
                .then();

    }


}
