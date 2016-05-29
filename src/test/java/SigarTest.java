import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Padmaka on 5/29/16.
 */
public class SigarTest {

    public static void main(String[] args) {
        String[] cmd = {
                "/bin/sh",
                "-c",
                "system_profiler SPHardwareDataType | grep Memory"
        };

        String line;
        String cpuString = "";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null) {
                cpuString += line;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(cpuString.trim());
        System.out.println(System.getProperty("os.name"));
    }
}
