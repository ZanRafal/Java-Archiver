package main;

import comand.ExitCommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Archiver {
    public static void main(String[] args) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter full file path:");
        String path = reader.readLine();
        ZipFileManager zipFileManager = new ZipFileManager(Paths.get(path));

        System.out.println("Enter full file name for archiving:");
        String fileName = reader.readLine();
        zipFileManager.createZip(Paths.get(fileName));

        ExitCommand exitCommand = new ExitCommand();
        exitCommand.execute();
    }
}
