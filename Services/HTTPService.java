package Services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import util.DatabaseUtil;
import util.LogUtil;
import util.PropertiesUtil;

public class HTTPService {
	private static Logger logger = LogUtil.getLogger(DatabaseUtil.class.getName());

    public static void httpRun(String endpoint){
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);


            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Berhasil Hit Endpoint");
            } else {
                System.out.println("Gagal Hit endpoitn");
            }


        } catch (Exception e) {
            logger.info(e);
        }   
    }
}
