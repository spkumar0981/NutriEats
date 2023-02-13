package com.nutri.rest.exception;

import lombok.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ErrorInfo {

    private String field;
    private Object invalidValue;
    private String message;

/*    public static ErrorInfo of(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        List<String> methodNames =
                ofNullable(violation.getRootBeanClass()).map(Class::getDeclaredMethods).stream()
                        .flatMap(Arrays::stream)
                        .map(Method::getName)
                        .collect(toList());
        if (!methodNames.isEmpty())
            path =
                    stream(path.split("\\."))
                            .filter(part -> !methodNames.contains(part))
                            .collect(joining("."));
        return of(path, violation.getInvalidValue(), violation.getMessage());
    }*/

    public static ErrorInfo of(ObjectError error) {
        if (error instanceof FieldError) {
            var fieldError = (FieldError) error;
            return of(
                    fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        }
        return of(null, null, error.getDefaultMessage());
    }
}
