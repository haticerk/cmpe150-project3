import java.util.*;
import java.io.*;
public class project3{
	public static void main(String[] args) throws FileNotFoundException{
		
		int mode = Integer.parseInt(args[0]);
		//The variable 'mode' will show which part should be operated.
		
		File f = new File(args[1]);
		//The variable 'f' is the file which is input.
		Scanner file = new Scanner(f);
		//I use scanner here to get format and integers easily. 
		
		String imageFormat = file.nextLine();
		//'imageFormat' represents 'P3' everywhere in my code.
		
		int rows = file.nextInt();
		int columns = file.nextInt();
		//'rows' and 'columns' shows how much column and row ppm file has.
		
		int max = file.nextInt();
		//'max' is the maximum value of a pixel.
		
		//Here, I read the ppm file and create an 3D array.
		int[][][] imageArray = new int[rows][columns][3];
		for(int e=0; e<rows; e++){
			for(int r=0; r<columns; r++){
				for(int k=0; k<3; k++){
					imageArray[e][r][k]= file.nextInt();
				}
			}
		}
		
		//Now, due to the value of 'mode', it's operation time.
		if(mode==0){
			//when 'mode' is zero,code just does the writing another file. 
			writingPPMFile(imageFormat,max,imageArray);
		}if(mode==1){
			//when 'mode' is one, code gives a black-and-white version of image.
			blackAndWhite(imageArray, imageFormat, max);
		}if(mode==2){
			//when 'mode' is two, code should operate a filter on image.
			
			//before the method, first I changed filter to a 2D array.
			File filter2 = new File(args[2]);
			Scanner file2 = new Scanner(filter2);
			
			String size = file2.nextLine();
			int a = Integer.parseInt(size.substring(0,size.indexOf("x")));
			int b = Integer.parseInt(size.substring(size.indexOf("x")+1));
			
			int[][] filter = new int[a][b];
			for(int h=0; h<a; h++){
				for(int e=0; e<b; e++){
					filter[h][e] = file2.nextInt();
				}
			}
			//Then,method time.
			filter(imageArray,max,filter,imageFormat);
		}if(mode==3){
			//when 'mode' is three, code should quantize the image.
			//First, I took the range. 
			int range = Integer.parseInt(args[2]);
			//Then, the method.
			quantization(imageArray,range,imageFormat,max);
		}
	
	}
	
	//To write pixels to another file.
	public static void writingPPMFile(String imageFormat, int max, int[][][] array)throws FileNotFoundException {
		//First, to create a new file, we should throw FileNotFoundException.
		
		//Here, I create the output file. 
		File f = new File("output.ppm");
		PrintStream output = new PrintStream(f);
		
		int rows = array.length;
		int columns = array[0].length;
		
		//First I write to the file the necessary things.  
		output.println(imageFormat);
		output.println(rows  + " " + columns);
		output.println(max);
		//then, I wrote the value of pixels.
		for(int e=0; e<rows; e++ ){
			for(int r=0; r<columns; r++){
				for(int k=0; k<3; k++){
					output.print(array[e][r][k]+ " ");
				}
				output.print("\t");
			}
		}
		
	}
	
