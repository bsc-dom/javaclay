
package es.bsc.dataclay.test;

import java.util.Arrays;

public class Statistics {
	long[] data;
	double size;    

	public Statistics(final long[] data) 
	{
		this.data = data;
		size = data.length;
	}   

	public long getMean()
	{
		long sum = 0L;
		for(long a : data)
			sum += a;
		return (long) (sum/size);
	}

	public long getVariance()
	{
		long mean = getMean();
		long temp = 0;
		for(Long a :data)
			temp += (mean-a)*(mean-a);
		return (long) (temp/size);
	}

	public long getStdDev()
	{
		return (long) Math.sqrt(getVariance());
	}

	public long median() 
	{
		long[] b = new long[data.length];
		System.arraycopy(data, 0, b, 0, b.length);
		Arrays.sort(b);

		if (data.length % 2 == 0) 
		{
			return (long) ((b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0);
		} 
		else 
		{
			return b[b.length / 2];
		}
	}

	public long min() 
	{
		long[] b = new long[data.length];
		System.arraycopy(data, 0, b, 0, b.length);
		Arrays.sort(b);
		return b[0];
	}

	public long max() 
	{
		long[] b = new long[data.length];
		System.arraycopy(data, 0, b, 0, b.length);
		Arrays.sort(b);
		return b[b.length - 1];
	}
}
