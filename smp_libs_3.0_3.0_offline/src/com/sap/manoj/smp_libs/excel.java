package com.sap.manoj.smp_libs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.content.Context;
import android.util.Log;

public class excel {
	public static void saveExcelFile(Context context, int stepcode) { 
		try {
			//File file2 = new File("/mnt/sdcard/data/app/out.xls");
			//File file2 = new File("/mnt/shell/emulated/0/out.xls");
			
		 String step=null;
		 switch (stepcode)
		 {
		 case 0:
			 step = "onboard";
			 break;
			 case 1:
				 step = "service document";
			break;
			 case 2:
				 step = "Metadata";
			break;
			 case 3:
				 step = "Collection 300 records";
			break;
			 case 4:
				 step = "Collection 1000 records";
			break;
			 case 5:
				 step = "Get";
			break;
			 case 6:
				 step = "Put";
			break;
			 case 7:
				 step = "Delete";
			break;
			 case 8:
				 step = "Add";
			break;
			 case 9:
				 step = "Upload 100kb";
			break;
			 case 10:
				 step = "Download 100kb";
			break;
			 case 11:
				 step = "Upload 500kb";
			break;
			 case 12:
				 step = "Download 500kb";
			break;
			 default:
				 step = "Error";
		    break;
				 
		 }
		 Log.d("Timings",step+" - Req/Resp="+(helper.stop-helper.start)+" Parser="+(helper.end-helper.stop)+" E2E="+(helper.end-helper.start));
		 //be carefukl with the location
		 //File file2 = new File("/data/out.xls");
		 File file2 = new File("//mnt/sdcard/data/app/out.xls");
		    FileInputStream file = new FileInputStream(file2);
		 
		    Workbook workbook = new HSSFWorkbook(file);
		    Sheet sheet = workbook.getSheetAt(0);
		    Cell cell = null;
		    //Update the value of cell
		    int k = helper.row++;
		    sheet.createRow(k);
		    cell = sheet.getRow(k).createCell(0);
		    cell.setCellValue(step);
		    cell = sheet.getRow(k).createCell(1);
		    cell.setCellValue(helper.stop-helper.start);
		    cell = sheet.getRow(k).createCell(2);
		    cell.setCellValue(helper.end-helper.stop);
		    cell = sheet.getRow(k).createCell(3);
		    cell.setCellValue(helper.end-helper.start);
		    
		    
		    file.close();
		    
		    FileOutputStream outFile =new FileOutputStream(file2);
		    workbook.write(outFile);
		    outFile.close();
		 //   Log.d("Timings",step+" - Req/Resp="+(helper.stop-helper.start)+" Parser="+(helper.end-helper.stop)+" E2E="+(helper.end-helper.start));
		     
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
    } 

}
