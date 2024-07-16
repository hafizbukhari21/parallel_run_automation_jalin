package Services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.json.simple.JSONObject;

import util.PropertiesUtil;

public class SendTelegram {
    private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
    private static final String BOT_TOKEN =progProp.getProperty("TOKEN");
    private static final String CHAT_ID = progProp.getProperty("chat_id") ;

  

    public static void sendMessage( String message) {
        String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // JSONObject json = new JSONObject();
            // json.put("chat_id", CHAT_ID);
            // json.put("text", message);

            String json = new StringBuilder()
                .append("{")
                .append("\"chat_id\":\"")
                .append(CHAT_ID)
                .append("\",")
                .append("\"text\":\"")
                .append(message)
                .append("\"")
                .append("}")
                .toString();

            System.out.println(json.toString());

            // try (OutputStream os = conn.getOutputStream()) {
            //     byte[] input = json.getBytes(StandardCharsets.UTF_8);
            //     os.write(input);
            // }

            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message sent successfully!");
            } else {
                System.out.println("Failed to send message.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
