// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 * modified by Jonah
 */
public class QuadraticProbingHashTable<AnyType>
{
    /**
     * Construct the hash table.
     */
    public QuadraticProbingHashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public QuadraticProbingHashTable( int size )
    {
        allocateArray( size );
        makeEmpty( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        inserting = true;
            // Insert x as active
        int currentPos = findPos( x );
        if( isActive( currentPos ) ) {
            inserting = false;
            return;
        }

        array[ currentPos ] = new HashEntry<AnyType>( x, true );
        numOfElements++;

            // Rehash; see Section 5.5
        if( ++currentSize > array.length / 2 )
            rehash( );

        inserting = false;
    }

    /**
     * Expand the hash table.
     */
    private void rehash( )
    {
        HashEntry<AnyType> [ ] oldArray = array;

            // Create a new double-sized, empty table
        allocateArray( nextPrime( 2 * oldArray.length ) );

        currentSize = numOfElements = collisions = 0;
        avgCollisionChain = longestCollisionChain = 1;

            // Copy table over
        for( int i = 0; i < oldArray.length; i++ )
            if( oldArray[ i ] != null && oldArray[ i ].isActive )
                insert( oldArray[ i ].element );
    }

    /**
     * Method that performs quadratic probing resolution.
     * Assumes table is at least half empty and table length is prime.
     * @param x the item to search for.
     * @return the position where the search terminates.
     */
    private int findPos( AnyType x )
    {
        int offset = 1;
        int currentPos = myhash( x );
        int collisionChainLength = 1;

        while( array[ currentPos ] != null &&
                !array[ currentPos ].element.equals( x ) )
        {
            collisionChainLength++;
            currentPos += offset;  // Compute ith probe
            offset += 2;
            if( currentPos >= array.length )
                currentPos -= array.length;
        }

        if(inserting) {
            if (collisionChainLength > 1) {
                collisions++;
                if (collisionChainLength > longestCollisionChain)
                    longestCollisionChain = collisionChainLength;
            }
            avgCollisionChain = ((avgCollisionChain * numOfElements) + collisionChainLength) / (numOfElements + 1);
        }

        return currentPos;
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        int currentPos = findPos( x );
        if( isActive( currentPos ) ) {
            array[ currentPos ].isActive = false;
            numOfElements--;
        }
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public boolean contains( AnyType x )
    {
        int currentPos = findPos( x );
        return isActive( currentPos );
    }

    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        currentSize = numOfElements = collisions = 0;
        avgCollisionChain = longestCollisionChain = 1;

        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }

    private int myhash( AnyType x )
    {
        int hashVal = x.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }
	
    private static class HashEntry<AnyType>
    {
        public AnyType  element;   // the element
        public boolean isActive;  // false if marked deleted

        public HashEntry( AnyType e )
        {
            this( e, true );
        }

        public HashEntry( AnyType e, boolean i )
        {
            element  = e;
            isActive = i;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 11;

    private HashEntry<AnyType> [ ] array; // The array of elements
    private int currentSize;              // The number of occupied cells
    private int numOfElements;          // The number of active elements in the hash table
    private int collisions;           // The number of collisions
    private double avgCollisionChain = 1;   // The average length of all collision chains
    private int longestCollisionChain = 1; // The length of the longest collision chain
    private boolean inserting = false; // true when insert() is called so that findPos() can know if insert() called it

    /**
     * Returns the number of elements in the hash table.
     * (Or the number of words for this assignment)
     * @return the number of active elements currently in the hash table
     */
    public int getNumberOfElements() {
        return numOfElements;
    }

    /**
     * Returns the hash table size.
     * @return the size of the hash table
     */
    public int getHashTableSize() {
        return array.length;
    }

    /**
     * Returns the currentload factor of the hash table.
     * @return the load factor of the hash table
     */
    public double getLoadFactor() {
        return (double) currentSize / getHashTableSize();
    }

    /**
     * Returns the number of insertions that encountered a collision since the last rehash.
     * @return the number of collisions
     */
    public int getCollisions() {
        return collisions;
    }

    /**
     * Returns the average length of all collision chains since the last rehash.
     * Note, when there is no collision, the collision chain length is 1
     * @return the average length of all collision chains
     */
    public double getAvgCollisionChain() {
        return avgCollisionChain;
    }

    /**
     * Returns the length of the longest collision chain since the last rehash.
     * Note, when there is no collision, the collision chain length is 1
     * @return the length of the longest collision chain
     */
    public int getLongestCollisionChain() {
        return longestCollisionChain;
    }

    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
     @SuppressWarnings("unchecked")
    private void allocateArray( int arraySize )
    {
        array = new HashEntry[ nextPrime( arraySize ) ];
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime( int n )
    {
        if( n <= 0 )
            n = 3;

        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }


    // Simple main
    public static void main( String [ ] args )
    {
        QuadraticProbingHashTable<String> H = new QuadraticProbingHashTable<String>( );

        final int NUMS = 400000;
        final int GAP  =   37;

        System.out.println( "Checking... (no more output means success)" );


        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            H.insert( ""+i );
        for( int i = 1; i < NUMS; i+= 2 )
            H.remove( ""+i );

        for( int i = 2; i < NUMS; i+=2 )
            if( !H.contains( ""+i ) )
                System.out.println( "Find fails " + i );

        for( int i = 1; i < NUMS; i+=2 )
        {
            if( H.contains( ""+i ) )
                System.out.println( "OOPS!!! " +  i  );
        }
    }

}
