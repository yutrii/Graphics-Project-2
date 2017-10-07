package ass2.spec;

import org.junit.Test;

import junit.framework.TestCase;

public class CombinationTest extends TestCase{

	private static final double EPSILON = 0.001;
	
	@Test
	public void test1() {
		assertEquals(MathUtil.getCombination(1, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(1, 1), 1, EPSILON);
	}
	
	@Test
	public void test2() {
		assertEquals(MathUtil.getCombination(2, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(2, 1), 2, EPSILON);
		assertEquals(MathUtil.getCombination(2, 2), 1, EPSILON);
	}
	
	@Test
	public void test3() {
		assertEquals(MathUtil.getCombination(3, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(3, 1), 3, EPSILON);
		assertEquals(MathUtil.getCombination(3, 2), 3, EPSILON);
		assertEquals(MathUtil.getCombination(3, 3), 1, EPSILON);
		
	}
	
	@Test
	public void test4() {
		assertEquals(MathUtil.getCombination(4, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(4, 1), 4, EPSILON);
		assertEquals(MathUtil.getCombination(4, 2), 6, EPSILON);
		assertEquals(MathUtil.getCombination(4, 3), 4, EPSILON);
		assertEquals(MathUtil.getCombination(4, 4), 1, EPSILON);
	}
	
	@Test
	public void test5() {
		assertEquals(MathUtil.getCombination(5, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(5, 1), 5, EPSILON);
		assertEquals(MathUtil.getCombination(5, 2), 10, EPSILON);
		assertEquals(MathUtil.getCombination(5, 3), 10, EPSILON);
		assertEquals(MathUtil.getCombination(5, 4), 5, EPSILON);
		assertEquals(MathUtil.getCombination(5, 5), 1, EPSILON);
	}
	
	@Test
	public void test6() {
		assertEquals(MathUtil.getCombination(6, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(6, 1), 6, EPSILON);
		assertEquals(MathUtil.getCombination(6, 2), 15, EPSILON);
		assertEquals(MathUtil.getCombination(6, 3), 20, EPSILON);
		assertEquals(MathUtil.getCombination(6, 4), 15, EPSILON);
		assertEquals(MathUtil.getCombination(6, 5), 6, EPSILON);
		assertEquals(MathUtil.getCombination(6, 6), 1, EPSILON);
	}
	
	@Test
	public void test7() {
		assertEquals(MathUtil.getCombination(7, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(7, 1), 7, EPSILON);
		assertEquals(MathUtil.getCombination(7, 2), 21, EPSILON);
		assertEquals(MathUtil.getCombination(7, 3), 35, EPSILON);
		assertEquals(MathUtil.getCombination(7, 4), 35, EPSILON);
		assertEquals(MathUtil.getCombination(7, 5), 21, EPSILON);
		assertEquals(MathUtil.getCombination(7, 6), 7, EPSILON);
		assertEquals(MathUtil.getCombination(7, 7), 1, EPSILON);
	}
	
	@Test
	public void test8() {
		assertEquals(MathUtil.getCombination(8, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(8, 1), 8, EPSILON);
		assertEquals(MathUtil.getCombination(8, 2), 28, EPSILON);
		assertEquals(MathUtil.getCombination(8, 3), 56, EPSILON);
		assertEquals(MathUtil.getCombination(8, 4), 70, EPSILON);
		assertEquals(MathUtil.getCombination(8, 5), 56, EPSILON);
		assertEquals(MathUtil.getCombination(8, 6), 28, EPSILON);
		assertEquals(MathUtil.getCombination(8, 7), 8, EPSILON);
		assertEquals(MathUtil.getCombination(8, 8), 1, EPSILON);
	}
	
	@Test
	public void test9() {
		assertEquals(MathUtil.getCombination(9, 0), 1, EPSILON);
		assertEquals(MathUtil.getCombination(9, 1), 9, EPSILON);
		assertEquals(MathUtil.getCombination(9, 2), 36, EPSILON);
		assertEquals(MathUtil.getCombination(9, 3), 84, EPSILON);
		assertEquals(MathUtil.getCombination(9, 4), 126, EPSILON);
		assertEquals(MathUtil.getCombination(9, 5), 126, EPSILON);
		assertEquals(MathUtil.getCombination(9, 6), 84, EPSILON);
		assertEquals(MathUtil.getCombination(9, 7), 36, EPSILON);
		assertEquals(MathUtil.getCombination(9, 8), 9, EPSILON);
		assertEquals(MathUtil.getCombination(9, 9), 1, EPSILON);
	}

}
