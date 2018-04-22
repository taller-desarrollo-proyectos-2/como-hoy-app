package com.fiuba.gaff.comohoy.filters;

import android.support.annotation.NonNull;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CalificationFilter implements Filter {
    List<Commerce> mCommerceList;
    float mPuntuation;

    public CalificationFilter(List<Commerce> commerceList, float puntuation) {
        mCommerceList = commerceList;
        mPuntuation = puntuation;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> mCommerceListFilter = new List<Commerce>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Commerce> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(Commerce commerce) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Commerce> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends Commerce> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Commerce get(int index) {
                return null;
            }

            @Override
            public Commerce set(int index, Commerce element) {
                return null;
            }

            @Override
            public void add(int index, Commerce element) {

            }

            @Override
            public Commerce remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Commerce> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Commerce> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<Commerce> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            float puntuationConvert = Float.parseFloat(commerce.getRating());
            if (puntuationConvert > mPuntuation) {
                mCommerceListFilter.add(commerce);
            }
        }
        return mCommerceListFilter;
    }
}