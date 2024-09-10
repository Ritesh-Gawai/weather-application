import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import javax.imageio.ImageIO;

public class weatherswing extends JFrame {
    private JSONObject weatherData;

    weatherswing() {
        this.setTitle("Weathering APP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(360, 600);
        this.setLayout(null);
        this.setLocation(280, 50);
        this.setVisible(true);
        this.setBackground(Color.white);

        weatherdata();
    }

    private void weatherdata() {
        JTextField textField = new JTextField();
        textField.setBounds(20, 20, 250, 40);
        textField.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        add(textField);

        JButton button = new JButton(loadImage("src/assest/search.png"));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBounds(283, 18, 47, 40);
        button.setFocusable(false);
        add(button);


        JLabel cloud = new JLabel(loadImage("src/assest/cloudy.png"));
        cloud.setBounds(50, 110, 270, 130);
        add(cloud);


        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(-10, 280, 400, 54);
        temperatureText.setFont(new Font("Comic Sans MS", Font.BOLD, 48));

        // center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(40, 350, 300, 36);
        weatherConditionDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);


        JLabel humidity = new JLabel(loadImage("src/assest/humidity.png"));
        humidity.setBounds(-5, 470, 100, 70);
        add(humidity);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(75, 470, 80, 50);
        humidityText.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        add(humidityText);

        JLabel windspeed = new JLabel(loadImage("src/assest/windspeed.png"));
        windspeed.setBounds(160, 470, 100, 70);
        add(windspeed);

        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 100%</html>");
        windspeedText.setBounds(250, 470, 86, 50);
        windspeedText.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        add(windspeedText);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String city = textField.getText();

                // validate input - remove whitespace to ensure non-empty text
                if (city.trim().isEmpty()) {
                    return;
                }

                // retrieve weather data from OpenWeatherMap API
                try {
                    String apiKey = "aa3e839615a98b9671e5614b092b564a";
                    String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // parse JSON response
                        JSONObject weatherJson = new JSONObject(response.toString());
                        double temperature = weatherJson.getJSONObject("main").getDouble("temp");
                        String weatherCondition = weatherJson.getJSONArray("weather").getJSONObject(0).getString("main");
                        long humidity = weatherJson.getJSONObject("main").getLong("humidity");
                        double windSpeed = weatherJson.getJSONObject("wind").getDouble("speed");

                        // update GUI components
                        temperatureText.setText(temperature + " °C");
                        weatherConditionDesc.setText(weatherCondition);
                        humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
                        windspeedText.setText("<html><b>Windspeed</b> " + windSpeed + " km/h</html>");

                        // update weather image based on condition
                        if (weatherCondition.equals("Clear")) {
                            cloud.setIcon(loadImage("src/assest/clear.png"));
                        } else if (weatherCondition.equals("Clouds")) {
                            cloud.setIcon(loadImage("src/assest/cloudy.png"));
                        } else if (weatherCondition.equals("Rain")) {
                            cloud.setIcon(loadImage("src/assest/rain.png"));
                        } else if (weatherCondition.equals("Snow")) {
                            cloud.setIcon(loadImage("src/assest/snow.png"));
                        }
                    } else {
                        System.out.println("HTTP error: " + responseCode);
                    }
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns an image icon so that our component can render it
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }

    public static void main(String args[]) {
        new weatherswing();
    }
}
