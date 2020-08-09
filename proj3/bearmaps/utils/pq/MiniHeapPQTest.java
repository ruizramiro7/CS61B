package bearmaps.utils.pq;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MiniHeapPQTest {

    @Test
    public void pollTest(){
        MinHeapPQ<Integer> min = new MinHeapPQ<>();
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        naive.insert(3, 24.0);
        min.insert(3,24.0);
        naive.insert(7, 8.0);
        min.insert(7,8.0);
        System.out.println(min.size());
        System.out.println(min.peek());
        System.out.println(naive.peek());

    }

    @Test
    public void check(){
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        MinHeapPQ<Integer> min = new MinHeapPQ<>();
        naive.insert(5, -44.0);
        min.insert(5, -44.0);
        naive.insert(6, -44.0);
        min.insert(6, -44.0);
        naive.insert(7, 34.0);
        min.insert(7, 34.0);
        naive.insert(8, -80.0);
        min.insert(8, -80.0);
        naive.insert(9, -71.0);
        min.insert(9, -71.0);
        naive.insert(10, 98.0);
        min.insert(10, 98.0);
        System.out.println(min.peek());
        System.out.println(naive.peek());
        //System.out.println(min.poll());
        assertEquals(naive.poll(), min.poll());
       // naive.insert();
    }

    @Test
    public void testChangePriority(){
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        MinHeapPQ<Integer> min = new MinHeapPQ<>();
        naive.insert(5, -44.0);
        min.insert(5, -44.0);
        naive.insert(6, -44.0);
        min.insert(6, -44.0);
        naive.insert(7, 34.0);
        min.insert(7, 34.0);
        naive.insert(8, -80.0);
        min.insert(8, -80.0);
        naive.insert(9, -71.0);
        min.insert(9, -71.0);
        naive.insert(10, 98.0);
        min.insert(10, 98.0);
        assertEquals(naive.poll(), min.poll());
        min.changePriority(7, 0.0);
        naive.changePriority(7,0.0);
        min.changePriority(7,-24.0);
        naive.changePriority(7,24.0);
        System.out.println(min.peek());
        System.out.println(naive.peek());
        assertEquals(min.poll(), naive.poll());
        System.out.println(min.toString());
        System.out.println(naive.toString());
        int i =0;
        int[] test = new int[min.size()];
        while (min.size()!= 0){
            test[i] = min.poll();
            i++;
        }
        System.out.println(Arrays.toString(test));
    }

}
