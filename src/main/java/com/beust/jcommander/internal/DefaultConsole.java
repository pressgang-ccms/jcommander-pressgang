package com.beust.jcommander.internal;

import java.io.IOException;
import java.util.Arrays;

import com.beust.jcommander.ParameterException;

public class DefaultConsole implements Console {

    public void print(String msg) {
        System.out.print(msg);
    }

    public void println(String msg) {
        System.out.println(msg);
    }

    public char[] readPassword() {
        try {
            char[] buf;
            char[] tempBuf;
            buf = tempBuf = new char[128];

            char c;
            int offset = 0;
            int bufferSpace = buf.length;
            while ((c = (char) System.in.read()) != '\n') {
                if (--bufferSpace < 0) {
                    // Copy the data to the temporary buffer
                    tempBuf = buf;
                    // Increase the size of the buffer
                    buf = new char[offset + 128];
                    bufferSpace = buf.length - offset - 1;
                    // Copy the temp buffer back into the normal buffer
                    System.arraycopy(tempBuf, 0, buf, 0, offset);
                    // Clean out the temporary buffer
                    Arrays.fill(tempBuf, ' ');
                }
                buf[offset++] = c;
            }

            char[] retValue = new char[offset];
            System.arraycopy(buf, 0, retValue, 0, offset);
            return retValue;
        } catch (IOException e) {
            throw new ParameterException(e);
        }
    }

    public String readLine() {
        try {
            char c;
            final StringBuilder line = new StringBuilder();
            while ((c = (char) System.in.read()) != '\n') {
                line.append(c);
            }
            return line.toString();
        } catch (IOException e) {
            throw new ParameterException(e);
        }
    }

}
