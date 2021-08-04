package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
    }

    public static int readInt() throws IOException {
        String text = readString();
        return Integer.parseInt(text);
    }
}
