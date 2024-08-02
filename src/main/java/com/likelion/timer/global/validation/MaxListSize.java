package com.likelion.timer.global.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = MaxListSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxListSize {
	String message() default "리스트 사이즈가 ${max}보다 작아야 합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int max();
}