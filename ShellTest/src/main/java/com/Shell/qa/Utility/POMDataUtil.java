package com.Shell.qa.Utility;

import java.util.Hashtable;


public class POMDataUtil {
	
	public static final String TestDataSheet ="POM TestData";
	public static String sheet="POM TestCases";
	
	public static Object[][] getTestData(POMXls_Reader xls,String TestCaseName){
					
				String SheetName=TestDataSheet;
				
				//To get the row number from where "TestC" is starting
				
				int testStartRowNum=1;
				while(!xls.getCellData(SheetName, 0, testStartRowNum).equals(TestCaseName)){
					testStartRowNum++;	
				}
				
				//To get the row number from where "TestC" heading is started i.e RunMode,Col1,Col2
				int coloumStartRowNum= testStartRowNum+1;
				
				//To get the row number from where "TestC" data is started 
				int dataStartRowNum= testStartRowNum+2;
				
				//To get the total number of row number containing data for "TestC" 
				int rows=0;
				while(!xls.getCellData(SheetName, 0, dataStartRowNum+rows).equals("")){
					rows++;
				}
				
				//To get the number of column till where Data is extending
				
				int coloums=0;
				
				while(!xls.getCellData(SheetName, coloums, coloumStartRowNum).equals("")){
				coloums++;
				}
				
				//Get the Data
				
				Object[][] data= new Object[rows][1];
				int datarow=0;
				
				//Declaring Object of HashTable
				Hashtable<String,String> table=null;
				for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
					
					//Create an Object of HashTable inside row loop(every row will have its own Hash table)
					table=new Hashtable<String,String>();
					for(int cNum=0;cNum<coloums;cNum++){
						
						//Key value of the Hash Table
						String key= xls.getCellData(SheetName, cNum, coloumStartRowNum);
						String value= xls.getCellData(SheetName, cNum, rNum);
						table.put(key, value);
						
					}
					
					data[datarow][0]=table;
					datarow++;
				}
				return data;
				
		
	}
	
	
	//Function to check TCid's Run Modes
	
	public static boolean TestCaseIDToRun(String TestName,POMXls_Reader xls){

		int rows= xls.getRowCount(sheet);
		//System.out.println("Total Number of Rows:-"+rows);
		for(int r=2;r<=rows;r++){
			String tName=xls.getCellData(sheet, "TCID", r);
			if(tName.equals(TestName)){
				String runmode=xls.getCellData(sheet, "Runmodes", r);
				System.out.println("Run Mode for test: "+tName+" is "+runmode);
				if(runmode.equals("Y"))
					return true;
				else
					return false;
			}
		}
		return false;
	}

}
