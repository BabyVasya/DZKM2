package org.example;

import java.util.List;

/**
 * Interface which allows getting access to inner data for TripletDeque
 */
public interface Containerable{

    Object[] getContainerByIndex(int cIndex);

}
