package forGson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonGetter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonGetter.LocalDateTimeAdapter())
                .serializeNulls()
                .create();
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter jsonWriter, final LocalDateTime localDateTime)
                throws IOException {
            if (localDateTime == null) {
                jsonWriter.value("");
            } else {
                jsonWriter.value(localDateTime.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            if ((value == null) || value.equals("")) {
                return null;
            } else {
                return LocalDateTime.parse(value, formatter);
            }
        }
    }
}
