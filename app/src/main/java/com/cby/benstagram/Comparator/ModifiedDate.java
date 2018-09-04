package com.cby.benstagram.Comparator;

import java.io.File;
import java.util.Comparator;

public class ModifiedDate implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        File f1 = (File)o1;
        File f2 = (File)o2;
        if (f1.lastModified() > f2.lastModified())
            return -1;

        if (f1.lastModified() == f2.lastModified())
            return 0;

        return 1;
    }
}
