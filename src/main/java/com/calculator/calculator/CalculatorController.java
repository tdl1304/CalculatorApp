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
    @PostMapping("/calculate")
    public double calculate(@RequestBody Regnestykke regnestykke) {
        logger.info("calculating "+ regnestykke.getRegnestykke());
        return calculate(regnestykke.getRegnestykke());
    }

    // calculates + and - from a string only two numbers at the same time
    static double calculate(String s) {
        try {
            List<String> operators = Arrays.asList(s.trim().split("[0-9]"));
            List<String> operands = Arrays.asList(s.trim().split("[+/*-]"));
            operators = operators.stream().filter(e -> !(e.equals("") || e.equals("."))).collect(Collectors.toList());
            double agregate = 0;
            if(operators.get(0).equals("-") && operands.get(0).equals("")) {
                operators.remove(0);
                operands = operands.stream().filter(e -> !e.equals("")).collect(Collectors.toList());
                agregate = Double.parseDouble("-"+operands.get(0));
            } else {
                agregate = Double.parseDouble(operands.get(0));
            }
            if(operands.size() > 2) throw new ResponseStatusException(HttpStatus.CONFLICT, "there should only be 2 numbers per operation");
            for (int i = 1; i < operands.size(); i++) {
                switch (operators.get(i-1)) {
                    case "+":
                        agregate += Double.parseDouble(operands.get(i));
                        break;
                    case "-":
                        agregate -= Double.parseDouble(operands.get(i));
                        break;
                    case "*":
                        agregate *= Double.parseDouble(operands.get(i));
                        break;
                    default:
                        agregate /= Double.parseDouble(operands.get(i));
                        break;
                }
            }
            return agregate;
        } catch (NumberFormatException ne) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
