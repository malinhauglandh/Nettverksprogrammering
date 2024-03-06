package org.ntnu.dockerservice.controller;

import org.ntnu.dockerservice.model.UserCode;
import org.ntnu.dockerservice.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class DockerController {

    @Autowired
    CompilerService compiler;

    @GetMapping("/")
    public String test() {
        return "Hello World";
    }
    @PostMapping("/compile-and-run")
    @ResponseBody
    public UserCode compileAndRun(@RequestBody UserCode code) throws IOException, InterruptedException {
        return this.compiler.compileAndRun(code);
    }
}
