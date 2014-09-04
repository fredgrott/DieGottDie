package com.grottworkshop.diegottdie.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by fgrott on 9/4/2014.
 */
public final class Lists {

    private Lists() {}

    /**
     * New array list.
     *
     * @param <E>  the type parameter
     * @param elements the elements
     * @return the array list
     */
    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        //noinspection unchecked
        return (elements instanceof Collection)
                ? new ArrayList<>((Collection<E>)(elements))
                : newArrayList(elements.iterator());
    }

    /**
     * New array list.
     *
     * @param <E>  the type parameter
     * @param elements the elements
     * @return the array list
     */
    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = new ArrayList<>();
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return list;
    }
}
