package com.basketball.rbgt.util;

/**
 * @author 俞春旺
 */
public class RandomNumberUtil {

	/**
	 * 随机获取单双
	 * @return
	 */
	public static String getRandomNumber()
	{
		String value = "",single = "",doubles="";
		int x = 0;
		for(int i=0;i<4;i++)
		{
			x=(int)(Math.random()*100);
			if(i < 3 )
			{
				if(x%2 == 1)
				{
					single = "单,";
					value = value + single;
				}else
				{
					doubles = "双,";
					value = value + doubles;
				}
			}else
			{
				if(x%2 == 1)
				{
					single = "单";
					value = value + single;
				}else
				{
					doubles = "双";
					value = value + doubles;
				}
			}
		}
		return value;
	}
	
}
