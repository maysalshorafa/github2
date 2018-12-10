package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by Win8.1 on 12/9/2018.
 */

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
public class Generators {

    private String str;
    private int randInt;
    private final StringBuilder sb;
    private final List<Integer> l;
    private int length;

    public Generators() {
        this.l = new ArrayList<>();
        this.sb = new StringBuilder();


    }
    public Generators setLength(int length){
        if (length >=9 && length <= 15)
            this.length=length;
        else this.length=9;

        return this;

    }

    private void build() {

        //Add ASCII numbers of numbers from 0-9 and lower and upper case of characters


        for (int i = 97; i <= 122; i++) {
            l.add(i);

        }

       /*Randomise over the ASCII numbers and append respective character
         values into a StringBuilder*/

        for (int i = 0; i < length; i++) {
            randInt = l.get(new SecureRandom().nextInt(26));
            sb.append((char) randInt);

        }

        str = sb.toString();
    }



    public String generate() {

        build();
        return str;
    }



    public void buildId() {

        //Add ASCII numbers of numbers from 0-9 and lower and upper case of characters


        for (int i = 48; i <= 57; i++) {
            l.add(i);

        }
        for (int i = 0; i < length; i++) {
            randInt = l.get(new SecureRandom().nextInt(10));
            sb.append((char) randInt);

        }

        str = sb.toString();
    }

    public String generateId() {

        buildId();
        return str;
    }
}
