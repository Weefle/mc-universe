package com.octopod.network;

import com.octopod.octolib.common.IOUtils;

import java.io.*;
import java.util.HashMap;

/**
 * @author Octopod
 *         Created on 3/11/14
 */
public class Util {

    public static void write(File file, String text) throws IOException
    {
        write(file, new ByteArrayInputStream(text.getBytes()));
    }

    public static void write(File file, InputStream input) throws IOException
    {
        if(!file.exists())
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(file);
        IOUtils.copy(input, new FileOutputStream(file));
        IOUtils.closeSilent(output);
    }

    public static String toString(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int ch;
        while((ch = is.read()) != -1) sb.append((char)ch);
        IOUtils.closeSilent(is);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> mapFromJson(String json) {
        return NetworkPlus.gson().fromJson(json, (Class<HashMap<String, Object>>)(Class<?>)HashMap.class);
    }

}
