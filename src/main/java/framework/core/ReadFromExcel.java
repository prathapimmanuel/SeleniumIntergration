package framework.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;


/**
 * This method will be used get the column number based on column Identifier
 * 
 * @param strSheetName
 * 				Sheet Name in the workbook
 * @param strColIdentifier
 * 				Column Name in the sheet
 * @return int column number
 * @throws IOException
 */
public class ReadFromExcel {

	
	
	public ArrayList<String> extractExcelContentByColumnIndex(int columnIndex,
			XSSFSheet sheet) { 
		ArrayList<String> columndata = null;
		try {
			int rowCount = sheet.getPhysicalNumberOfRows();
			Iterator<Row> rowIterator = sheet.iterator();
			columndata = new ArrayList<>(); 

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if (row.getRowNum() > 0) { // To filter column headings
						if (cell.getColumnIndex() == columnIndex) {// To match
																	// column
								 									// index
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								columndata.add(cell.getNumericCellValue() + "");
								break;

							case Cell.CELL_TYPE_STRING:
								columndata.add(cell.getStringCellValue());
								break;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return columndata;
	}
	

	/**
	 * This method will be used get the column number based on column Identifier
	 * 
	 * @param strSheetName
	 * 				Sheet Name in the workbook
	 * @param strColIdentifier
	 * 				Column Name in the sheet
	 * @return int column number
	 * @throws IOException
	 */
/*	public static int getColumnNumber(String strFileName,String strSheetName, String strColIdentifier) throws IOException {
		Workbook objWorkBook;
		Sheet objCurrentSheet;
		String strAbsFilePath = getAbsolutePath(strFileName);
		int col;
		try {
			objWorkBook = Workbook.getWorkbook(new File(strAbsFilePath));
			objCurrentSheet = objWorkBook.getSheet(strSheetName);
			col = objCurrentSheet.findCell(strColIdentifier).getColumn();
			objWorkBook.close();
			return col;
		} catch (Exception e) {
			return -1;
		}
	}
*/
	/**
	 * This method is used to fetch the absolute path of the given excel sheet
	 * 
	 * @param strFilePath
	 * @return String full path of the file
	 */
	private static String getAbsolutePath(String strFilePath) {
		try {
			if (strFilePath.contains("https")) {
				return strFilePath;
			}
			else {
				return new File(strFilePath).getAbsolutePath();
			}
		} catch (Exception objException) {
			return "";
		}
	}
}
