package com.iie.gxb.textengine.extract;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hdgf.extractor.VisioTextExtractor;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

//import chinese.ZHConverter;

public class ExtractText {

	private static String Text;

	public static String extractText(String filePath, String fileName) {
		
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if ("doc".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextDoc(filePath);
		} else if ("docx".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextDocx(filePath);
		} else if ("pdf".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextPdf(filePath);
		} else if ("rtf".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextRtf(filePath);
		} else if ("html".toLowerCase().equals(formatName.toLowerCase())
				|| "xml".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextHtml(filePath);
		} else if ("txt".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextTxt(filePath);
		} else if ("xlsx".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextXlsx(filePath);
		} else if ("xls".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextXls(filePath);
		} else if ("ppt".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextPpt(filePath);
		} else if ("pptx".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextPptx(filePath);
		} else if ("vsd".toLowerCase().equals(formatName.toLowerCase())) {
			Text = getTextVsd(filePath);
		}
		return Text;
	}

	public static String[] ExtractWord(String path) {
		ArrayList<String> list = new ArrayList<String>();
		String[] keywords = null;
		Scanner scanner = null;
		//ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
		try {

			scanner = new Scanner(new File(path), getCharset(path));

		} // catch (FileNotFoundException e) {
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			String word = scanner.next();
			list.add(word);
			//System.out.println("j:"+word);
			//String traditionalStr = converter.convert(word);
			//list.add(traditionalStr);
			//System.out.println("f:"+traditionalStr);
		}

		keywords = new String[list.size()];

		for (int idx = 0; idx < list.size(); ++idx) {
			if (idx == 0) {
				if (list.get(idx).length() < 2) {
					keywords[idx] = " ";
				} else {
					keywords[idx] = list.get(idx).substring(1, list.get(idx).length());
				}
			} else {
				keywords[idx] = (String) list.get(idx);
			}
		}
		
