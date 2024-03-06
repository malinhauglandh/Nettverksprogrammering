package org.ntnu.dockerservice.service;

import org.ntnu.dockerservice.model.UserCode;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CompilerService {

    public UserCode compileAndRun(UserCode inputCode) throws IOException, InterruptedException {
        String escapedCode = inputCode.getCode().replace("\"", "\\\"");

        String dockerCommand = String.format("docker run --rm python:latest python -c \"%s\"", escapedCode);
        String[] command = {"cmd", "/c", dockerCommand};

        Process process = Runtime.getRuntime().exec(command);
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String codeLine;

        while ((codeLine = reader.readLine()) != null) {
            output.append(codeLine).append("\n");
        }
        while ((codeLine = errorReader.readLine()) != null) {
            output.append(codeLine).append("\n");
        }
        int exitVal = process.waitFor();
        if (exitVal != 0) {
            inputCode.setOutput("Error: " + output.toString());
        } else {
            inputCode.setOutput(output.toString());
        }
        return inputCode;
    }
}
