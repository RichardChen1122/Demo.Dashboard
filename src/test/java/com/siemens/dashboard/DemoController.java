package com.siemens.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {
    private Map<String,Object> param = new HashMap<>();

    @GetMapping(path="/api/hello")
    public Object hello(){
        param.clear();
        param.put("hello","world");
        return param;
    }
}
