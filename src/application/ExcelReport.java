package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

import com.sun.jmx.snmp.Timestamp;

import javafx.collections.ObservableList;
import model.AuthorBook;




public class ExcelReport {
	
	public static String DEST_FILE_PATH;
	private AuthorBook authorb = new AuthorBook();
	public ExcelReport() {
		
	}
	public void Generate(String publisher, ArrayList<ObservableList<AuthorBook>> aBook, String path) {
		DEST_FILE_PATH = path;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("RoyaltyReport");

		//title row
		//create a title font
		HSSFFont titleFont = workbook.createFont();
		titleFont.setFontName("Title font");
		titleFont.setFontHeightInPoints((short) 24);
		titleFont.setBold(true);
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);
		
		//date font
		HSSFFont dateFont = workbook.createFont();
		dateFont.setFontName("date font");
		dateFont.setFontHeightInPoints((short) 20);
		dateFont.setBold(true);
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFont(dateFont);
		
		//sheet.setColumnWidth(0, 5000);
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Royalty Report");
		cell.setCellStyle(titleStyle);
		
		//publisher
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("Publisher: " + publisher);
		cell.setCellStyle(titleStyle);
		
		//timestamp
		long millis=System.currentTimeMillis();  
		java.util.Date date=new java.util.Date(millis);   
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue("Report generated on " + date);
		cell.setCellStyle(dateStyle);
		//row indexes are base 0 of course
		//let's put a space between title and header rows, and start header and data on row 2
		int rowNum = 4;

		//set default alignment of Age column to be centered
		HSSFCellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);
		sheet.setDefaultColumnStyle(2, centerStyle);
		
		//create a bold font to use for header row and summary row
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		HSSFCellStyle boldStyle = workbook.createCellStyle();
		boldStyle.setFont(font);
		//create a bold + centered font to use for age header and average
		//surely there is a better way to apply 2 different styles to 1 cell???
		HSSFCellStyle boldCenterStyle = workbook.createCellStyle();
		boldCenterStyle.cloneStyleFrom(boldStyle);
		boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);

		//create header row
		row = sheet.createRow(rowNum++);
		//add cells 0 to 3
		int cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Book Title");
		cell.setCellStyle(boldStyle);

		cell = row.createCell(cellNum++);
		cell.setCellValue("ISBN");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("Author");
		cell.setCellStyle(boldCenterStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("Royalty");
		cell.setCellStyle(boldStyle);

		boolean first;
		int totalRoyal;
		for (int i = 0; i < aBook.size(); i++) {
			totalRoyal = 0;
			first = true;
			for (int j = 0; j < aBook.get(i).size(); j++) {
				authorb = aBook.get(i).get(j);
				row = sheet.createRow(rowNum++);
				if (first) {
					cellNum = 0;
					cell = row.createCell(cellNum++);
					cell.setCellValue(authorb.getBook().getTitle());
					cell = row.createCell(cellNum++);
					cell.setCellValue(authorb.getBook().getisbn());
					cell = row.createCell(cellNum++);
					cell.setCellValue(authorb.getAuthor().getAuthorFirstName());
					cell = row.createCell(cellNum++);
					cell.setCellValue(authorb.getRoyalty() + "%");
					totalRoyal += authorb.getRoyalty();
				}
				if (!first) {
					System.out.println("HERE");
					cell = row.createCell(2);
					cell.setCellValue(authorb.getAuthor().getAuthorFirstName());
					cell = row.createCell(3);
					cell.setCellValue(authorb.getRoyalty() + "%");
					totalRoyal += authorb.getRoyalty();
				}
				first = false;
			}
			row = sheet.createRow(rowNum++);
			cell = row.createCell(2);
			cell.setCellValue("Total Royalty");
			cell.setCellStyle(boldStyle);
			cell = row.createCell(3);
			cell.setCellValue(totalRoyal + "%");
			cell.setCellStyle(boldStyle);
			rowNum++;
		}
		for (int k = 0; k < 4; k++) {
			sheet.autoSizeColumn(k);
		}
		//write to a file
		try (FileOutputStream f = new FileOutputStream(new File(DEST_FILE_PATH))) {
			workbook.write(f);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}