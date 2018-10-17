package adt.hashtable.closed;

import adt.hashtable.hashfunction.HashFunctionClosedAddress;
import util.Util;
import adt.hashtable.hashfunction.HashFunction;
import adt.hashtable.hashfunction.HashFunctionClosedAddressMethod;
import adt.hashtable.hashfunction.HashFunctionFactory;

import java.util.LinkedList;

public class HashtableClosedAddressImpl<T> extends AbstractHashtableClosedAddress<T> {

    /**
     * A hash table with closed address works with a hash function with closed
     * address. Such a function can follow one of these methods: DIVISION or
     * MULTIPLICATION. In the DIVISION method, it is useful to change the size
     * of the table to an integer that is prime. This can be achieved by
     * producing such a prime number that is bigger and close to the desired
     * size.
     * <p>
     * For doing that, you have auxiliary methods: Util.isPrime and
     * getPrimeAbove as documented bellow.
     * <p>
     * The length of the internal table must be the immediate prime number
     * greater than the given size (or the given size, if it is already prime).
     * For example, if size=10 then the length must
     * be 11. If size=20, the length must be 23. You must implement this idea in
     * the auxiliary method getPrimeAbove(int size) and use it.
     *
     * @param desiredSize
     * @param method
     */

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HashtableClosedAddressImpl(int desiredSize, HashFunctionClosedAddressMethod method) {
        int realSize = desiredSize;

        if (method == HashFunctionClosedAddressMethod.DIVISION) {
            /* Real size must the the immediate prime above */
            realSize = this.getPrimeAbove(desiredSize);
        }

        initiateInternalTable(realSize);
        HashFunction function = HashFunctionFactory.createHashFunction(method, realSize);
        this.hashFunction = function;
    }

    /**
     * It returns the prime number that is closest (and greater) to the given number.
     * If the given number is prime, it is returned.
     * You can use the method Util.isPrime to check if a number is prime.
     */
    int getPrimeAbove(int number) {
        int prime = number;

        while (!Util.isPrime(prime)) {
            prime++;
        }

        return prime;
    }

    /**
     * Inserts a non-null object into the hash table. The hashtable does not
     * work with duplicated elements. Every time that the insert is called, if
     * there is a collision, then the attribute COLLISION of this hashtable is
     * incremented.
     */
    @Override
    public void insert(T element) {
        if (element != null) {
            int index = this.getIndexHash(element);

            if (this.table[index] == null) {
                this.table[index] = new LinkedList<T>();
            } else {
                this.COLLISIONS++;
            }

            if (!((LinkedList<T>) this.table[index]).contains(element)) {
                ((LinkedList<T>) this.table[index]).add(element);
                this.elements++;
            }
        }
    }

    /**
     * Removes an element from the hash table.
     */
    @Override
    public void remove(T element) {
        if (element != null) {
            int index = this.indexOf(element);

            if (index != -1) {
                ((LinkedList<T>) this.table[index]).remove(element);
                this.elements--;
            }
        }
    }

    /**
     * Searches a given element in the hash table. If it is not in the table, the hash table returns null.
     */
    @Override
    public T search(T element) {
        T elementFound = null;

        if (element != null) {
            int index = this.indexOf(element);

            if (index != -1) {
                int indexList = ((LinkedList<T>) this.table[index]).indexOf(element);
                elementFound = ((LinkedList<T>) this.table[index]).get(indexList);
            }
        }

        return elementFound;
    }

    /**
     * Searches the index of an element in the hashtable. It returns -1 if the
     * element is not in the hashtable.
     */
    @Override
    public int indexOf(T element) {
        int indexOf = -1;

        if (element != null) {
            int index = this.getIndexHash(element);
            LinkedList<T> position = (LinkedList<T>) this.table[index];

            if (position != null && position.contains(element)) {
                indexOf = index;
            }
        }

        return indexOf;
    }

    /**
     * Return index generate by hash.
     */
    private int getIndexHash(T element) {
        return ((HashFunctionClosedAddress<T>) this.hashFunction).hash(element);
    }
}
