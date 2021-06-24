package ua.edu.ukma.fido.models;

import lombok.Data;

@Data
public class User {
    // generate somewhere in database
    private final Integer id = 0;
    private final String firstName;
    private final String lastName;
    private final String inst;
}
