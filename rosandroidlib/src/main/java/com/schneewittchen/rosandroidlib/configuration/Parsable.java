package com.schneewittchen.rosandroidlib.configuration;

import java.util.HashMap;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 21.01.20
 * @modified by
 */
interface Parsable<T> {

    HashMap parse();
    T createFromMap(HashMap map);
}
