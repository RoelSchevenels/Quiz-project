package Protocol.submits;

public class IdRangeSubmit extends Submit{
	private int min, max;

	public IdRangeSubmit(int min, int max)
	{
		super();
		this.min = min;
		this.max = max;
	}

	public int getMin()
	{
		return min;
	}

	public void setMin(int min)
	{
		this.min = min;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}
	
	
}
