package test;

import io.airlift.slice.DynamicSliceOutput;
import io.airlift.slice.SliceOutput;

public class SliceTest {

	public static void main(String[] args) {
		
		SliceOutput sliceOutput = new DynamicSliceOutput(16 * 10);
		sliceOutput.writeDouble(12.33);
		Double d = sliceOutput.slice().getDouble(0);
		System.out.println(d);
	}
}
