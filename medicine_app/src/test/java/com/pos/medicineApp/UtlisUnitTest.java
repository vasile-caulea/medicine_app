package com.pos.medicineApp;

import com.pos.medicineApp.utils.Utils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class UtlisUnitTest {

    @Test
    void getDate() throws ParseException {
        System.out.println(Utils.calculateBirthDateFromCNP("1231206890123"));
    }
}
