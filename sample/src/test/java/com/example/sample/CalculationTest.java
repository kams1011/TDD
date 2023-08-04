package com.example.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculationTest {

    @Test
    public void 덧셈_연산을_할_수_있다(){
        //given
        long num1 = 2;
        String operator = "+";
        long num2 = 3;

        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertEquals(5, result);

    }

    @Test
    public void 뺄셈_연산을_할_수_있다(){
        //given
        long num1 = 2;
        String operator = "-";
        long num2 = 3;

        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertEquals(-1, result);

    }

    @Test
    public void 곱셈_연산을_할_수_있다(){
        //given
        long num1 = 2;
        String operator = "*";
        long num2 = 3;

        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertEquals(6, result);

    }

    @Test
    public void 나눗셈_연산을_할_수_있다(){
        //given
        long num1 = 2;
        String operator = "/";
        long num2 = 3;

        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertEquals(0, result);

    }

    @Test
    public void 잘못된_연산자가_들어오면_에러가_난다(){
        //given
        long num1 = 2;
        String operator = "x";
        long num2 = 3;

        Calculator calculator = new Calculator();

        //when

        //then
        assertThrows(InvalidOperatorException.class, () -> {
            calculator.calculate(num1, operator, num2);
        });

    }
}
