package com.kratonsolution.skrip.leon.view;

public class CosineEngine {

	public static double similarity(int vec1[],int vec2[]) 
	{

		int dop=vec1[0]*vec2[0] +  vec1[1]*vec2[1];    
		double mag1 =Math.sqrt(Math.pow(vec1[0],2)+Math.pow(vec1[1],2));
		double mag2 =Math.sqrt(Math.pow(vec2[0],2)+Math.pow(vec2[1],2));    
		double csim =dop / (mag1 * mag2);

		return csim; 
	}


	public static double similarity(double vec1[],double vec2[]) 
	{

		double dop=vec1[0]*vec2[0] +  vec1[1]*vec2[1];    
		double mag1=Math.sqrt(Math.pow(vec1[0],2) + Math.pow(vec1[1],2));
		double mag2=Math.sqrt(Math.pow(vec2[0],2) + Math.pow(vec2[1],2));    
		double csim=dop/ (mag1 * mag2);

		return csim; 
	}
}
