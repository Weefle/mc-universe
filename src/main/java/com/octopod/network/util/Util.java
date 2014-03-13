package com.octopod.network.util;

import com.octopod.octolib.common.IOUtils;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

import java.io.*;

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

}
