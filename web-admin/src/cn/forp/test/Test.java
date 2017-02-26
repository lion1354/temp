package cn.forp.test;

import java.text.DecimalFormat;

public class Test
{

	public static void main(String[] args)
	{
		DecimalFormat df = new DecimalFormat("000");
		System.out.println(df.format(100));
	}

}
