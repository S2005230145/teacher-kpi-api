package controllers;

import play.mvc.Result;

import static play.mvc.Results.ok;

public class TestController {

    public Result test(){
        return ok("complete");
    }
}
