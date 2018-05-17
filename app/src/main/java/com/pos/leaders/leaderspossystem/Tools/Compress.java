package com.pos.leaders.leaderspossystem.Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by KARAM on 20/04/2017.
 */

public class Compress {
    private static final int BUFFER = 1024;

    private String[] _files;
    private String _zipFile;

    public Compress(String[] files, String zipFile) {
        _files = files;
        _zipFile = zipFile;
    }

    public void zip() {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class customList<T,E> extends AbstractCollection<T> {

    private List<E> t;
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return t.size();
    }
}


class test{
    public static void main1(String[] args){
        String []s=new String[2]; //declare an array for storing the files i.e the path of your source files
        s[0]="C:/User/TOSHIBA/Desktop/11/clover-pos-station-front.jpg";    //Type the path of the files in here
        s[1]="C:/User/TOSHIBA/Desktop/11/shopify-pos-uk.jpg"; // path of the second file
        new Compress(s,"C:/User/TOSHIBA/Desktop/11/MyZipFolder.zip").zip();
        //zip((s,"/mnt/sdcard/MyZipFolder.zip");    //call the zip function
    }

}

class test2{
    public static void main(String[] args){
        List<A> a = new ArrayList<A>();
        List<B> b = new ArrayList<B>();
        List<C> c = new ArrayList<C>();
        Long d = new Date().getTime();
        a.add(new A(1, new Date(d - 1000000)));
        a.add(new A(2, new Date(d + 1000000)));
        a.add(new A(3, new Date(d - 2000000)));

        b.add(new B(4, new Date(d - 3000000)));
        b.add(new B(5, new Date(d + 1400000)));

        c.add(new C(6, new Date(d - 4000000)));
        c.add(new C(7, new Date(d + 4000000)));

        for (A _a:a) {
            addA(_a);
        }
        for (B _b:b) {
            addB(_b);
        }
        for (C _c:c) {
            addC(_c);
        }

        Collections.sort(abcList, new Comparator<ABC>(){
            @Override
            public int compare(ABC o1, ABC o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for(int i = 0; i < abcList.size(); i++){
            System.out.println(abcList.get(i).getDate().toString() + "/t" + abcList.get(i).getObj().toString());
        }

        System.out.println(String.format(new Locale("en"),"%015.3f",15.1234));
        double totalSaved = 12345.67;
        System.out.println("+" + String.format(new Locale("en"), "%012.0f", totalSaved - 0.5) +String.format(new Locale("en"), "%02d", (int)((totalSaved-Math.floor(totalSaved))*100)));


    }
    public interface ABC {
        Object getObj();
        Date getDate();
    }

    static List<ABC> abcList=new ArrayList<>();

    public static void addA(final A _a) {
        abcList.add(new ABC() {
            public Object getObj() {
                return _a;
            }
            public Date getDate() {
                return _a.date;
            }
        });
    }
    public static void addB(final B _b) {
        abcList.add(new ABC() {
            public Object getObj() {
                return _b;
            }
            public Date getDate() {
                return _b.date;
            }
        });
    }
    public static void addC(final C _c) {
        abcList.add(new ABC() {
            public Object getObj() {
                return _c;
            }
            public Date getDate() {
                return _c.date;
            }
        });
    }


}
class A{
    int id;
    Date date;
    A(int id,Date date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public String toString() {
        return "A{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
class B{
    int id;
    Date date;
    B(int id,Date date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public String toString() {
        return "B{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
class C{
    int id;
    Date date;
    C(int id,Date date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public String toString() {
        return "C{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
