package com.beust.jcommander.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.beust.jcommander.ParameterException;

public class DefaultConsole implements Console {

    private final InputStreamReader isr = new InputStreamReader(System.in);
    private final BufferedReader in = new BufferedReader(isr);

    public void print(String msg) {
        System.out.print(msg);
    }

    public void println(String msg) {
        System.out.println(msg);
    }

    public char[] readPassword() {
        try {
            String result = in.readLine();
            return result.toCharArray();
        } catch (IOException e) {
            throw new ParameterException(e);
        }
    }

    public String readLine() {
        try {
            String result = in.readLine();
            return result;
        } catch (IOException e) {
            throw new ParameterException(e);
        }
    }

}