		return keywords;
	}

	public static void writeStrToFile(String str, String path, String filename) throws Exception {
		Scanner input = new Scanner(str + "\n");

		path = path + filename + "." + "txt";
		FileOutputStream fos = new FileOutputStream(path);
		while (input.hasNext()) {
			String a = input.next();
			fos.write((a + "\r\n").getBytes());
		}
		fos.close();
		input.close();
	}

	private static String getTextDoc(String filePath) {
		String textdoc = null;
		try {
			InputStream is = new FileInputStream(new File(filePath));
			WordExtractor ex = new WordExtractor(is);
			textdoc = ex.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Text = textdoc;
		return Text;
	}

	private static String getTextDocx(String filePath) {
		String textdocx = null;
		try {

			OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
			POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
			textdocx = extractor.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Text = textdocx;
		return Text;
	}

	private static String getTextPdf(String filePath) {
		String textpdf = null;
		FileInputStream is = null;
		PDDocument document = null;
		try {
			is = new FileInputStream(filePath);
			PDFParser parser = new PDFParser(is);
			parser.parse();
			document = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			textpdf = stripper.getText(document);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Text = textpdf;
		return Text;
	}

	private static String getTextRtf(String filePath) {
		String textrtf = null;
		File file = new File(filePath);
		try {
			System.out.println(getCharset(filePath));
			DefaultStyledDocument styledDoc = new DefaultStyledDocument();
			InputStream is = new FileInputStream(file);
			new RTFEditorKit().read(is, styledDoc, 0);
			textrtf = new String(styledDoc.getText(0, styledDoc.getLength()).getBytes("ISO8859_1"), "GBK");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		Text = textrtf;
		return Text;
	}

	private static String getTextHtml(String filePath) {
		String texthtml = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		texthtml = sb.toString();
		Text = texthtml;
		return Text;
	}

	private static String getTextTxt(String filePath) {
		String texttxt = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), getCharset(filePath));
			BufferedReader br = new BufferedReader(isr);

			StringBuffer sb = new StringBuffer();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp + "\r\n");
			}
			br.close();
			texttxt = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Text = texttxt;
		return Text;
	}

	private static String getTextXls(String filePath) {
		String textxls = null;
		StringBuffer buff = new StringBuffer();
		try {
			// 鍒涘缓瀵笶xcel宸ヤ綔绨挎枃浠剁殑寮曠敤
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
			// 鍒涘缓瀵瑰伐浣滆〃鐨勫紩鐢ㄣ��
			for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
				if (null != wb.getSheetAt(numSheets)) {
					HSSFSheet aSheet = wb.getSheetAt(numSheets);// 鑾峰緱涓�涓猻heet
					for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 鑾峰緱涓�涓
							for (int cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
								if (null != aRow.getCell(cellNumOfRow)) {
									HSSFCell aCell = aRow.getCell(cellNumOfRow);// 鑾峰緱鍒楀��
									switch (aCell.getCellType()) {
									case HSSFCell.CELL_TYPE_FORMULA:
										break;
									case HSSFCell.CELL_TYPE_NUMERIC:
										buff.append(aCell.getNumericCellValue()).append('\t');
										break;
									case HSSFCell.CELL_TYPE_STRING:
										buff.append(aCell.getStringCellValue()).append('\t');
										break;
									}
								}
							}
							buff.append('\n');
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		textxls = buff.toString();
		Text = textxls;
		return Text;
	}

	private static String getTextXlsx(String filePath) {
		String textxlsx = null;
		StringBuffer buff = new StringBuffer();
		try {
			// 鍒涘缓瀵笶xcel宸ヤ綔绨挎枃浠剁殑寮曠敤
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));
			// 鍒涘缓瀵瑰伐浣滆〃鐨勫紩鐢ㄣ��
			for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
				if (null != wb.getSheetAt(numSheets)) {
					XSSFSheet aSheet = wb.getSheetAt(numSheets);// 鑾峰緱涓�涓猻heet
					for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							XSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 鑾峰緱涓�涓
							for (int cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
								if (null != aRow.getCell(cellNumOfRow)) {
									XSSFCell aCell = aRow.getCell(cellNumOfRow);// 鑾峰緱鍒楀��
									switch (aCell.getCellType()) {
									case XSSFCell.CELL_TYPE_FORMULA:
										break;
									case XSSFCell.CELL_TYPE_NUMERIC:
										buff.append(aCell.getNumericCellValue()).append('\t');
										break;
									case XSSFCell.CELL_TYPE_STRING:
										buff.append(aCell.getStringCellValue()).append('\t');
										break;
									}
								}
							}
							buff.append('\n');
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		textxlsx = buff.toString();
		Text = textxlsx;
		return Text;
	}

	private static String getTextPpt(String filePath) {
		String textppt = null;
		StringBuffer content = new StringBuffer("");
		try {

			SlideShow ss = new SlideShow(new HSLFSlideShow(filePath));// path涓烘枃浠剁殑鍏ㄨ矾寰勫悕绉帮紝寤虹珛SlideShow
			Slide[] slides = ss.getSlides();// 鑾峰緱姣忎竴寮犲够鐏墖
			for (int i = 0; i < slides.length; i++) {
				TextRun[] t = slides[i].getTextRuns();// 涓轰簡鍙栧緱骞荤伅鐗囩殑鏂囧瓧鍐呭锛屽缓绔婽extRun
				for (int j = 0; j < t.length; j++) {
					content.append(t[j].getText());// 杩欓噷浼氬皢鏂囧瓧鍐呭鍔犲埌content涓幓
				}
				content.append(slides[i].getTitle());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		textppt = content.toString();
		Text = textppt;
		return Text;
	}

	private static String getTextPptx(String filePath) {
		String textpptx = null;
		@SuppressWarnings("unused")
		StringBuffer content = new StringBuffer("");
		// pptx
		XSLFSlideShow slideShow;
		// String reusltString=null;
		try {
			slideShow = new XSLFSlideShow(filePath);
			XMLSlideShow xmlSlideShow = new XMLSlideShow(slideShow);
			XSLFSlide[] slides = xmlSlideShow.getSlides();
			StringBuilder sb = new StringBuilder();
			for (XSLFSlide slide : slides) {
				CTSlide rawSlide = (CTSlide) slide._getCTSlide();
				// ;
				CTGroupShape gs = rawSlide.getCSld().getSpTree();
				CTShape[] shapes = gs.getSpArray();
				for (CTShape shape : shapes) {
					CTTextBody tb = shape.getTxBody();
					if (null == tb)
						continue;
					CTTextParagraph[] paras = tb.getPArray();
					for (CTTextParagraph textParagraph : paras) {
						CTRegularTextRun[] textRuns = textParagraph.getRArray();
						for (CTRegularTextRun textRun : textRuns) {
							sb.append(textRun.getT());
						}
					}
				}
			}
			textpptx = sb.toString();
		} catch (OpenXML4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Text = textpptx;
		return Text;
	}

	private static String getTextVsd(String filePath) {
		String textvsd = null;
		try {
			InputStream visio = new FileInputStream(new File(filePath));
			VisioTextExtractor visiototext = new VisioTextExtractor(visio);
			textvsd = visiototext.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Text = textvsd;
		return Text;
	}

	private static String getCharset(String fileName) throws IOException {

		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();

		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}
		bin.close();
		return code;
	}

}
