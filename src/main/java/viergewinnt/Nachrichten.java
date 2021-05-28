package viergewinnt;

import javax.json.Json;
import java.io.StringReader;

public class Nachrichten {

    public static interface Json<T> {
        public static Json<Object> json(String json) {return null;};
    }

    public static class Nachricht extends Object implements Json {
        public String aktion;

        public Nachricht(String aktion) {
            this.aktion = aktion;
        }

        public static Nachricht json(String json) {
            return new Nachricht(json);
        }

    }

    public static class NachrichtMitDaten<T> extends Nachricht{
        T daten;

        public NachrichtMitDaten(String aktion, T daten) {
            super(aktion);
            this.daten = daten;
        }

        public static NachrichtMitDaten<Object> json (String json) {
            return new NachrichtMitDaten<Object>(javax.json.Json.createReader(new StringReader(json)).readObject().get("aktion").toString(), javax.json.Json.createReader(new StringReader(json)).readObject().get(""));
        }
    }

    public static class Error {
        public String info;
    }
}
