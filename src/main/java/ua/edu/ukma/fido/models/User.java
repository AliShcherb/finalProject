package ua.edu.ukma.fido.models;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class User {
    Integer id = 0;
    String firstName;
    String lastName;
    String inst;
}
