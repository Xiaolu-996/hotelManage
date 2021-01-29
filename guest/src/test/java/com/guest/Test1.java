package com.guest;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
public class Test1 {
    static BufferedReader reader;
    static StringTokenizer tokenizer;
    @Test
    public void fun(){
        Timestamp fromTime = new Timestamp(System.currentTimeMillis());
        String time = fromTime.toString();
        System.out.println(time);
    }
}
