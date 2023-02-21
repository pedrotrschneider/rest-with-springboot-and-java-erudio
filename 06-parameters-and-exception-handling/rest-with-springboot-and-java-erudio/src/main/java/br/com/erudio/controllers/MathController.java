package br.com.erudio.controllers;

import br.com.erudio.services.MathService;
import br.com.erudio.MathUtil;
import br.com.erudio.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    private static final String template = "Hello, %s";
    static final AtomicLong counter = new AtomicLong();
    final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws UnsupportedMathOperationException {
        if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please enter a numeric value.");
        }
        return mathService.sum(MathUtil.convertToDouble(numberOne), MathUtil.convertToDouble(numberTwo));
    }

    @GetMapping("/subtract/{numberOne}/{numberTwo}")
    public Double subtract(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws UnsupportedMathOperationException {
        if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please enter a numeric value.");
        }
        return mathService.subtract(MathUtil.convertToDouble(numberOne), MathUtil.convertToDouble(numberTwo));
    }

    @GetMapping("/multiply/{numberOne}/{numberTwo}")
    public Double multiply(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws UnsupportedMathOperationException {
        if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please enter a numeric value.");
        }
        return mathService.multiply(MathUtil.convertToDouble(numberOne), MathUtil.convertToDouble(numberTwo));
    }

    @GetMapping("/divide/{numberOne}/{numberTwo}")
    public Double divide(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws UnsupportedMathOperationException {
        if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please enter a numeric value.");
        }
        return mathService.divide(MathUtil.convertToDouble(numberOne), MathUtil.convertToDouble(numberTwo));
    }

    @GetMapping("/mean/{numberOne}/{numberTwo}")
    public Double mean(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws UnsupportedMathOperationException {
        if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please enter a numeric value.");
        }
        return mathService.mean(MathUtil.convertToDouble(numberOne), MathUtil.convertToDouble(numberTwo));
    }

    @GetMapping("/sqrt/{number}")
    public Double subtract(
            @PathVariable(value = "number") String number
    ) throws UnsupportedMathOperationException {
        if (!MathUtil.isNumeric(number)) {
            throw new UnsupportedMathOperationException("Please enter a numeric value.");
        }
        return mathService.sqrt(MathUtil.convertToDouble(number));
    }
}
