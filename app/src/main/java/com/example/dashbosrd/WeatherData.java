package com.example.dashbosrd;

public class WeatherData {

    double latitude;
    double longitude;
    double generationtime_ms;
    double utc_offset_seconds;
    String timezone;
    String timezone_abbreviation;
    int elevation;

    current_weatherClass current_weather;

    public current_weatherClass getCurrentWeather() {
        return current_weather;
    }

    public void setCurrentWeather(current_weatherClass current_weather) {
        this.current_weather = current_weather;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getGenerationtime_ms() {
        return generationtime_ms;
    }

    public void setGenerationtime_ms(double generationtime_ms) {
        this.generationtime_ms = generationtime_ms;
    }

    public double getUtc_offset_seconds() {
        return utc_offset_seconds;
    }

    public void setUtc_offset_seconds(double utc_offset_seconds) {
        this.utc_offset_seconds = utc_offset_seconds;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezone_abbreviation = timezone_abbreviation;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }


    class current_weatherClass {
        float temperature;
        float windspeed;
        int  winddirection;
        int weathercode;
        int is_day;
        String time;

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public float getWindspeed() {
            return windspeed;
        }

        public void setWindspeed(float windspeed) {
            this.windspeed = windspeed;
        }

        public int getWinddirection() {
            return winddirection;
        }

        public void setWinddirection(int winddirection) {
            this.winddirection = winddirection;
        }

        public int getWeathercode() {
            return weathercode;
        }

        public void setWeathercode(int weathercode) {
            this.weathercode = weathercode;
        }

        public int getIs_day() {
            return is_day;
        }

        public void setIs_day(int is_day) {
            this.is_day = is_day;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
