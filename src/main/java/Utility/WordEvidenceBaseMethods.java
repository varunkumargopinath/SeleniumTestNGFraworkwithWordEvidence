package Utility;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.testng.Assert;

public class WordEvidenceBaseMethods {
	
	// Apache Word Evidence variables
	public static String TestScriptName = "TS.01.01. Create and Order";
	public static int TestStepNo = 0;
	XWPFTable TestEnvDetailsTable ;
	public static XWPFDocument Evidence = new XWPFDocument();

	public void StartWordReader() {

		String env = System.getProperty("env");
		env = "https://demowebshop.tricentis.com/";
		try {

			XWPFHeader header = Evidence.createHeader(HeaderFooterType.DEFAULT);
			XWPFParagraph logopar = header.createParagraph();
			logopar.setAlignment(ParagraphAlignment.RIGHT);
			XWPFRun logorun = logopar.createRun();
			String imgFile = System.getProperty("user.dir") + "/src/main/resources/logo.png";

			InputStream is = new FileInputStream(imgFile);
			// Add the PNG image to the document
			logorun.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(45), Units.toEMU(45));
			// 45*45 pixels size, converted to EMUs (English Metric Units)

			// Enter Test script Name Header
			XWPFRun run = Evidence.createParagraph().createRun();
			run.setBold(true);
			run.setColor("4F81C8");
			run.setFontSize(15);
			run.setText(TestScriptName);
			// Enter Test Environment Details Header
			run = Evidence.createParagraph().createRun();
			run.setBold(true);
			run.setColor("4F81C8");
			run.setFontSize(13);
			run.setText("Test Environment Details:");

			// Environment details table
			TestEnvDetailsTable = Evidence.createTable();
			TestEnvDetailsTable.setWidth(9500);
			setColumnWidths(TestEnvDetailsTable, new int[] { 2200, 7300 });
			TestEnvDetailsTable.getRow(0).getCell(0)
					.setParagraph(EnterTextParagraph("Test Instance URL", true, "000000", 11));
			TestEnvDetailsTable.getRow(0).addNewTableCell().setParagraph(EnterTextParagraph(env, false, "000000", 11));
			TestEnvDetailsTable.createRow().getCell(0)
					.setParagraph(EnterTextParagraph("Host Machine Name", true, "000000", 11));
			TestEnvDetailsTable.getRow(1).getCell(1)
					.setParagraph(EnterTextParagraph(InetAddress.getLocalHost().getHostName(), false, "000000", 11));
			TestEnvDetailsTable.createRow().getCell(0).setParagraph(EnterTextParagraph("OS", true, "000000", 11));
			TestEnvDetailsTable.getRow(2).getCell(1)
					.setParagraph(EnterTextParagraph(System.getProperty("os.name"), false, "000000", 11));
			TestEnvDetailsTable.createRow().getCell(0).setParagraph(EnterTextParagraph("Browser", true, "000000", 11));
			TestEnvDetailsTable.getRow(3).getCell(1)
					.setParagraph(EnterTextParagraph(initialization.browser, false, "000000", 11));
			
			TestEnvDetailsTable.createRow().getCell(0).setParagraph(EnterTextParagraph("Start Time", true, "000000", 11));
			TestEnvDetailsTable.getRow(4).getCell(1)
					.setParagraph(EnterTextParagraph(getCurrentISTTime(), false, "000000", 11));
			
			Evidence.createParagraph().createRun().addBreak();
			Evidence.createParagraph().createRun().addBreak();
			// Create a table for execution Header
			XWPFTable ExecutionTableHeader = Evidence.createTable();
			// Set the width of the table (in points)
			ExecutionTableHeader.setWidth(9500);

			// create first row
			XWPFTableRow HeaderRow = ExecutionTableHeader.getRow(0);
			HeaderRow.getCell(0).setParagraph(EnterTextParagraph("Step No.", true, "000000", 11));
			HeaderRow.addNewTableCell().setParagraph(EnterTextParagraph("Test Step Description", true, "000000", 11));
			HeaderRow.addNewTableCell().setParagraph(EnterTextParagraph("Status", true, "000000", 11));
			// Set different column widths
			setColumnWidths(ExecutionTableHeader, new int[] { 1000, 7500, 1000 });
			// Mark the header row to repeat on every page
			HeaderRow.setRepeatHeader(true);

			XWPFTable ExecutionTable = Evidence.createTable();
			XWPFTableRow addrow = ExecutionTable.getRow(0);
			addrow.getCell(0).setParagraph(EnterTextParagraph(String.valueOf(++TestStepNo), false, "000000", 11));
			addrow.createCell().setParagraph(EnterTextParagraph("Test Execution Started", false, "000000", 11));
			addrow.createCell().setParagraph(EnterTextParagraph("Info", false, "000000", 11));
			setColumnWidths(ExecutionTable, new int[] { 1000, 7500, 1000 });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void EnterTestStepDescriptionWithScreenshotForInfo(String[] LogDescriptionArr) {
		try {

			XWPFTable ExecutionTable = Evidence.createTable();
			XWPFTableRow addrow = ExecutionTable.getRow(0);
			addrow.getCell(0).setParagraph(EnterTextParagraph(String.valueOf(++TestStepNo), false, "000000", 11));
			addrow.createCell().setParagraph(EnterTextParagraphWithAddBreak(LogDescriptionArr, false, "000000", 11));
			addrow.createCell().setParagraph(EnterTextParagraph("Info", false, "000000", 11));
			setColumnWidths(ExecutionTable, new int[] { 1000, 7500, 1000 });

			XWPFTable ScreenshotTable = Evidence.createTable();
			setColumnWidths(ScreenshotTable, new int[] { 9500 });
			XWPFParagraph TempPar= EnterScreenshotParagraph();
			ScreenshotTable.getRow(0).getCell(0).setParagraph(TempPar);
			TempPar.removeRun(0);
			Evidence.removeBodyElement(Evidence.getPosOfParagraph(TempPar));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	public static XWPFParagraph EnterScreenshotParagraph() {
		XWPFParagraph ScreenshotPar = null;
		
		try {
			String screenshot = initialization.getScreenshot(initialization.driver);
			
			InputStream screenshotis = new FileInputStream(screenshot);
			ScreenshotPar =Evidence.createParagraph();
			ScreenshotPar.setAlignment(ParagraphAlignment.CENTER);
			ScreenshotPar.setSpacingBefore(250);

			ScreenshotPar.createRun().addPicture(screenshotis, XWPFDocument.PICTURE_TYPE_PNG, screenshot,
					Units.toEMU(460), Units.toEMU(230));
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ScreenshotPar;
	}

	public static XWPFParagraph EnterTextParagraphWithAddBreak(String[] text, boolean bold, String RGbHexcolour,
			int fontSize) {

		XWPFParagraph paragraph = new XWPFDocument().createParagraph();
		paragraph.setSpacingBefore(50);
		XWPFRun run = paragraph.createRun();
		run.setBold(bold);
		run.setColor(RGbHexcolour);
		run.setFontSize(fontSize);
		run.setText(" " + text[0]);

		for (int i = 1; i < text.length; i++) {
			run.addBreak();
			run.setText(" " + text[i]);
		}
		return paragraph;
	}

	public static XWPFParagraph EnterTextParagraph(String text, boolean bold, String RGbHexcolour, int fontSize) {

		XWPFParagraph paragraph = new XWPFDocument().createParagraph();
		paragraph.setSpacingBefore(50);
		XWPFRun run = paragraph.createRun();
		run.setBold(bold);
		run.setColor(RGbHexcolour);
		run.setFontSize(fontSize);
		run.setText(" " + text);
		return paragraph;
	}

	public static void setColumnWidths(XWPFTable table, int[] widths) {
		// Access the table's underlying XML structure to set column widths
		CTTbl ttbl = table.getCTTbl();
		CTTblGrid tblGrid = ttbl.getTblGrid() == null ? ttbl.addNewTblGrid() : ttbl.getTblGrid();

		for (int width : widths) {
			CTTblGridCol gridCol = tblGrid.addNewGridCol();
			gridCol.setW(BigInteger.valueOf(width)); // Set the width in TWIPs
		}

		// For each row, set the width of each cell
		for (XWPFTableRow row : table.getRows()) {
			for (int i = 0; i < row.getTableCells().size(); i++) {
				CTTc cttc = row.getCell(i).getCTTc();
				CTTblWidth tblWidth = cttc.addNewTcPr().addNewTcW();
				tblWidth.setW(BigInteger.valueOf(widths[i]));
				tblWidth.setType(STTblWidth.DXA);
			}
		}
	}

	public static String getCurrentISTTime(){
		//Start Time
		ZonedDateTime currentTimeIST = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        // Format the time with the IST tag
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'IST'");
        return currentTimeIST.format(formatter);
	}
}
