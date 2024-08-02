package com.likelion.timer.global.validation;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxListSizeValidator implements ConstraintValidator<MaxListSize, List<?>> {
	private int maxSize;

	@Override
	public void initialize(MaxListSize constraintAnnotation) {
		this.maxSize = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(List<?> value, ConstraintValidatorContext context) {
		if (value != null && value.size() > maxSize) {
			context.buildConstraintViolationWithTemplate(
					context.getDefaultConstraintMessageTemplate()
						.replace("${max}", String.valueOf(maxSize)))
				.addConstraintViolation();
			return false;
		}
		return true;
	}
}