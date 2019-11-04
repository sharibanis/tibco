package tibco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BigRandomNumberGenerator {
	static private Logger logger = Logger.getLogger(BigRandomNumberGenerator.class.getName());
	static long generateBigRandom() {
		long number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
		return number;
	}
	
	static boolean isPrime(long number) {
		int sqrt = (int) Math.sqrt(number) + 1;
        for (int i = 2; i < sqrt; i++) {
            if (number % i == 0) {
                // number is perfectly divisible - no prime
                return false;
            }
        }
        return true;
	}
	
	static long[] getFactors(long number) {
		int upperlimit = (int)(Math.sqrt(number));
        ArrayList<Long> factors = new ArrayList<Long>();
        for(long i=1;i <= upperlimit; i+= 1){
            if(number%i == 0){
                factors.add(i);
                if(i != number/i){
                    factors.add(number/i);
                }
            }
        }
        Collections.sort(factors);
        Long factorsArr[] = new Long[factors.size()];
        factorsArr = factors.toArray(factorsArr);
        long[] primLong = new long[factorsArr.length];
		for(int index = 0; index < factorsArr.length; index++) {
		    primLong[index] = factorsArr[index];
		}
        return primLong;
     }
	
	static class Task implements Runnable {
		private Logger logger = Logger.getLogger(Task.class.getName());
		private long number;
		
	    public Task(long n) { 
	    	number = n; 
	    }
	    
		public void run() {
			long threadId = Thread.currentThread().getId();
			if (isPrime(number)) {
				logger.info(threadId + " - " + number + " - " + number);
			} else {
				logger.info(threadId + " - " + number + " - " + Arrays.toString(getFactors(number)));
			}
	    }
	}
	
	public static void main(String[] args) {
		long number = 0;
		String format = "%s - %5$s %n";
		System.setProperty("java.util.logging.SimpleFormatter.format", format);
		try {
			long threadId = Thread.currentThread().getId();
	        long start = System.currentTimeMillis();
			int maxThreads = 10;
			long [] numbers = new long[maxThreads];
	        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);   
			for (int i = 0; i < maxThreads; i++) {
				number = generateBigRandom();
				numbers[i] = number;
			}
			logger.info(threadId + " - Big Numbers - "+ Arrays.toString(numbers));
			for (int i = 0; i < maxThreads; i++) {
		        pool.execute(new Task(numbers[i]));
			}
	        pool.shutdown();
	        long end = 0;
	        if (pool.awaitTermination(10, TimeUnit.SECONDS)) {
	        	end = System.currentTimeMillis();
		        long total = end - start;
		        logger.info("Total Time Taken = "+ total +" milliseconds" );
	        }
		} catch (Exception ex) {
	        logger.severe(ex.getMessage());
		}
	}
}
