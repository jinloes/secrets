package com.jinloes.secrets;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by jinloes on 4/17/15.
 */
public class TestStepDefs {

    @Given("Test")
    public void test() {
        System.out.println("Test");
    }

    @When("That")
    public void that() {
        System.out.println("That");
    }

    @Then("something")
    public void something() {
        System.out.println("something");
    }

}
