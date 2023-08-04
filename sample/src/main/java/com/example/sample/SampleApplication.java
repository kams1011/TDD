package com.example.sample;

import java.util.Scanner;

public class SampleApplication {

    public static void main(String[] args){
        CalculationRequest request = new CalculationRequestReader().read();
        long answer = new Calculator().calculate(
                request.getNum1(),
                request.getOperator(),
                request.getNum2());
        System.out.println(answer);

    }
}
