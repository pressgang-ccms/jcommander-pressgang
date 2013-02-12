package com.beust.jcommander.internal;

import java.io.IOException;
import java.io.InputStream;
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
            final InputStream is = System.in;
            char[] buf;
            char[] tempBuf;
            buf = tempBuf = new char[128];

            int c;
            int offset = 0;
            int bufferSpace = buf.length;

            // Wait for some data to become available
            waitForInputStreamData(is);

            while ((c = is.read()) != '\n') {
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
                buf[offset++] = (char) c;

                // No new line found yet so check and wait for the stream to have more data
                waitForInputStreamData(is);
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
            final InputStream is = System.in;
            int c;
            final StringBuilder line = new StringBuilder();

            // Wait for some data to become available
            waitForInputStreamData(is);

            while ((c = is.read()) != '\n') {
                line.append((char) c);

                // No new line found yet so check and wait for the stream to have more data
                waitForInputStreamData(is);
            }
            return line.toString();
        } catch (IOException e) {
            throw new ParameterException(e);
        }
    }

    private void waitForInputStreamData(InputStream is) throws IOException {
        // Wait for some input if there is none available
        while (is.available() <= 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
