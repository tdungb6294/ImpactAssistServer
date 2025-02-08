package com.bdserver.impactassist.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FutureWithinValidator implements ConstraintValidator<FutureWithin, LocalDate> {
    private long maxDays;

    @Override
    public void initialize(FutureWithin constraintAnnotation) {
        this.maxDays = constraintAnnotation.days();
    }

    @Override
    public boolean isValid(LocalDate o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) return true;
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), o);
        return daysBetween > 0 && daysBetween < maxDays;
    }
}
