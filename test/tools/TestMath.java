/**
*	TestMath.java
*
*	@author Johan
*/

package tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMath {

	@Test
	public void testFibonacci() {
		
		assertEquals(1, Math.fib(1));
		assertEquals(1, Math.fib(2));
		assertEquals(2, Math.fib(3));
		assertEquals(3, Math.fib(4));
		assertEquals(5, Math.fib(5));
	}
	
	@Test
	public void testIsPrime() {
		assertTrue(Math.isPrime(1));
		assertTrue(Math.isPrime(3));
		assertTrue(Math.isPrime(5));
		assertTrue(Math.isPrime(7));
		assertFalse(Math.isPrime(0));
	}
	
	@Test
	public void testPrimeForNumber() {
		assertEquals(1, Math.prime(1));
		assertEquals(3, Math.prime(2));
		assertEquals(5, Math.prime(3));
		assertEquals(7, Math.prime(4));
		
		assertNotSame(2, Math.prime(1));
	}

}
