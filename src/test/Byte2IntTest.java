package test;

import java.nio.ByteBuffer;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

public class Byte2IntTest {

	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16)
				| ((src[offset + 3] & 0xFF) << 24));

		return value;
	}

	public static long bytesToLong1(byte[] bytes) {

		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();// need flip
		return buffer.getLong();
	}

	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, x);
		return buffer.array();
	}

	    public static long bytesToLong2(byte[] b) {
	        long s = 0;
	        long s0 = b[0] & 0xff;// 最低位
	        long s1 = b[1] & 0xff;
	        long s2 = b[2] & 0xff;
	        long s3 = b[3] & 0xff;
	        long s4 = b[4] & 0xff;// 最低位
	        long s5 = b[5] & 0xff;
	        long s6 = b[6] & 0xff;
	        long s7 = b[7] & 0xff;

	        // s0不变
	        s1 <<= 8;
	        s2 <<= 16;
	        s3 <<= 24;
	        s4 <<= 8 * 4;
	        s5 <<= 8 * 5;
	        s6 <<= 8 * 6;
	        s7 <<= 8 * 7;
	        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
	        return s;
	    }


	public static double arr2double(byte[] arr, int start) {
		int i = 0;
		int len = 8;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}

	public static byte[] doubleToBytes(double d)
	{
	    byte writeBuffer[]= new byte[8];
	     long v = Double.doubleToLongBits(d);
	        writeBuffer[0] = (byte)(v >>> 56);
	        writeBuffer[1] = (byte)(v >>> 48);
	        writeBuffer[2] = (byte)(v >>> 40);
	        writeBuffer[3] = (byte)(v >>> 32);
	        writeBuffer[4] = (byte)(v >>> 24);
	        writeBuffer[5] = (byte)(v >>> 16);
	        writeBuffer[6] = (byte)(v >>>  8);
	        writeBuffer[7] = (byte)(v >>>  0);
	        return writeBuffer;

	}

	public static void main(String[] args) {


//		byte[] b1 = {-127,35,1,0,0,0,0,0};
//		byte[] b2 = {-126, 36, 1,0,0,0,0,0};
//		byte[] b3 = {-120, 37, 1,0,0,0,0,0};
//		byte[] b4 = {-85, 36, 1,0,0,0,0,0};
//		byte[] b5 = {-94, 38, 1,0,0,0,0,0};
		byte[] b11 = {-75, 37, 1, 0, 0, 0, 0, 0};
		byte[] b21 = {-120, 37, 1, 0, 0, 0, 0, 0};
		byte[] b31 = {-58, 36, 1, 0, 0, 0, 0, 0};
		byte[] b41 = {42, 38, 1, 0, 0, 0, 0, 0};
		int i1 = bytesToInt(b11, 0);
		int i2 = bytesToInt(b21, 0);
		int i3 = bytesToInt(b31, 0);
		int i4 = bytesToInt(b41, 0);

		byte[] lucene = {-73,5,0,0, 0, 0, 0, 0};
		int i5 = bytesToInt(lucene, 0);
		System.out.println("i5 is: "  + i5);

		int total = i1+i2+i3+i4;
		System.out.println(i1 + " " + i2 + " " + i3 + " " + i4 + ": " + total);


		byte[] b2 = {-121, -21, 111, 115, 95, 51, 5, 66, -75, 37, 1, 0, 0, 0, 0, 0};
		byte[] b3 = {-42, -11, -122, -45, -29, 49, 5, 66, -120, 37, 1, 0, 0, 0, 0, 0};
		byte[] b4 = {66, -10, 96, -105, 24, 36, 5, 66, -58, 36, 1, 0, 0, 0, 0, 0};
		byte[] b5 = {-45, 122, -26, -54, 72, 55, 5, 66, 42, 38, 1, 0, 0, 0, 0, 0};


		double d2 = arr2double(b2,0);
		double d3 = arr2double(b3,0);
		double d4 = arr2double(b4,0);
		double d5 = arr2double(b5,0);


		System.out.println(d2 + " " + d3 + " " + d4 + " " + d5);
		System.out.println(d2+d3+d4+d5);
		System.out.println((d2+d3+d4+d5)/total);

		long l1 = 12345;
		byte[] l1bytes = longToBytes(75189L);

		long l2 = bytesToLong1(l1bytes);
		System.out.println(l2);

		double dd  = 12.34;
		byte[] ddBytes = doubleToBytes(dd);


		Slice slice = Slices.allocate(100);
		slice.setBytes(0, ddBytes);
		slice.setBytes(8, l1bytes);

		System.out.println("aaa");
	}
}
