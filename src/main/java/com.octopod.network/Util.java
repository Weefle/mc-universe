package com.octopod.network;

import com.octopod.octal.common.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod
 *         Created on 3/11/14
 */
public class Util {

	public static void writeAsync(final File file, final String text) {
		new Thread() {
			public void run() {
				try {
					write(file, text);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

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

    static public String generateArgs(String... arguments) {
        if(arguments.length == 0) return "";

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < arguments.length - 1; i++) {
            String element = arguments[i].replace("\"", "\\\"");
            if(element.contains(" ")) {
                sb.append("\"").append(element).append("\"");
            } else {
                sb.append(element);
            }
            sb.append(' ');
        }

        String last = arguments[arguments.length - 1].replace("\"", "\\\"");
        if(last.contains(" ")) {
            sb.append("\"").append(last).append("\"");
        } else {
            sb.append(last);
        }

        return sb.toString();
    }

    static public List<String> parseArgs(String text) {

        List<String> args = new ArrayList<String>();

        boolean inQuotes = false;
        boolean inEscape = false;

        StringBuilder element = new StringBuilder();

        char[] charArray = text.toCharArray();
        for(int i = 0; i < charArray.length; i++) {

            char c = charArray[i];

            if(c == '"' && !inEscape) {
                inQuotes = !inQuotes; //Toggle on/off inQuotes
                continue;
            }

            if(!inQuotes && c == ' ') {
                args.add(element.toString());
                element = new StringBuilder();
                continue;
            }

            if(c == '\\' && !inEscape) {
                inEscape = true;
                continue;
            } else {
                inEscape = false;
            }

            element.append(c);

        }

        if(!element.toString().equalsIgnoreCase("")) {
            args.add(element.toString());
        }

        return args;

    }

}
