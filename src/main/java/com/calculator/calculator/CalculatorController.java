package com.calculator.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
public class CalculatorController {
    static Logger logger = LoggerFactory.getLogger(CalculatorController.class.getName());

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/calculate")
    public double calculate(@RequestBody Regnestykke regnestykke) {
        logger.info("calculating "+ regnestykke.getRegnestykke());
        return calculate(regnestykke.getRegnestykke());
    }

    // calculates + and - from a string only two numbers at the same time
    static double calculate(String s) {
        try {

            List<String> operators = Arrays.asList(s.trim().split("[0-9]"));
            String[] operands = s.trim().split("[+/*-]");
            if(operands.length > 2) throw new ResponseStatusException(HttpStatus.CONFLICT, "there should only be 2 numbers per operation");
            operators = operators.stream().filter(e -> !e.equals("")).collect(Collectors.toList());
            logger.info(Arrays.toString(operands));
            logger.info(String.valueOf(operators));
            double agregate = Integer.parseInt(operands[0]);
            for (int i = 1; i < operands.length; i++) {
                switch (operators.get(i-1)) {
                    case "+":
                        agregate += Integer.parseInt(operands[i]);
                        break;
                    case "-":
                        agregate -= Integer.parseInt(operands[i]);
                        break;
                    case "*":
                        agregate *= Integer.parseInt(operands[i]);
                        break;
                    default:
                        agregate /= Double.parseDouble(operands[i]);
                        break;
                }
            }
            return agregate;
        } catch (NumberFormatException ne) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
