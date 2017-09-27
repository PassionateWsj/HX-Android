package com.intfocus.hx.util;

import com.intfocus.hx.subject.template_v2.entity.BargraphComparator;

import java.util.Comparator;

/**
 * Created by zbaoliang on 17-5-23.
 */
public class BargraphDataComparator implements Comparator<BargraphComparator> {

    public int compare(BargraphComparator obj1, BargraphComparator obj2) {
        float v1 = obj1.data;
        float v2 = obj2.data;
        if (v1 > v2) {
            return 1;
        } else if (v1 < v2) {
            return -1;
        } else {
            return 0;
        }
    }
}
