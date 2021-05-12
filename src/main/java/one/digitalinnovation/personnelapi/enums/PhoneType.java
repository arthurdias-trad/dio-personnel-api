package one.digitalinnovation.personnelapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PhoneType {

    HOME(0,"Home"), MOBILE(1,"Mobile"), BUSINESS(2,"Business");

    private final int code;
    private final String description;
}
