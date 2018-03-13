package org.sample;

import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;


@State(Scope.Thread)
@OutputTimeUnit(NANOSECONDS)
@BenchmarkMode(AverageTime)
@Fork(value = 1, jvmArgsAppend = {
        "-XX:-UseSuperWord",
        "-XX:+UnlockDiagnosticVMOptions",
        "-XX:CompileCommand=print,*BenchmarkSIMDBlog.array1"})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
/**
 * Created by waixingren on 13/03/2018.
 */
public class BenchmarkHashloopSplit {


    public static final int SIZE = 1024;

    @State(Scope.Thread)
    public static class Context
    {
        public final int[] values = new int[SIZE];
        public final int[] results = new int[SIZE];

        @Setup
        public void setup()
        {
            Random random = new Random();
            for (int i = 0; i < SIZE; i++) {
                values[i] = random.nextInt(Integer.MAX_VALUE / 32);
            }
        }
    }

    @Benchmark
    public int[] hashLoopSplit(Context context)
    {
        for (int i = 0; i < SIZE; i++) {
            context.results[i] = getHashPosition1(context.values[i]);
        }

        for (int i = 0; i < SIZE; i++) {
            context.results[i] = getHashPosition2(context.results[i]);
        }

        for (int i = 0; i < SIZE; i++) {
            context.results[i] = getHashPosition3(context.results[i], 1048575);
        }

        return context.results;
    }

    private static int getHashPosition1(int rawHash)
    {
        rawHash ^= rawHash >>> 15;
        rawHash *= 0xed558ccd;
        return rawHash;
    }

    private static int getHashPosition2(int rawHash)
    {
        rawHash ^= rawHash >>> 15;
        rawHash *= 0x1a85ec53;
        return rawHash;
    }

    private static int getHashPosition3(int rawHash, int mask)
    {
        rawHash ^= rawHash >>> 15;
        return rawHash & mask;
    }
}
