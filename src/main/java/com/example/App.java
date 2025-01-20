package com.example;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

import com.zaxxer.nuprocess.NuAbstractProcessHandler;
import com.zaxxer.nuprocess.NuProcess;
import com.zaxxer.nuprocess.NuProcessBuilder;

class ProcessHandler extends NuAbstractProcessHandler {
    @Override
    public void onStart(NuProcess nuProcess) {
        System.out.println("Process started: " + nuProcess.getPID());
    }

    @Override
    public void onStdout(ByteBuffer buffer, boolean closed) {
        if (buffer != null) {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            System.out.print(new String(bytes));
        }
    }

    @Override
    public void onStderr(ByteBuffer buffer, boolean closed) {
        if (buffer != null) {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            System.err.print(new String(bytes));
        }
    }

    @Override
    public void onExit(int exitCode) {
        System.out.println("\nProcess exited with code: " + exitCode);
    }
}

public class App {
    public static void main(String[] args) {
        // NuProcessBuilder pb = new NuProcessBuilder(Arrays.asList("sh", "-c", "echo
        // 'Hello'; ls -l"));
        // NuProcessBuilder pb = new NuProcessBuilder(Arrays.asList("bash", "-c",
        // "\0echo 'Hello';", "touch test.txt"));
        // NuProcessBuilder pb = new NuProcessBuilder( Arrays.asList("./script.sh", "1",
        // "2", "3\0\"/bin/clear\"", "echo XD"));
        String searchTermString = "";
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Search in the database: ");
            searchTermString = scanner.nextLine().replace("\\0", "\0"); // BAD SANITIZATION CASE OR SOMETHING
        }

        NuProcessBuilder pb = new NuProcessBuilder(
                Arrays.asList("/bin/grep", searchTermString, "database.csv"));
        ProcessHandler handler = new ProcessHandler();

        pb.setProcessListener(handler);
        pb.start();
    }
}
