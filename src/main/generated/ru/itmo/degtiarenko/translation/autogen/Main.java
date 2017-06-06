package ru.itmo.degtiarenko.translation.autogen;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by Иван on 06.06.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        InputStream input = new FileInputStream(new File("src/main/resources/input.txt"));
        System.out.println(new OurParser().parse(input));
    }
}
