package com.pos.medicineApp.utils.validators;

import com.pos.medicineApp.utils.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// https://blog.clairvoyantsoft.com/spring-boot-creating-a-custom-annotation-for-validation-edafbf9a97a4
public class CnpValidator implements ConstraintValidator<CnpValidation, String> {

    @Override
    public void initialize(CnpValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @SuppressWarnings("CommentedOutCode")
    @Override
    public boolean isValid(String cnp, ConstraintValidatorContext constraintValidatorContext) {
        int S = Integer.parseInt(cnp.substring(0, 1));
        if (!(S >= 1 && S <= 8))
            return false;

        // no need to check the year -> values between (0-99)
        // int YY = Integer.parseInt(cnp.substring(1, 3));

        int MM = Integer.parseInt(cnp.substring(3, 5));
        if (!(MM >= 1 && MM <= 12))
            return false;

        int DD = Integer.parseInt(cnp.substring(5, 7));
        if (!(DD >= 1 && DD <= 31))
            return false;

        try {
            Utils.calculateBirthDateFromCNP(cnp);
        } catch (Exception ex) {
            return false;
        }

        /*  int JJ = Integer.parseInt(cnp.substring(7, 9));
            int NNN = Integer.parseInt(cnp.substring(9, 12));
            int C = Integer.parseInt(cnp.substring(12));
        */
        return true;
    }
}
