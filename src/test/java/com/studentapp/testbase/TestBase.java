package com.studentapp.testbase;

import com.studentapp.constants.Path;
import com.studentapp.utils.PropertyReader;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class TestBase {

    public static PropertyReader propertyReader;

    @BeforeClass
    public static void inIt(){


        RestAssured.baseURI = PropertyReader.getInstance().getProperty("baseUrl");
        RestAssured.port = Integer.parseInt(PropertyReader.getInstance().getProperty("port"));
        RestAssured.basePath = Path.STUDENT;

    }

}
