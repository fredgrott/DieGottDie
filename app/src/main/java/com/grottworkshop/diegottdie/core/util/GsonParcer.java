package com.grottworkshop.diegottdie.core.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import flow.Parcer;

/**
 * Created by fgrott on 9/2/2014.
 */
public class GsonParcer<T> implements Parcer<T> {
    private final Gson gson;

    public GsonParcer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Parcelable wrap(T instance) {
        try {
            String json = encode(instance);
            return new Wrapper(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T unwrap(Parcelable parcelable) {
        Wrapper wrapper = (Wrapper) parcelable;
        try {
            return decode(wrapper.json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String encode(T instance) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        try {
            Class<?> type = instance.getClass();

            writer.beginObject();
            writer.name(type.getName());
            gson.toJson(instance, type, writer);
            writer.endObject();

            return stringWriter.toString();
        } finally {
            writer.close();
        }
    }

    private T decode(String json) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(json));

        try {
            reader.beginObject();

            Class<?> type = Class.forName(reader.nextName());
            return gson.fromJson(reader, type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            reader.close();
        }
    }

    private static class Wrapper implements Parcelable {
        final String json;

        Wrapper(String json) {
            this.json = json;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel out, int flags) {
            out.writeString(json);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Creator<Wrapper> CREATOR = new Parcelable.Creator<Wrapper>() {
            @Override public Wrapper createFromParcel(Parcel in) {
                String json = in.readString();
                return new Wrapper(json);
            }

            @Override public Wrapper[] newArray(int size) {
                return new Wrapper[size];
            }
        };
    }
}
