package api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import models.Docente;

public class DocenteDeserializable implements JsonDeserializer<Docente> {
    @Override
    public Docente deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement element = json.getAsJsonObject();
        Log.i("LOG-RESULTS", "JSON DE ENTRADA --> "+element.toString());

        if(json.getAsJsonObject().get("docente") != null){
            element = json.getAsJsonObject().get("docente");
        }

        return (new Gson().fromJson(element, Docente.class));
    }
}
