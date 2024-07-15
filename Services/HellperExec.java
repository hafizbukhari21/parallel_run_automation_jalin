package Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HellperExec {
    public void Run(String source, String date, String env){


        String jarFilePath = "./HelperApp/HelperApp.jar";
        // Command to run the JAR file
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(jarFilePath);

        // Args
        command.add(source);
        command.add(date);
        command.add(env);
        command.add("");

        //java -Xms512m -Xmx6048m -jar HelperApp.jar 2000 20240401 1 1  

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        
        try {
            // Start the process
            Process process = processBuilder.start();
            
            // Read the standard output
            new Thread(()->this.Processing(process.getInputStream())).start();
            
            // Read the standard error
            new Thread(()->this.Processing(process.getErrorStream())).start();
            
            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Delete Source 2000 Once Done
            deleteSource2000();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void Processing(InputStream process){
          try (BufferedReader reader = new BufferedReader(new InputStreamReader(process))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    private void deleteSource2000(){
        
    }

    
}