	//To get a black-and-version of image.
	public static void blackAndWhite(int[][][] array, String imageFormat, int max)throws FileNotFoundException{
		//First, to create a new file, we should throw FileNotFoundException.
		
		//To get a black-and-white version, we should average the pixel values. 
		for(int e=0; e<array.length; e++){
			for(int r=0; r<array[0].length; r++){
				int sum =0;
				for(int k=0; k<3; k++){
					sum += array[e][r][k];
				}
				for(int k=0; k<3; k++){
					array[e][r][k]=sum/3;
				}		
			}
		}
		//Now, in the array pixel values like 'x,x,x'.
		
		// Rest of method is the same as writingPPMFile method.
		File f = new File("black-and-white.ppm");
		PrintStream output = new PrintStream(f);
		
		int rows = array.length;
		int columns = array[0].length;
		
		output.println(imageFormat);
		output.println(rows  + " " + columns);
		output.println(max);
		
		for(int e=0; e<rows; e++ ){
			for(int r=0; r<columns; r++){
				for(int k=0; k<3; k++){
					output.print(array[e][r][k]+ " ");
				}
				output.print("\t");
			}
		}	
	}
	//To operate filter on image.
	public static void filter(int[][][] array,int max, int[][] filter, String imageFormat)throws FileNotFoundException{
		//First, to create a new file, we should throw FileNotFoundException.
		
		int a = filter.length;
		int b = filter[0].length;
		
		int[][][] arrayNew = new int[array.length-2][array[0].length-2][3];
		//I create a new array to storage results of calculations. It is smaller than first array.
		
		for(int e=0; e<array.length-a+1; e++){
			for(int r=0; r<array[0].length-b+1; r++){
				for(int k=0; k<3; k++){
					int sum=0;
					for(int h=0; h<a; h++){
						for(int t=0; t<b; t++){
							sum += filter[h][t]*array[e+h][r+t][k]; 
						}
					}
					if(sum/a*b<0){
						sum=0;
					}if(sum/a*b>max){
						sum=max;
					}
					arrayNew[e][r][k] = sum/a*b;
				}
			}
		}
		
		// now, arrayNew should be black-and-white. This part is same as black-and-white method.
		for(int e=0; e<arrayNew.length; e++){
			for(int r=0; r<arrayNew[0].length; r++){
				int sum =0;
				for(int k=0; k<3; k++){
					sum += arrayNew[e][r][k];
				}
				for(int k=0; k<3; k++){
					arrayNew[e][r][k]=sum/3;
				}		
			}
		}
		//This part is the same as writingPPMFile method.
		File f = new File("convolution.ppm");
		PrintStream output = new PrintStream(f);
		
		int rows = arrayNew.length;
		int columns = arrayNew[0].length;
		
		output.println(imageFormat);
		output.println(rows  + " " + columns);
		output.println(max);
		
		for(int e=0; e<rows; e++ ){
			for(int r=0; r<columns; r++){
				for(int k=0; k<3; k++){
					output.print(arrayNew[e][r][k]+ " ");
				}
				output.print("\t");
			}
		}	
	}
	//To quantize the image.
	public static void quantization(int[][][] array,int range, String imageFormat, int max)throws FileNotFoundException{
		//First, to create a new file, we should throw FileNotFoundException.
		
		//I looked every pixel to look every neighbor.
		for(int k=0; k<3; k++){
			for(int r=0; r<array[0].length; r++){
				for(int e=0; e<array.length; e++){
					int value = array[e][r][k];
					recursionQuan(array, value, range, e, r, k);
				}
			}
		}
		//This part is the same as writingPPMFile method.
		File f = new File("quantized.ppm");
		PrintStream output = new PrintStream(f);
		
		int rows = array.length;
		int columns = array[0].length;
		
		output.println(imageFormat);
		output.println(rows  + " " + columns);
		output.println(max);
		
		for(int e=0; e<rows; e++ ){
			for(int r=0; r<columns; r++){
				for(int k=0; k<3; k++){
					output.print(array[e][r][k]+ " ");
				}
				output.print("\t");
			}
		}
		
	}
	//To look every neighbor of pixel.
	public static int[][][] recursionQuan(int[][][] array, int value, int range, int e, int r, int k){
		if(e<array.length-1){
			//I looked the right pixel.
			if(array[e+1][r][k]<=value+range && value-range<=array[e+1][r][k] && !(array[e+1][r][k]==value)){
				array[e+1][r][k] = value;
				recursionQuan(array, value, range, e+1, r, k);
			}
		}
		
		if(e>0){
			//I looked the left pixel.
			if(array[e-1][r][k]<=value+range && value-range<=array[e-1][r][k] && !(array[e-1][r][k]==value)){
				array[e-1][r][k] = value;
				recursionQuan(array, value, range, e-1, r, k);
			}
		}
		
		if(r<array[0].length-1){
			//I looked the under.
			if(array[e][r+1][k]<=value+range && value-range<=array[e][r+1][k] &&  !(array[e][r+1][k]==value)){
				array[e][r+1][k] = value;
				recursionQuan(array, value, range, e, r+1, k);
			}
		}
		if(r>0){
			//I looked the top.
			if(array[e][r-1][k]<=value+range && value-range<=array[e][r-1][k] && !(array[e][r-1][k]==value)){
				array[e][r-1][k] = value;
				recursionQuan(array, value, range, e, r-1, k);
			}
		}
		
		if(k<2){
			//I looked inside.
			if(array[e][r][k+1]<=value+range && value-range<=array[e][r][k+1] && !(array[e][r][k+1]==value)){
				array[e][r][k+1] = value;
				recursionQuan(array, value, range, e, r, k+1);
			}
		}
		if(k>0){
			//I looked outside.
			if(array[e][r][k-1]<=value+range && value-range<=array[e][r][k-1] && !(array[e][r][k-1]==value)){
				array[e][r][k-1] = value;
				recursionQuan(array, value, range, e, r, k-1);
			}
		}
		
		return array;
	}
}
