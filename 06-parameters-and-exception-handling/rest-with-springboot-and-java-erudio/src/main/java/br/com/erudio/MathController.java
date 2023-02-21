package br.com.erudio;

import jakarta.security.auth.message.callback.PrivateKeyCallback;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    private static final String template = "Hello, %s";
    static final AtomicLong counter = new AtomicLong();

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws Exception{
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new Exception();
        }
        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    private boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        strNumber = strNumber.replaceAll(",", ".");
        return strNumber.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    private Double convertToDouble(String strNumber) {
        if (strNumber == null) return 0D;
        strNumber = strNumber.replaceAll(",", ".");
        if (isNumeric(strNumber)) return Double.parseDouble(strNumber);
        return 1D;
    }
}
